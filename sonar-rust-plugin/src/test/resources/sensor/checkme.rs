match token {
Token::BackQuote => in_template = !in_template,
Token::RBrace => {
match (stack.pop(), token) {
Token::RParen => {}
(Some(left), _) => {
return 42
}
(None, _) => {
// While technically invalid when unpaired, it should be V8's task to output error instead.
// Thus marked as valid with no info.
return Ok(ValidationResult::Valid(None));
}
}
}
_ => {}
}