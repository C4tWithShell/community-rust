// Copyright 2018-2021 the Deno authors. All rights reserved. MIT license.
use rusty_v8 as v8;
use serde::ser;
use serde::ser::Serialize;

use std::cell::RefCell;

use crate::error::{Error, Result};
use crate::keys::v8_struct_key;
use crate::magic;

type JsValue<'s> = v8::Local<'s, v8::Value>;
type JsResult<'s> = Result<JsValue<'s>>;

type ScopePtr<'a, 'b, 'c> = &'c RefCell<&'b mut v8::HandleScope<'a>>;

pub fn to_v8<'a, T>(scope: &mut v8::HandleScope<'a>, input: T) -> JsResult<'a>
where
  T: Serialize,
{
  let scopeptr = RefCell::new(scope);
  let serializer = Serializer::new(&scopeptr);

  input.serialize(serializer)
}

/// Wraps other serializers into an enum tagged variant form.
/// Uses {"Variant": ...payload...} for compatibility with serde-json.
pub struct VariantSerializer<'a, 'b, 'c, S> {
  inner: S,
  scope: ScopePtr<'a, 'b, 'c>,
  variant: &'static str,
}

impl<'a, 'b, 'c, S> VariantSerializer<'a, 'b, 'c, S> {
  pub fn new(
    scope: ScopePtr<'a, 'b, 'c>,
    variant: &'static str,
    inner: S,
  ) -> Self {
    Self {
      inner,
      scope,
      variant,
    }
  }

  fn end(self, inner: impl FnOnce(S) -> JsResult<'a>) -> JsResult<'a> {
    let value = inner(self.inner)?;
    let scope = &mut *self.scope.borrow_mut();
    let obj = v8::Object::new(scope);
    let key = v8_struct_key(scope, self.variant).into();
    obj.set(scope, key, value);
    Ok(obj.into())
  }
}

impl<'a, 'b, 'c, S> ser::SerializeTupleVariant
  for VariantSerializer<'a, 'b, 'c, S>
where
  S: ser::SerializeTupleStruct<Ok = JsValue<'a>, Error = Error>,
{
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    value: &T,
  ) -> Result<()> {
    self.inner.serialize_field(value)
  }

  fn end(self) -> JsResult<'a> {
    self.end(S::end)
  }
}

impl<'a, 'b, 'c, S> ser::SerializeStructVariant
  for VariantSerializer<'a, 'b, 'c, S>
where
  S: ser::SerializeStruct<Ok = JsValue<'a>, Error = Error>,
{
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    key: &'static str,
    value: &T,
  ) -> Result<()> {
    self.inner.serialize_field(key, value)
  }

  fn end(self) -> JsResult<'a> {
    self.end(S::end)
  }
}

pub struct ArraySerializer<'a, 'b, 'c> {
  pending: Vec<JsValue<'a>>,
  scope: ScopePtr<'a, 'b, 'c>,
}

impl<'a, 'b, 'c> ArraySerializer<'a, 'b, 'c> {
  pub fn new(scope: ScopePtr<'a, 'b, 'c>, len: Option<usize>) -> Self {
    let pending = match len {
      Some(len) => Vec::with_capacity(len),
      None => vec![],
    };
    Self { pending, scope }
  }
}

impl<'a, 'b, 'c> ser::SerializeSeq for ArraySerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_element<T: ?Sized + Serialize>(
    &mut self,
    value: &T,
  ) -> Result<()> {
    let x = value.serialize(Serializer::new(self.scope))?;
    self.pending.push(x);
    Ok(())
  }

  fn end(self) -> JsResult<'a> {
    let elements = self.pending.iter().as_slice();
    let scope = &mut *self.scope.borrow_mut();
    let arr = v8::Array::new_with_elements(scope, elements);
    Ok(arr.into())
  }
}

impl<'a, 'b, 'c> ser::SerializeTuple for ArraySerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_element<T: ?Sized + Serialize>(
    &mut self,
    value: &T,
  ) -> Result<()> {
    ser::SerializeSeq::serialize_element(self, value)
  }

  fn end(self) -> JsResult<'a> {
    ser::SerializeSeq::end(self)
  }
}

impl<'a, 'b, 'c> ser::SerializeTupleStruct for ArraySerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    value: &T,
  ) -> Result<()> {
    ser::SerializeTuple::serialize_element(self, value)
  }

  fn end(self) -> JsResult<'a> {
    ser::SerializeTuple::end(self)
  }
}

