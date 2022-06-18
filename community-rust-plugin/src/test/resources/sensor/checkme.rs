for param in  generics.params {
match param {
syn::GenericParam::Lifetime(param) => {
param.bounds.push(place_lifetime.lifetime.clone());
}
syn::GenericParam::Type(param) => {
param.bounds.push(syn::TypeParamBound::Lifetime(
place_lifetime.lifetime.clone(),
));
}
syn::GenericParam::Const(_) => {}
}
}
