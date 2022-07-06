use proc_macro::TokenStream;
use quote::{format_ident, quote};
use std::collections::HashMap;
use syn::{parse_macro_input, AttributeArgs, DeriveInput, Lit, Meta, NestedMeta};

const ON_MONTH_IN_SECONDS: u64 = 2663280;

fn parse_invocation(attr: Vec<NestedMeta>, input: DeriveInput) -> TokenStream {
    let mut hashmap: HashMap<String, Lit> = HashMap::new();

    for attr_iter in attr.into_iter() {
        match attr_iter {
            NestedMeta::Lit(lit) => {
                hashmap.insert(String::from("secret"), lit);
            }
            NestedMeta::Meta(meta) => {
                if let Meta::NameValue(namevalue) = meta {
                    let name = namevalue.path;
                    let value = namevalue.lit;
                    let name = name.segments[0].ident.to_string();
                    hashmap.insert(name, value);
                }
            }
        };
    }

    // handle input
    let guard_type = &input.ident;
    let vis = &input.vis;
    let fairing_name = format!("'{}' JwtFairing", "Test");
    let guard_claim = format_ident!("{}JwtClaim", &guard_type);

    let guard_types = quote! {
        use ::serde::{Deserialize, Serialize};

        #[derive(Debug, Deserialize, Serialize)]
        #input

        #[derive(Debug, Deserialize, Serialize)]
        #vis struct #guard_claim {
            exp: u64,
            iat: u64,
            user: #guard_type
        }
    };

    let secret = match hashmap.get("secret") {
        Some(secret) => secret,
        _ => panic!("JWT secret should not be empty"),
    };

    let exp = match hashmap.get("exp") {
        Some(exp_lit) => {
            if let Lit::Int(exp_lit_int) = exp_lit {
                exp_lit_int.base10_digits().parse::<u64>().unwrap()
            } else {
                ON_MONTH_IN_SECONDS
            }
        }
        None => ON_MONTH_IN_SECONDS,
    };

    quote! {
        use ::jsonwebtoken::errors::{Result, ErrorKind, Error};
        use ::jsonwebtoken::TokenData;
        use ::jsonwebtoken::{encode, Header, EncodingKey};
        use ::jsonwebtoken::{decode, Validation, DecodingKey};

        use ::rocket::http::Status;
        use ::rocket::outcome::Outcome;
        use ::rocket::request::{self, FromRequest, Request};
        use ::rocket::response::status;

        #guard_types

        impl #guard_type {
            pub fn fairing() -> impl ::rocket::fairing::Fairing {
                ::rocket::fairing::AdHoc::on_attach(#fairing_name, |rocket| {
                    Ok(rocket)
                })
            }

            pub fn sign(user: #guard_type) -> String {
                use ::std::time::{SystemTime, UNIX_EPOCH};
                let now = SystemTime::now().duration_since(UNIX_EPOCH).unwrap().as_secs();
                let payload = #guard_claim {
                    exp: #exp + now,
                    iat: now,
                    user,
                };

                encode(&Header::default(), &payload, &EncodingKey::from_secret((#secret).as_bytes())).unwrap()
            }

            pub fn decode(token: String) -> Result<#guard_claim> {
                let result = decode::<#guard_claim>(&token, &DecodingKey::from_secret((#secret).as_bytes()), &Validation::default());
                if let Ok(token_claim) = result {
                    return Ok(token_claim.claims);
                }
                Err(Error::from(ErrorKind::InvalidToken))
            }
        }

        impl<'a, 'r> FromRequest<'a, 'r> for #guard_type {
            type Error = status::Custom<String>;
            fn from_request(
                request: &'a Request<'r>,
            ) -> request::Outcome<Self, status::Custom<String>> {
                if let Some(auth_header) = request.headers().get_one("Authorization") {
                    let auth_str = auth_header.to_string();
                    if auth_str.starts_with("Bearer") {
                        let token = auth_str[6..auth_str.len()].trim();
                        if let Ok(token_data) = #guard_type::decode(token.to_string()) {
                            return Outcome::Success(token_data.user);
                        }
                    }
                }

                Outcome::Failure((
                    Status::Unauthorized,
                    status::Custom(
                        Status::Unauthorized,
                        String::from("401 Unauthorized"),
                    ),
                ))
            }
        }
    }.into()
}

#[proc_macro_attribute]
pub fn jwt(attr: TokenStream, input: TokenStream) -> TokenStream {
    let input = parse_macro_input!(input as DeriveInput);
    let attr = parse_macro_input!(attr as AttributeArgs);

    parse_invocation(attr, input)
}