pub struct ObjectSerializer<'a, 'b, 'c> {
  scope: ScopePtr<'a, 'b, 'c>,
  obj: v8::Local<'a, v8::Object>,
}

impl<'a, 'b, 'c> ObjectSerializer<'a, 'b, 'c> {
  pub fn new(scope: ScopePtr<'a, 'b, 'c>) -> Self {
    let obj = v8::Object::new(&mut *scope.borrow_mut());
    Self { scope, obj }
  }
}

impl<'a, 'b, 'c> ser::SerializeStruct for ObjectSerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    key: &'static str,
    value: &T,
  ) -> Result<()> {
    let value = value.serialize(Serializer::new(self.scope))?;
    let scope = &mut *self.scope.borrow_mut();
    let key = v8_struct_key(scope, key).into();
    self.obj.set(scope, key, value);
    Ok(())
  }

  fn end(self) -> JsResult<'a> {
    Ok(self.obj.into())
  }
}

pub struct MagicSerializer<'a> {
  v8_value: Option<v8::Local<'a, v8::Value>>,
}

impl<'a> ser::SerializeStruct for MagicSerializer<'a> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    key: &'static str,
    value: &T,
  ) -> Result<()> {
    if key != magic::FIELD {
      unreachable!();
    }
    let transmuted: u64 = value.serialize(magic::FieldSerializer {})?;
    let mv: magic::Value<'a> = unsafe { std::mem::transmute(transmuted) };
    self.v8_value = Some(mv.v8_value);
    Ok(())
  }

  fn end(self) -> JsResult<'a> {
    Ok(self.v8_value.unwrap())
  }
}

// TODO(@AaronO): refactor this and streamline how we transmute values
pub struct MagicBufferSerializer<'a, 'b, 'c> {
  scope: ScopePtr<'a, 'b, 'c>,
  f1: u64,
  f2: u64,
}

impl<'a, 'b, 'c> MagicBufferSerializer<'a, 'b, 'c> {
  pub fn new(scope: ScopePtr<'a, 'b, 'c>) -> Self {
    Self {
      scope,
      f1: 0,
      f2: 0,
    }
  }
}

impl<'a, 'b, 'c> ser::SerializeStruct for MagicBufferSerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    key: &'static str,
    value: &T,
  ) -> Result<()> {
    // Get u64 chunk
    let transmuted: u64 = value.serialize(magic::FieldSerializer {})?;
    match key {
      magic::buffer::BUF_FIELD_1 => self.f1 = transmuted,
      magic::buffer::BUF_FIELD_2 => self.f2 = transmuted,
      _ => unreachable!(),
    }
    Ok(())
  }

  fn end(self) -> JsResult<'a> {
    let x: [usize; 2] = [self.f1 as usize, self.f2 as usize];
    let buf: Box<[u8]> = unsafe { std::mem::transmute(x) };
    let scope = &mut *self.scope.borrow_mut();
    let v8_value = boxed_slice_to_uint8array(scope, buf);
    Ok(v8_value.into())
  }
}

// Dispatches between magic and regular struct serializers
pub enum StructSerializers<'a, 'b, 'c> {
  Magic(MagicSerializer<'a>),
  MagicBuffer(MagicBufferSerializer<'a, 'b, 'c>),
  Regular(ObjectSerializer<'a, 'b, 'c>),
}

impl<'a, 'b, 'c> ser::SerializeStruct for StructSerializers<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_field<T: ?Sized + Serialize>(
    &mut self,
    key: &'static str,
    value: &T,
  ) -> Result<()> {
    match self {
      StructSerializers::Magic(s) => s.serialize_field(key, value),
      StructSerializers::MagicBuffer(s) => s.serialize_field(key, value),
      StructSerializers::Regular(s) => s.serialize_field(key, value),
    }
  }

  fn end(self) -> JsResult<'a> {
    match self {
      StructSerializers::Magic(s) => s.end(),
      StructSerializers::MagicBuffer(s) => s.end(),
      StructSerializers::Regular(s) => s.end(),
    }
  }
}

// Serializes to JS Objects, NOT JS Maps ...
pub struct MapSerializer<'a, 'b, 'c> {
  scope: ScopePtr<'a, 'b, 'c>,
  obj: v8::Local<'a, v8::Object>,
  next_key: Option<JsValue<'a>>,
}

