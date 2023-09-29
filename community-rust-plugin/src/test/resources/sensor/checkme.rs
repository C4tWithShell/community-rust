#[derive(Encode, Decode, Eq, PartialEq, Clone, PartialOrd, Ord, Debug, scale_info::TypeInfo)]
 #[cfg_attr(feature = "std", derive(Serialize, Deserialize))]
 #[repr(u8)]
 pub enum TechPurpose<AssetId> {
     FeeCollector = 0,
     FeeCollectorForPairTradPair(foo),
     }
