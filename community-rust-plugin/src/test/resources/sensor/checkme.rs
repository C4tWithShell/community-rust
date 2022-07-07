use proc_macro::{Span, TokenStream};
use quote::{format_ident, quote};
use std::collections::HashMap;
use syn::{
    parse_macro_input, AttributeArgs, DeriveInput, Expr, ExprLit, ExprPath, Lit, LitStr, Meta,
    NestedMeta,
};

const ONE_MONTH_IN_SECONDS: u64 = 2592_000;
const ONE_MINUTE_IN_SECONDS: u64 = 60;

fn get_lit_int(lit: Option<&Lit>, default_value: u64) -> u64 {
    match lit {
        Some(exp_lit) => {
            if let Lit::Int(exp_lit_int) = exp_lit {
                exp_lit_int.base10_digits().parse::<u64>().unwrap()
            } else {
                default_value
            }
        }
        None => default_value,
    }
}

fn get_lit_str(lit: Option<&Lit>, default_value: String) -> String {
    match lit {
        Some(exp_lit) => {
            if let Lit::Str(exp_lit_str) = exp_lit {
                exp_lit_str.value()
            } else {
                default_value
            }
        }
        None => default_value,
    }
}

fn parse_invocation(attr: Vec<NestedMeta>, input: DeriveInput) -> TokenStream {
    let mut attr_into_iter = attr.into_iter();

    // get secret
    let secret = attr_into_iter.next();
    let mut secrete_value: Expr = Expr::Lit(ExprLit {
        attrs: Vec::new(),
        lit: Lit::Str(LitStr::new("", Span::call_site().into())),
    });

    if let Some(secret) = secret {
        match secret {
            NestedMeta::Lit(lit) => {
                if let Lit::Str(lit_str) = lit {
                    secrete_value = Expr::Lit(ExprLit {
                        attrs: Vec::new(),
                        lit: Lit::Str(lit_str),
                    });
                }
            }
            NestedMeta::Meta(meta) => {
                if let Meta::Path(secret_path) = meta {
                    secrete_value = Expr::Path(ExprPath {
                        attrs: Vec::new(),
                        qself: None,
                        path: secret_path,
                    })
                }
            }
        }
    }

    let mut hashmap: HashMap<String, Lit> = HashMap::new();
    for attr_iter in attr_into_iter.into_iter() {
        if let NestedMeta::Meta(meta) = attr_iter {
            if let Meta::NameValue(namevalue) = meta {
                let name = namevalue.path;
                let value = namevalue.lit;
                let name = name.segments[0].ident.to_string();
                hashmap.insert(name, value);
            }
        }
    }

    let exp = get_lit_int(hashmap.get("exp"), ONE_MONTH_IN_SECONDS);
    let leeway = get_lit_int(hashmap.get("leeway"), ONE_MINUTE_IN_SECONDS);
    let cookie_key = get_lit_str(hashmap.get("cookie"), "".to_string());
    let query_key = get_lit_str(hashmap.get("query"), "".to_string());

    // handle input
    let guard_type = &input.ident;
    let vis = &input.vis;
    let fairing_name = format!("'{}' JwtFairing", &guard_type.to_string());
    let guard_claim = format_ident!("{}JwtClaim", &guard_type);

    let jwt = quote!(::jsonwebtoken);
    #[allow(non_snake_case)]
    let Result = quote!(::jsonwebtoken::errors::Result);
    #[allow(non_snake_case)]
    let Status = quote!(::rocket::http::Status);
    #[allow(non_snake_case)]
    let Outcome = quote!(::rocket::outcome::Outcome);
    let request = quote!(::rocket::request);
    let response = quote!(::rocket::response);
    let std_time = quote!(::std::time);
    let serder = quote!(::serde);

    let async_trait = quote!(#[::rocket::async_trait]);

    let guard_types = quote! {
        #[derive(Debug, #serder::Deserialize, #serder::Serialize)]
        #input

        #[derive(Debug, #serder::Deserialize,#serder::Serialize)]
        #vis struct #guard_claim {
            exp: u64,
            iat: u64,
            user: #guard_type
        }
    };

    quote! {
        #guard_types

        impl #guard_type {
            pub fn fairing() -> impl ::rocket::fairing::Fairing {
                ::rocket::fairing::AdHoc::on_ignite(#fairing_name, |rocket| async {
                    rocket
                })
            }

            pub fn sign(user: #guard_type) -> String {
                let now = #std_time::SystemTime::now().duration_since(#std_time::UNIX_EPOCH).unwrap().as_secs();
                let payload = #guard_claim {
                    exp: #exp + now,
                    iat: now,
                    user,
                };

                #jwt::encode(&#jwt::Header::default(), &payload, &#jwt::EncodingKey::from_secret((#secrete_value).as_bytes())).unwrap()
            }

            pub fn decode(token: String) -> #Result<#guard_claim> {
                let mut validation = #jwt::Validation::default();
                validation.leeway = #leeway;

                let result = #jwt::decode::<#guard_claim>(&token, &#jwt::DecodingKey::from_secret((#secrete_value).as_bytes()), &validation);
                match result {
                    Ok(token_claim) => Ok(token_claim.claims),
                    Err(err) => Err(err),
                }
            }
        }

        #async_trait
        impl<'r> #request::FromRequest<'r> for #guard_type {
            type Error = #response::status::Custom<String>;
            // type Error = ();

            async fn from_request(request: &'r #request::Request<'_>,) -> #request::Outcome<Self, #response::status::Custom<String>> {
                let mut auth_str: Option<String> = None;
                if (#cookie_key) != "" {
                    auth_str = match request.cookies().get(#cookie_key) {
                        None => None,
                        Some(t) => Some(t.value().to_string()),
                    };
                } else if (#query_key) != "" {
                    auth_str = match request.query_value::<String>(#query_key) {
                        None => None,
                        Some(t) => match t {
                            Ok(r) => Some(r),
                            Err(_) => None,
                        }
                    }
                } else {
                    auth_str = match auth_str {
                        Some(auth_str) => Some(auth_str),
                        None => match request.headers().get_one("Authorization") {
                            Some(s) => Some(s.to_string()),
                            None => None,
                        }
                    };
                };

                if let Some(auth_str) = auth_str {
                    if auth_str.starts_with("Bearer") {
                        let token = auth_str[6..auth_str.len()].trim();
                        match #guard_type::decode(token.to_string()) {
                            Ok(token_data) => {
                                return #Outcome::Success(token_data.user);
                            },
                            Err(err) => {
                                return #Outcome::Failure((
                                    #Status::Unauthorized,
                                    #response::status::Custom(
                                        #Status::Unauthorized,
                                        err.to_string(),
                                    ),
                                ));
                            },
                            // Err(_) => {
                            //     return #Outcome::Forward(());
                            // },
                        }
                    }
                }

                // #Outcome::Forward(())
                #Outcome::Failure((
                    #Status::Unauthorized,
                    #response::status::Custom(
                        #Status::Unauthorized,
                        String::from("EmptySignature"),
                    ),
                ))
            }
        }
    }.into()
}

