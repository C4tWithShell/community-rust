#[derive(Debug, Deserialize, Serialize, Getter)]
#[serde(deny_unknown_fields)]
pub struct Message<'a> {
    #[serde(rename = "c")]
    command: Command,
    #[serde(borrow)]
    payload: Option<Cow<'a, str>>,
    #[serde(rename = "s")]
    identifier: Option<u16>,
}