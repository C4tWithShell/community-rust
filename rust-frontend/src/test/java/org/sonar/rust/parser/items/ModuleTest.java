/**
 *
 * Sonar Rust Plugin (Community)
 * Copyright (C) 2021 Eric Le Goff
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
package org.sonar.rust.parser.items;

import org.junit.Test;
import org.sonar.rust.RustGrammar;

import static org.sonar.sslr.tests.Assertions.assertThat;

public class ModuleTest {

    @Test
    public void testModule() {
        assertThat(RustGrammar.create().build().rule(RustGrammar.MODULE))
                .matches("mod foo ;")
                .matches("mod bar {}")
                .matches("mod foobar{#![crate_type = \"lib\"]}")
                .matches("mod foobar{#![crate_type = \"lib\"]\n" +
                        "}")
                .matches("mod tests {\n" +
                        "    use super::Identifier;\n" +
                        "    use super::SemVerError;\n" +
                        "    use super::Version;\n" +
                        "    use std::result;\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_parse() {\n" +
                        "        fn parse_error(e: &str) -> result::Result<Version, SemVerError> {\n" +
                        "            return Err(SemVerError::ParseError(e.to_string()));\n" +
                        "        }\n" +
                        "\n" +
                        "        assert_eq!(Version::parse(\"\"), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(Version::parse(\"  \"), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(Version::parse(\"1\"), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(Version::parse(\"1.2\"), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(Version::parse(\"1.2.3-\"), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"a.b.c\"),\n" +
                        "            parse_error(\"encountered unexpected token: AlphaNumeric(\\\"a\\\")\")\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3 abc\"),\n" +
                        "            parse_error(\"expected end of input, but got: [AlphaNumeric(\\\"abc\\\")]\")\n" +
                        "        );\n" +
                        "\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "\n" +
                        "        assert_eq!(Version::parse(\"1.2.3\"), Ok(Version::new(1, 2, 3)));\n" +
                        "\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"  1.2.3  \"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"  1.2.3-alpha1  \"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3+build5\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"  1.2.3+build5  \"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1+build5\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"  1.2.3-alpha1+build5  \"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-1.alpha1.9+build5.7.3aedf  \"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![\n" +
                        "                    Identifier::Numeric(1),\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"alpha1\")),\n" +
                        "                    Identifier::Numeric(9),\n" +
                        "                ],\n" +
                        "                build: vec![\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"build5\")),\n" +
                        "                    Identifier::Numeric(7),\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"3aedf\")),\n" +
                        "                ],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"0.4.0-beta.1+0851523\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 0,\n" +
                        "                minor: 4,\n" +
                        "                patch: 0,\n" +
                        "                pre: vec![\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"beta\")),\n" +
                        "                    Identifier::Numeric(1),\n" +
                        "                ],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"0851523\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        // for https://nodejs.org/dist/index.json, where some older npm versions are \"1.1.0-beta-10\"\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.1.0-beta-10\"),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 1,\n" +
                        "                patch: 0,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"beta-10\")),],\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_increment_patch() {\n" +
                        "        let mut buggy_release = Version::parse(\"0.1.0\").unwrap();\n" +
                        "        buggy_release.increment_patch();\n" +
                        "        assert_eq!(buggy_release, Version::parse(\"0.1.1\").unwrap());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_increment_minor() {\n" +
                        "        let mut feature_release = Version::parse(\"1.4.6\").unwrap();\n" +
                        "        feature_release.increment_minor();\n" +
                        "        assert_eq!(feature_release, Version::parse(\"1.5.0\").unwrap());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_increment_major() {\n" +
                        "        let mut chrome_release = Version::parse(\"46.1.246773\").unwrap();\n" +
                        "        chrome_release.increment_major();\n" +
                        "        assert_eq!(chrome_release, Version::parse(\"47.0.0\").unwrap());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_increment_keep_prerelease() {\n" +
                        "        let mut release = Version::parse(\"1.0.0-alpha\").unwrap();\n" +
                        "        release.increment_patch();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"1.0.1\").unwrap());\n" +
                        "\n" +
                        "        release.increment_minor();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"1.1.0\").unwrap());\n" +
                        "\n" +
                        "        release.increment_major();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"2.0.0\").unwrap());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_increment_clear_metadata() {\n" +
                        "        let mut release = Version::parse(\"1.0.0+4442\").unwrap();\n" +
                        "        release.increment_patch();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"1.0.1\").unwrap());\n" +
                        "        release = Version::parse(\"1.0.1+hello\").unwrap();\n" +
                        "\n" +
                        "        release.increment_minor();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"1.1.0\").unwrap());\n" +
                        "        release = Version::parse(\"1.1.3747+hello\").unwrap();\n" +
                        "\n" +
                        "        release.increment_major();\n" +
                        "\n" +
                        "        assert_eq!(release, Version::parse(\"2.0.0\").unwrap());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_eq() {\n" +
                        "        assert_eq!(Version::parse(\"1.2.3\"), Version::parse(\"1.2.3\"));\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1\"),\n" +
                        "            Version::parse(\"1.2.3-alpha1\")\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3+build.42\"),\n" +
                        "            Version::parse(\"1.2.3+build.42\")\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1+42\"),\n" +
                        "            Version::parse(\"1.2.3-alpha1+42\")\n" +
                        "        );\n" +
                        "        assert_eq!(Version::parse(\"1.2.3+23\"), Version::parse(\"1.2.3+42\"));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_ne() {\n" +
                        "        assert!(Version::parse(\"0.0.0\") != Version::parse(\"0.0.1\"));\n" +
                        "        assert!(Version::parse(\"0.0.0\") != Version::parse(\"0.1.0\"));\n" +
                        "        assert!(Version::parse(\"0.0.0\") != Version::parse(\"1.0.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha\") != Version::parse(\"1.2.3-beta\"));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_show() {\n" +
                        "        assert_eq!(\n" +
                        "            format!(\"{}\", Version::parse(\"1.2.3\").unwrap()),\n" +
                        "            \"1.2.3\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            format!(\"{}\", Version::parse(\"1.2.3-alpha1\").unwrap()),\n" +
                        "            \"1.2.3-alpha1\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            format!(\"{}\", Version::parse(\"1.2.3+build.42\").unwrap()),\n" +
                        "            \"1.2.3+build.42\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            format!(\"{}\", Version::parse(\"1.2.3-alpha1+42\").unwrap()),\n" +
                        "            \"1.2.3-alpha1+42\".to_string()\n" +
                        "        );\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_display() {\n" +
                        "        let version = Version::parse(\"1.2.3-rc1\").unwrap();\n" +
                        "        assert_eq!(format!(\"{:20}\", version), \"1.2.3-rc1           \");\n" +
                        "        assert_eq!(format!(\"{:*^20}\", version), \"*****1.2.3-rc1******\");\n" +
                        "        assert_eq!(format!(\"{:.4}\", version), \"1.2.\");\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_to_string() {\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3\").unwrap().to_string(),\n" +
                        "            \"1.2.3\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1\").unwrap().to_string(),\n" +
                        "            \"1.2.3-alpha1\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3+build.42\").unwrap().to_string(),\n" +
                        "            \"1.2.3+build.42\".to_string()\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            Version::parse(\"1.2.3-alpha1+42\").unwrap().to_string(),\n" +
                        "            \"1.2.3-alpha1+42\".to_string()\n" +
                        "        );\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_lt() {\n" +
                        "        assert!(Version::parse(\"0.0.0\") < Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.0.0\") < Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.0\") < Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha1\") < Version::parse(\"1.2.3\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha1\") < Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(!(Version::parse(\"1.2.3-alpha2\") < Version::parse(\"1.2.3-alpha2\")));\n" +
                        "        assert!(!(Version::parse(\"1.2.3+23\") < Version::parse(\"1.2.3+42\")));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_le() {\n" +
                        "        assert!(Version::parse(\"0.0.0\") <= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.0.0\") <= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.0\") <= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha1\") <= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") <= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.3+23\") <= Version::parse(\"1.2.3+42\"));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_gt() {\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") > Version::parse(\"0.0.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") > Version::parse(\"1.0.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") > Version::parse(\"1.2.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") > Version::parse(\"1.2.3-alpha1\"));\n" +
                        "        assert!(Version::parse(\"1.2.3\") > Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(!(Version::parse(\"1.2.3-alpha2\") > Version::parse(\"1.2.3-alpha2\")));\n" +
                        "        assert!(!(Version::parse(\"1.2.3+23\") > Version::parse(\"1.2.3+42\")));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_ge() {\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") >= Version::parse(\"0.0.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") >= Version::parse(\"1.0.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") >= Version::parse(\"1.2.0\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") >= Version::parse(\"1.2.3-alpha1\"));\n" +
                        "        assert!(Version::parse(\"1.2.3-alpha2\") >= Version::parse(\"1.2.3-alpha2\"));\n" +
                        "        assert!(Version::parse(\"1.2.3+23\") >= Version::parse(\"1.2.3+42\"));\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_prerelease_check() {\n" +
                        "        assert!(Version::parse(\"1.0.0\").unwrap().is_prerelease() == false);\n" +
                        "        assert!(Version::parse(\"0.0.1\").unwrap().is_prerelease() == false);\n" +
                        "        assert!(Version::parse(\"4.1.4-alpha\").unwrap().is_prerelease());\n" +
                        "        assert!(Version::parse(\"1.0.0-beta294296\").unwrap().is_prerelease());\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_spec_order() {\n" +
                        "        let vs = [\n" +
                        "            \"1.0.0-alpha\",\n" +
                        "            \"1.0.0-alpha.1\",\n" +
                        "            \"1.0.0-alpha.beta\",\n" +
                        "            \"1.0.0-beta\",\n" +
                        "            \"1.0.0-beta.2\",\n" +
                        "            \"1.0.0-beta.11\",\n" +
                        "            \"1.0.0-rc.1\",\n" +
                        "            \"1.0.0\",\n" +
                        "        ];\n" +
                        "        let mut i = 1;\n" +
                        "        while i < vs.len() {\n" +
                        "            let a = Version::parse(vs[i - 1]);\n" +
                        "            let b = Version::parse(vs[i]);\n" +
                        "            assert!(a < b, \"nope {:?} < {:?}\", a, b);\n" +
                        "            i += 1;\n" +
                        "        }\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_from_str() {\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3\".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"  1.2.3  \".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3-alpha1\".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"  1.2.3-alpha1  \".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: Vec::new(),\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3+build5\".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"  1.2.3+build5  \".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: Vec::new(),\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3-alpha1+build5\".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"  1.2.3-alpha1+build5  \".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![Identifier::AlphaNumeric(String::from(\"alpha1\"))],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"build5\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3-1.alpha1.9+build5.7.3aedf  \".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 1,\n" +
                        "                minor: 2,\n" +
                        "                patch: 3,\n" +
                        "                pre: vec![\n" +
                        "                    Identifier::Numeric(1),\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"alpha1\")),\n" +
                        "                    Identifier::Numeric(9),\n" +
                        "                ],\n" +
                        "                build: vec![\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"build5\")),\n" +
                        "                    Identifier::Numeric(7),\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"3aedf\")),\n" +
                        "                ],\n" +
                        "            })\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"0.4.0-beta.1+0851523\".parse(),\n" +
                        "            Ok(Version {\n" +
                        "                major: 0,\n" +
                        "                minor: 4,\n" +
                        "                patch: 0,\n" +
                        "                pre: vec![\n" +
                        "                    Identifier::AlphaNumeric(String::from(\"beta\")),\n" +
                        "                    Identifier::Numeric(1),\n" +
                        "                ],\n" +
                        "                build: vec![Identifier::AlphaNumeric(String::from(\"0851523\"))],\n" +
                        "            })\n" +
                        "        );\n" +
                        "    }\n" +
                        "\n" +
                        "    #[test]\n" +
                        "    fn test_from_str_errors() {\n" +
                        "        fn parse_error(e: &str) -> result::Result<Version, SemVerError> {\n" +
                        "            return Err(SemVerError::ParseError(e.to_string()));\n" +
                        "        }\n" +
                        "\n" +
                        "        assert_eq!(\"\".parse(), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\"  \".parse(), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\"1\".parse(), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\"1.2\".parse(), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\"1.2.3-\".parse(), parse_error(\"expected more input\"));\n" +
                        "        assert_eq!(\n" +
                        "            \"a.b.c\".parse(),\n" +
                        "            parse_error(\"encountered unexpected token: AlphaNumeric(\\\"a\\\")\")\n" +
                        "        );\n" +
                        "        assert_eq!(\n" +
                        "            \"1.2.3 abc\".parse(),\n" +
                        "            parse_error(\"expected end of input, but got: [AlphaNumeric(\\\"abc\\\")]\")\n" +
                        "        );\n" +
                        "    }\n" +
                        "}")

        ;

    }
}