impl<'a, 'b, 'c> MapSerializer<'a, 'b, 'c> {
  pub fn new(scope: ScopePtr<'a, 'b, 'c>) -> Self {
    let obj = v8::Object::new(&mut *scope.borrow_mut());
    Self {
      scope,
      obj,
      next_key: None,
    }
  }
}

impl<'a, 'b, 'c> ser::SerializeMap for MapSerializer<'a, 'b, 'c> {
  type Ok = JsValue<'a>;
  type Error = Error;

  fn serialize_key<T: ?Sized + Serialize>(&mut self, key: &T) -> Result<()> {
    debug_assert!(self.next_key.is_none());
    self.next_key = Some(key.serialize(Serializer::new(self.scope))?);
    Ok(())
  }

  fn serialize_value<T: ?Sized + Serialize>(
    &mut self,
    value: &T,
  ) -> Result<()> {
    let v8_value = value.serialize(Serializer::new(self.scope))?;
    let scope = &mut *self.scope.borrow_mut();
    self.obj.set(scope, self.next_key.take().unwrap(), v8_value);
    Ok(())
  }

  fn end(self) -> JsResult<'a> {
    debug_assert!(self.next_key.is_none());
    Ok(self.obj.into())
  }
}

pub struct Serializer<'a, 'b, 'c> {
  scope: ScopePtr<'a, 'b, 'c>,
}

impl<'a, 'b, 'c> Serializer<'a, 'b, 'c> {
  pub fn new(scope: ScopePtr<'a, 'b, 'c>) -> Self {
    Serializer { scope }
  }
}

macro_rules! forward_to {
    ($($name:ident($ty:ty, $to:ident, $lt:lifetime);)*) => {
        $(fn $name(self, v: $ty) -> JsResult<$lt> {
            self.$to(v as _)
        })*
    };
}

impl<'a, 'b, 'c> ser::Serializer for Serializer<'a, 'b, 'c> {
  type Ok = v8::Local<'a, v8::Value>;
  type Error = Error;

  type SerializeSeq = ArraySerializer<'a, 'b, 'c>;
  type SerializeTuple = ArraySerializer<'a, 'b, 'c>;
  type SerializeTupleStruct = ArraySerializer<'a, 'b, 'c>;
  type SerializeTupleVariant =
    VariantSerializer<'a, 'b, 'c, ArraySerializer<'a, 'b, 'c>>;
  type SerializeMap = MapSerializer<'a, 'b, 'c>;
  type SerializeStruct = StructSerializers<'a, 'b, 'c>;
  type SerializeStructVariant =
    VariantSerializer<'a, 'b, 'c, StructSerializers<'a, 'b, 'c>>;

  forward_to! {
      serialize_i8(i8, serialize_i32, 'a);
      serialize_i16(i16, serialize_i32, 'a);

      serialize_u8(u8, serialize_u32, 'a);
      serialize_u16(u16, serialize_u32, 'a);

      serialize_f32(f32, serialize_f64, 'a);
      serialize_u64(u64, serialize_f64, 'a);
      serialize_i64(i64, serialize_f64, 'a);
  }

  fn serialize_i32(self, v: i32) -> JsResult<'a> {
    Ok(v8::Integer::new(&mut self.scope.borrow_mut(), v).into())
  }

  fn serialize_u32(self, v: u32) -> JsResult<'a> {
    Ok(v8::Integer::new_from_unsigned(&mut self.scope.borrow_mut(), v).into())
  }

  fn serialize_f64(self, v: f64) -> JsResult<'a> {
    Ok(v8::Number::new(&mut self.scope.borrow_mut(), v).into())
  }

  fn serialize_bool(self, v: bool) -> JsResult<'a> {
    Ok(v8::Boolean::new(&mut self.scope.borrow_mut(), v).into())
  }

  fn serialize_char(self, _v: char) -> JsResult<'a> {
    unimplemented!();
  }

  fn serialize_str(self, v: &str) -> JsResult<'a> {
    v8::String::new(&mut self.scope.borrow_mut(), v)
      .map(|v| v.into())
      .ok_or(Error::ExpectedString)
  }

  fn serialize_bytes(self, _v: &[u8]) -> JsResult<'a> {
    // TODO: investigate using Uint8Arrays
    unimplemented!()
  }

