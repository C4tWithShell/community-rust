/**
 * Community Rust Plugin
 * Copyright (C) 2021-2022 Eric Le Goff
 * mailto:community-rust AT pm DOT me
 * http://github.com/elegoff/sonar-rust
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */
package org.sonar.rust.parser.macro;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class MacroTest {

  @Test
  public void testTokenExceptDelimiters() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TOKEN_EXCEPT_DELIMITERS))
      .matches("abc")
      .matches("42")
      .matches(";")
      .matches("\"hello\"")
      .notMatches("\"hello\")")
      .notMatches("(")
      .notMatches("{")
      .notMatches("[");
  }

  @Test
  public void testDelimTokenTree() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.DELIM_TOKEN_TREE))
      .matches("(abc)")
      .matches("(\"hello\")")
      .matches("()")
      .matches("(\"{}, {}\", word, j)")
      .notMatches("");
  }

  @Test
  public void testTokenTree() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.TOKEN_TREE))
      .matches("abc")
      .matches("\"hello\"")
      .matches("(abc)")
      .matches("[abc]")
      .matches("{abc}")
      .matches("(a(bc))")
      .matches("{a(bc)[(de)(fgh)]}")
      .matches("()")
      .notMatches("");
  }

  @Test
  public void testMacroInvocationSemi() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_INVOCATION_SEMI))
      .matches("j!(AS);")
      .matches("println!(\"hello\");")
      .matches("println!(\"hello,world!\");")
      .matches("println!(\"hello,{}\",k);")
      .matches("println!(\"hello,{}\", j);")
      .matches("println!(\"{}, {}\", word, j);")
      .notMatches("")
      .matches("assert_eq!(state.borrow::<MyStruct>().value, 110);")
      .matches("quote!{k}");
  }

  @Test
  public void testMacroInvocation() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_INVOCATION))
      .matches("std::io::Write!()")
      .matches("panic!()")
      .matches("println!(\"{}, {}\", word, j)")
      .notMatches("")
      .matches("Token![#]")
      .matches("syn_dev::r#mod! {\n" +
        "    // Write Rust code here and run `cargo check` to have Syn parse it.\n" +
        "\n" +
        "}")
      .matches("Token![const]")
      .matches("Token![~]")
      .matches("quote!{k}")

    ;
  }

  @Test
  public void testMacroFragSpec() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_FRAG_SPEC))
      .matches("block")
      .matches("expr")
      .matches("ident")
      .matches("item")
      .matches("lifetime")
      .matches("literal")
      .matches("meta")
      .matches("path")
      .matches("pat")
      .matches("pat_param")
      .matches("stmt")
      .matches("tt")
      .matches("ty")
      .matches("vis")

    ;
  }

  @Test
  public void testRepOp() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_REP_OP))
      .matches("+")
      .matches("*")
      .matches("?")
      .notMatches("a+")
      .notMatches("+a")
      .notMatches("+a+")

    ;
  }

  @Test
  public void testRepSep() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_REP_SEP))
      .matches("else")

    ;
  }

  @Test
  public void testMacroMatch() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_MATCH))
      .matches("token")
      .matches("$(token token)*")
      .matches("$i:ident")
      .matches("(token)")
      .matches("[token]")
      .matches("{token}")
      .matches("($(token)*)")
      .matches("[$i:ident]")
      .matches("[($i:ident)]")
      .matches("($($i:ident)*)")
      .matches("$($key:expr => $value:expr)+")
      .matches("$($key:expr => $value:expr),+")
      .matches("{ $( $i_tokens:tt )* }")
      .matches("else")
      .matches("if")
      .matches("#")
      .matches("[cfg( $i_meta:meta )]")
      .matches("$(if #[cfg( $i_meta:meta )] { $( $i_tokens:tt )* })+")
      .matches("$(if #[cfg( $i_meta:meta )])+");
  }

  @Test
  public void testMacroMatcher() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_MATCHER))
      .matches("(token)")
      .matches("($(token token)*)")
      .matches("[$(token)*]")
      .matches("{$i:ident}")
      .matches("((token))")
      .matches("[[token]]")
      .matches("{{token}}")
      .matches("(($(token)*))")
      .matches("[[$i:ident]]")
      .matches("{[($i:ident)]}")
      .matches("(($($i:ident)*))")
      .matches("($l:tt)")
      .matches("{ $($key:expr => $value:expr)+ }")
      .matches("{ $($key:expr => $value:expr),+ }")
      .matches("($($f:ident $(< $($generic:ty),* > )? )::+($($arg:ty),*): Send & Sync)")
      .matches("{ $( $e_tokens:tt )* }")
      .matches("(\n" +
        "        $(\n" +
        "            if #[cfg( $i_meta:meta )] { $( $i_tokens:tt )* }\n" +
        "        ) else+\n" +
        "        else { $( $e_tokens:tt )* }\n" +
        "    )")

    ;
  }

  @Test
  public void testMacroRule() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_RULE))
      .matches("($l:tt) => { bar!($l); }")
      .matches("($($name:ident($ty:ty, $to:ident, $lt:lifetime);)*) => {\n" +
        "        $(fn $name(self, v: $ty) -> JsResult<$lt> {\n" +
        "            self.$to(v as _)\n" +
        "        })*\n" +
        "    }")
      .matches("{ $($key:expr => $value:expr),+ } => {\n" +
        "      {\n" +
        "        let mut m = ::std::collections::HashMap::new();\n" +
        "        $(\n" +
        "          m.insert($key, $value);\n" +
        "        )+\n" +
        "        m\n" +
        "      }\n" +
        "    }")
      .matches("(\n" +
        "        $(\n" +
        "            if #[cfg( $i_meta:meta )] { $( $i_tokens:tt )* }\n" +
        "        ) else+\n" +
        "        else { $( $e_tokens:tt )* }\n" +
        "    ) => {\n" +
        "        $crate::cfg_if! {\n" +
        "            @__items () ;\n" +
        "            $(\n" +
        "                (( $i_meta ) ( $( $i_tokens )* )) ,\n" +
        "            )+\n" +
        "            (() ( $( $e_tokens )* )) ,\n" +
        "        }\n" +
        "    }")

    ;
  }

  @Test
  public void testMacroRules() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_RULES))
      .matches("($l:tt) => { bar!($l); }")
      .matches("($($name:ident($ty:ty, $to:ident, $lt:lifetime);)*) => {\n" +
        "        $(fn $name(self, v: $ty) -> JsResult<$lt> {\n" +
        "            self.$to(v as _)\n" +
        "        })*\n" +
        "    };")
      .matches("{ $($key:expr => $value:expr),+ } => {\n" +
        "      {\n" +
        "        let mut m = ::std::collections::HashMap::new();\n" +
        "        $(\n" +
        "          m.insert($key, $value);\n" +
        "        )+\n" +
        "        m\n" +
        "      }\n" +
        "    };")

    ;
  }

  @Test
  public void testMacroRulesDef() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_RULES_DEF))
      .matches("{($l:tt) => { bar!($l); }}")
      .matches("{($ l:tt) => { bar!($ l); }}")
      .matches("{($($name:ident($ty:ty, $to:ident, $lt:lifetime);)*) => {\n" +
        "        $(fn $name(self, v: $ty) -> JsResult<$lt> {\n" +
        "            self.$to(v as _)\n" +
        "        })*\n" +
        "    };\n" +
        "}")
      .matches("(\n" +
        "    { $($key:expr => $value:expr),+ } => {\n" +
        "      {\n" +
        "        let mut m = ::std::collections::HashMap::new();\n" +
        "        $(\n" +
        "          m.insert($key, $value);\n" +
        "        )+\n" +
        "        m\n" +
        "      }\n" +
        "    };\n" +
        "  );")
      .matches("(\n" +
        "    { $ ($key:expr => $ value:expr),+ } => {\n" +
        "      {\n" +
        "        let mut m = ::std::collections::HashMap::new();\n" +
        "        $(\n" +
        "          m.insert($key, $value);\n" +
        "        )+\n" +
        "        m\n" +
        "      }\n" +
        "    };\n" +
        "  );")

    ;
  }

  @Test
  public void testMacroRulesDefinition() {
    assertThat(RustGrammar.create().build().rule(RustGrammar.MACRO_RULES_DEFINITION))
      .matches("macro_rules! foo {\n" +
        "    ($l:tt) => { bar!($l); }\n" +
        "}")
      .matches("macro_rules! pat {\n" +
        "    ($i:ident) => (Some($i))\n" +
        "}")
      .matches("macro_rules! Tuple {\n" +
        "    { $A:ty, $B:ty } => { ($A, $B) };\n" +
        "}")
      .matches("macro_rules! const_maker {\n" +
        "    ($t:ty, $v:tt) => { const CONST: $t = $v; };\n" +
        "}")
      .matches("macro_rules! example {\n" +
        "    () => { println!(\"Macro call in a macro!\") };\n" +
        "}")
      .matches("macro_rules! forward_to {\n" +
        "    ($($name:ident($ty:ty, $to:ident, $lt:lifetime);)*) => {\n" +
        "        $(fn $name(self, v: $ty) -> JsResult<$lt> {\n" +
        "            self.$to(v as _)\n" +
        "        })*\n" +
        "    };\n" +
        "}")
      .matches("macro_rules! map (\n" +
        "    { $($key:expr => $value:expr),+ } => {\n" +
        "      {\n" +
        "        let mut m = ::std::collections::HashMap::new();\n" +
        "        $(\n" +
        "          m.insert($key, $value);\n" +
        "        )+\n" +
        "        m\n" +
        "      }\n" +
        "    };\n" +
        "  );")
      .matches("macro_rules! itest(\n" +
        "  ($name:ident {$( $key:ident: $value:expr,)*})  => {\n" +
        "    #[test]\n" +
        "    fn $name() {\n" +
        "      (util::CheckOutputIntegrationTest {\n" +
        "        $(\n" +
        "          $key: $value,\n" +
        "         )*\n" +
        "        .. Default::default()\n" +
        "      }).run()\n" +
        "    }\n" +
        "  }\n" +
        ");")
      .matches("macro_rules! a {\n" +
        "    () => { a!(1); };\n" +
        "    (1) => { a!(2); };\n" +
        "    (2) => { a!(3); };\n" +
        "    (3) => { a!(4); };\n" +
        "    (4) => { };\n" +
        "}")
      .matches("macro_rules ! a {\n" +
        "    () => { a!(1); };\n" +
        "    (1) => { a!(2); };\n" +
        "    (2) => { a!(3); };\n" +
        "    (3) => { a!(4); };\n" +
        "    (4) => { };\n" +
        "}")
      .notMatches("quote!{k}")
      .matches("macro_rules! _select_user { ($ (($ ($func_arg : ident : $func_arg_ty : ty) , +) =>)? $module_name : ident { $($ field : ident $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ? $ (: $ selection_mode : ident { $ ($ selections : tt) + }) ?) + }) => { # [allow (warnings)] pub mod $ module_name { $ crate :: prisma :: user :: select ! (@ definitions ; $ ($ field $ (($ ($ filters) +) $ (. $ arg ($ ($ arg_params) *)) *) ? $ (: $ selection_mode { $ ($ selections) + }) ?) +) ; pub struct Select (Vec < :: prisma_client_rust :: Selection >) ; impl :: prisma_client_rust :: select :: SelectType for Select { type Data = Data ; type ModelData = $ crate :: prisma :: user :: Data ; fn to_selections (self) -> Vec < :: prisma_client_rust :: Selection > { self . 0 } } use super :: * ; pub fn select ($ ($ ($ func_arg : $ func_arg_ty) , +) ?) -> Select { Select ($ crate :: prisma :: user :: select ! (@ selections_to_select_params ; : select { $ ($ field $ (($ ($ filters) +) $ (. $ arg ($ ($ arg_params) *)) *) ? $ (: $ selection_mode { $ ($ selections) + }) ?) + }) . into_iter () . map (| p | p . to_selection ()) . collect ()) } } } ; ({ $ ($ field : ident $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ? $ (: $ selection_mode : ident { $ ($ selections : tt) + }) ?) + }) => { { $ crate :: prisma :: user :: select ! (@ definitions ; $ ($ field $ (($ ($ filters) +) $ (. $ arg ($ ($ arg_params) *)) *) ? $ (: $ selection_mode { $ ($ selections) + }) ?) +) ; pub struct Select (Vec < :: prisma_client_rust :: Selection >) ; impl :: prisma_client_rust :: select :: SelectType for Select { type Data = Data ; type ModelData = $ crate :: prisma :: user :: Data ; fn to_selections (self) -> Vec < :: prisma_client_rust :: Selection > { self . 0 } } Select ($ crate :: prisma :: user :: select ! (@ selections_to_select_params ; : select { $ ($ field $ (($ ($ filters) +) $ (. $ arg ($ ($ arg_params) *)) *) ? $ (: $ selection_mode { $ ($ selections) + }) ?) + }) . into_iter () . map (| p | p . to_selection ()) . collect ()) } } ; (@ definitions ; $ ($ field : ident $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ? $ (: $ selection_mode : ident { $ ($ selections : tt) + }) ?) +) => { # [allow (warnings)] enum Fields { id , username , email , first_name , last_name , is_active , created , updated , accounts } # [allow (warnings)] impl Fields { fn selections () { $ (let _ = Fields :: $ field ;) + } } # [allow (warnings)] # [derive (std :: fmt :: Debug , Clone)] pub struct Data { $ (pub $ field : $ crate :: prisma :: user :: select ! (@ field_type ; $ field $ (: $ selection_mode { $ ($ selections) + }) ?) ,) + } impl :: serde :: Serialize for Data { fn serialize < S > (& self , serializer : S) -> Result < S :: Ok , S :: Error > where S : :: serde :: Serializer , { use :: serde :: ser :: SerializeStruct ; let mut state = serializer . serialize_struct (\"Data\" , [$ (stringify ! ($ field) ,) +] . len ()) ? ; $ (state . serialize_field ($ crate :: prisma :: user :: select ! (@ field_serde_name ; $ field) , & self . $ field) ? ;) * state . end () } } impl < 'de > :: serde :: Deserialize < 'de > for Data { fn deserialize < D > (deserializer : D) -> Result < Self , D :: Error > where D : :: serde :: Deserializer < 'de > , { # [allow (warnings)] enum Field { $ ($ field) , + , } impl < 'de > :: serde :: Deserialize < 'de > for Field { fn deserialize < D > (deserializer : D) -> Result < Field , D :: Error > where D : :: serde :: Deserializer < 'de > , { struct FieldVisitor ; impl < 'de > :: serde :: de :: Visitor < 'de > for FieldVisitor { type Value = Field ; fn expecting (& self , formatter : & mut :: std :: fmt :: Formatter) -> :: std :: fmt :: Result { formatter . write_str (concat ! ($ ($ crate :: prisma :: user :: select ! (@ field_serde_name ; $ field) , \", \") , +)) } fn visit_str < E > (self , value : & str) -> Result < Field , E > where E : :: serde :: de :: Error , { match value { $ ($ crate :: prisma :: user :: select ! (@ field_serde_name ; $ field) => Ok (Field :: $ field)) , * , _ => Err (:: serde :: de :: Error :: unknown_field (value , FIELDS)) , } } } deserializer . deserialize_identifier (FieldVisitor) } } struct DataVisitor ; impl < 'de > :: serde :: de :: Visitor < 'de > for DataVisitor { type Value = Data ; fn expecting (& self , formatter : & mut std :: fmt :: Formatter) -> std :: fmt :: Result { formatter . write_str (\"struct Data\") } fn visit_map < V > (self , mut map : V) -> Result < Data , V :: Error > where V : :: serde :: de :: MapAccess < 'de > , { $ (let mut $ field = None ;) * while let Some (key) = map . next_key () ? { match key { $ (Field :: $ field => { if $ field . is_some () { return Err (:: serde :: de :: Error :: duplicate_field ($ crate :: prisma :: user :: select ! (@ field_serde_name ; $ field))) ; } $ field = Some (map . next_value () ?) ; }) * } } $ (let $ field = $ field . ok_or_else (|| serde :: de :: Error :: missing_field ($ crate :: prisma :: user :: select ! (@ field_serde_name ; $ field))) ? ;) * Ok (Data { $ ($ field) , * }) } } const FIELDS : & 'static [& 'static str] = & [\"id\" , \"username\" , \"email\" , \"first_name\" , \"last_name\" , \"is_active\" , \"created\" , \"updated\" , \"accounts\"] ; deserializer . deserialize_struct (\"Data\" , FIELDS , DataVisitor) } } $ ($ (pub mod $ field { $ crate :: prisma :: user :: select ! (@ field_module ; $ field : $ selection_mode { $ ($ selections) + }) ; }) ?) + } ; (@ field_type ; id) => { String } ; (@ field_type ; username) => { String } ; (@ field_type ; email) => { String } ; (@ field_type ; first_name) => { String } ; (@ field_type ; last_name) => { String } ; (@ field_type ; is_active) => { bool } ; (@ field_type ; created) => { :: prisma_client_rust :: chrono :: DateTime < :: prisma_client_rust :: chrono :: FixedOffset , > } ; (@ field_type ; updated) => { Option < :: prisma_client_rust :: chrono :: DateTime < :: prisma_client_rust :: chrono :: FixedOffset , > > } ; (@ field_type ; accounts : $ selection_mode : ident { $ ($ selections : tt) + }) => { Vec < accounts :: Data > } ; (@ field_type ; accounts) => { Vec < crate :: prisma :: account :: Data > } ; (@ field_type ; $ field : ident $ ($ tokens : tt) *) => { compile_error ! (stringify ! (Cannot select field nonexistent field $ field on model \"User\" , available fields are \"id, username, email, first_name, last_name, is_active, created, updated, accounts\")) } ; (@ field_module ; accounts : $ selection_mode : ident { $ ($ selections : tt) + }) => { $ crate :: prisma :: account :: select ! (@ definitions ; $ ($ selections) +) ; } ; (@ field_module ; $ ($ tokens : tt) *) => { } ; (@ selection_field_to_selection_param ; id) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: id :: Select) } ; (@ selection_field_to_selection_param ; username) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: username :: Select) } ; (@ selection_field_to_selection_param ; email) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: email :: Select) } ; (@ selection_field_to_selection_param ; first_name) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: first_name :: Select) } ; (@ selection_field_to_selection_param ; last_name) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: last_name :: Select) } ; (@ selection_field_to_selection_param ; is_active) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: is_active :: Select) } ; (@ selection_field_to_selection_param ; created) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: created :: Select) } ; (@ selection_field_to_selection_param ; updated) => { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: updated :: Select) } ; (@ selection_field_to_selection_param ; accounts $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ? : $ selection_mode : ident { $ ($ selections : tt) + }) => { { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: accounts :: Select :: $ selection_mode ($ crate :: prisma :: account :: ManyArgs :: new ($ crate :: prisma :: account :: select ! (@ filters_to_args ; $ ($ ($ filters) +) ?)) $ ($ (. $ arg ($ ($ arg_params) *)) *) ? , $ crate :: prisma :: account :: select ! (@ selections_to_select_params ; : $ selection_mode { $ ($ selections) + }) . into_iter () . collect ())) } } ; (@ selection_field_to_selection_param ; accounts $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ?) => { { Into :: < $ crate :: prisma :: user :: SelectParam > :: into ($ crate :: prisma :: user :: accounts :: Select :: Fetch ($ crate :: prisma :: account :: ManyArgs :: new ($ crate :: prisma :: account :: select ! (@ filters_to_args ; $ ($ ($ filters) +) ?)) $ ($ (. $ arg ($ ($ arg_params) *)) *) ?) ,) } } ; (@ selection_field_to_selection_param ; $ ($ tokens : tt) *) => { compile_error ! (stringify ! ($ ($ tokens) *)) } ; (@ selections_to_select_params ; : $ macro_name : ident { $ ($ field : ident $ (($ ($ filters : tt) +) $ (. $ arg : ident ($ ($ arg_params : tt) *)) *) ? $ (: $ selection_mode : ident { $ ($ selections : tt) + }) ?) + }) => { [$ ($ crate :: prisma :: user :: $ macro_name ! (@ selection_field_to_selection_param ; $ field $ (($ ($ filters) +) $ (. $ arg ($ ($ arg_params) *)) *) ? $ (: $ selection_mode { $ ($ selections) + }) ?) ,) +] } ; (@ filters_to_args ;) => { vec ! [] } ; (@ filters_to_args ; $ ($ t : tt) *) => { $ ($ t) * } ; (@ field_serde_name ; id) => { \"id\" } ; (@ field_serde_name ; username) => { \"username\" } ; (@ field_serde_name ; email) => { \"email\" } ; (@ field_serde_name ; first_name) => { \"first_name\" } ; (@ field_serde_name ; last_name) => { \"last_name\" } ; (@ field_serde_name ; is_active) => { \"is_active\" } ; (@ field_serde_name ; created) => { \"created\" } ; (@ field_serde_name ; updated) => { \"updated\" } ; (@ field_serde_name ; accounts) => { \"accounts\" } ; }")

    ;
  }

}