///
/// Attribute to generate a [`jsonwebtoken claim`] and associated metadata.
///
/// ```rust
/// // expire default in 2592_000s
/// [jwt("secret")]
/// struct User { id: String }
/// ```
///
/// or
///
/// ```rust
/// // expire in 10s
/// [jwt("secret", exp = 10)]
/// struct User { id: String }
/// ```
///
/// ## Example
/// ---
/// ```rust
/// #![feature(proc_macro_hygiene, decl_macro)]

/// #[macro_use]
/// extern crate rocket;
///
/// use rocket_jwt::jwt;
///
/// static SECRET_KEY: &str = "secret_key";
///
/// #[jwt(SECRET_KEY)]
/// pub struct UserClaim {
///     id: String,
/// }
///
/// #[get("/")]
/// fn index() -> String {
///     let user_claim = UserClaim {
///         id: format!("hello_rocket_jwt"),
///     };
///     let token = UserClaim::sign(user_claim);
///     println!("{:#?}", UserClaim::decode(token.clone()));
///     token
/// }
///
/// #[get("/user_id")]
/// fn get_uer_id_from_jwt(user: UserClaim) -> String {
///     format!("user id is {}", user.id)
/// }
///
/// fn main() {
///     rocket::ignite()
///         .attach(UserClaim::fairing())
///         .mount("/", routes![index, get_uer_id_from_jwt])
///         .launch();
/// }
/// ```
/// token default comes from request.header, if want get from cookie or query, user
///
/// ```rust
/// #[jwt("secret", cookie = "token")]
/// pub struct UserClaim {
///     id: String,
/// }
/// ```
///
/// /// ```rust
/// #[jwt("secret", query = "token")]
/// pub struct UserClaim {
///     id: String,
/// }
/// ```
#[proc_macro_attribute]
pub fn jwt(attr: TokenStream, input: TokenStream) -> TokenStream {
    let input = parse_macro_input!(input as DeriveInput);
    let attr = parse_macro_input!(attr as AttributeArgs);

    parse_invocation(attr, input)
}