  fn serialize_none(self) -> JsResult<'a> {
    Ok(v8::null(&mut self.scope.borrow_mut()).into())
  }

  fn serialize_some<T: ?Sized + Serialize>(self, value: &T) -> JsResult<'a> {
    value.serialize(self)
  }

  fn serialize_unit(self) -> JsResult<'a> {
    Ok(v8::null(&mut self.scope.borrow_mut()).into())
  }

  fn serialize_unit_struct(self, _name: &'static str) -> JsResult<'a> {
    Ok(v8::null(&mut self.scope.borrow_mut()).into())
  }

  /// For compatibility with serde-json, serialises unit variants as "Variant" strings.
  fn serialize_unit_variant(
    self,
    _name: &'static str,
    _variant_index: u32,
    variant: &'static str,
  ) -> JsResult<'a> {
    Ok(v8_struct_key(&mut self.scope.borrow_mut(), variant).into())
  }

  fn serialize_newtype_struct<T: ?Sized + Serialize>(
    self,
    _name: &'static str,
    value: &T,
  ) -> JsResult<'a> {
    value.serialize(self)
  }

  fn serialize_newtype_variant<T: ?Sized + Serialize>(
    self,
    _name: &'static str,
    _variant_index: u32,
    variant: &'static str,
    value: &T,
  ) -> JsResult<'a> {
    let scope = self.scope;
    let x = self.serialize_newtype_struct(variant, value)?;
    VariantSerializer::new(scope, variant, x).end(Ok)
  }

  /// Serialises any Rust iterable into a JS Array
  fn serialize_seq(self, len: Option<usize>) -> Result<Self::SerializeSeq> {
    Ok(ArraySerializer::new(self.scope, len))
  }

  fn serialize_tuple(self, len: usize) -> Result<Self::SerializeTuple> {
    self.serialize_seq(Some(len))
  }

  fn serialize_tuple_struct(
    self,
    _name: &'static str,
    len: usize,
  ) -> Result<Self::SerializeTupleStruct> {
    self.serialize_tuple(len)
  }

  fn serialize_tuple_variant(
    self,
    _name: &'static str,
    _variant_index: u32,
    variant: &'static str,
    len: usize,
  ) -> Result<Self::SerializeTupleVariant> {
    Ok(VariantSerializer::new(
      self.scope,
      variant,
      self.serialize_tuple_struct(variant, len)?,
    ))
  }

  fn serialize_map(self, _len: Option<usize>) -> Result<Self::SerializeMap> {
    // Serializes a rust Map (e.g: BTreeMap, HashMap) to a v8 Object
    // TODO: consider allowing serializing to v8 Maps (e.g: via a magic type)
    // since they're lighter and better suited for K/V data
    // and maybe restrict keys (e.g: strings and numbers)
    Ok(MapSerializer::new(self.scope))
  }

  /// Serialises Rust typed structs into plain JS objects.
  fn serialize_struct(
    self,
    name: &'static str,
    _len: usize,
  ) -> Result<Self::SerializeStruct> {
    match name {
      magic::NAME => {
        let m: MagicSerializer<'a> = MagicSerializer { v8_value: None };
        Ok(StructSerializers::Magic(m))
      }
      magic::buffer::BUF_NAME => {
        let m = MagicBufferSerializer::new(self.scope);
        Ok(StructSerializers::MagicBuffer(m))
      }
      _ => {
        let o = ObjectSerializer::new(self.scope);
        Ok(StructSerializers::Regular(o))
      }
    }
  }

  fn serialize_struct_variant(
    self,
    _name: &'static str,
    _variant_index: u32,
    variant: &'static str,
    len: usize,
  ) -> Result<Self::SerializeStructVariant> {
    let scope = self.scope;
    let x = self.serialize_struct(variant, len)?;
    Ok(VariantSerializer::new(scope, variant, x))
  }
}

// Used to map MagicBuffers to v8
pub fn boxed_slice_to_uint8array<'a>(
  scope: &mut v8::HandleScope<'a>,
  buf: Box<[u8]>,
) -> v8::Local<'a, v8::Uint8Array> {
  if buf.is_empty() {
    let ab = v8::ArrayBuffer::new(scope, 0);
    return v8::Uint8Array::new(scope, ab, 0, 0)
      .expect("Failed to create UintArray8");
  }
  let buf_len = buf.len();
  let backing_store = v8::ArrayBuffer::new_backing_store_from_boxed_slice(buf);
  let backing_store_shared = backing_store.make_shared();
  let ab = v8::ArrayBuffer::with_backing_store(scope, &backing_store_shared);
  v8::Uint8Array::new(scope, ab, 0, buf_len)
    .expect("Failed to create UintArray8")
}
