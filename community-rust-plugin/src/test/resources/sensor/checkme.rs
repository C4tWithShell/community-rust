impl<T: Encode> Encode for Vec<T> {
    default fn is_empty(&self) -> bool {
        Vec::is_empty(self)
    }

    default fn into_cbor_value(self) -> Value {
        Value::Array(self.into_iter().map(Encode::into_cbor_value).collect())
    }
}

