|     |     |     
| --- | --- | 
|  [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=elegoff_sonar-rust&metric=alert_status)](https://sonarcloud.io/dashboard?id=elegoff_sonar-rust) | ![Coverage](https://sonarcloud.io/api/project_badges/measure?project=elegoff_sonar-rust&metric=coverage) |
| [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)|[Download latest release](https://github.com/elegoff/sonar-rust/releases) |

## SonarQube Rust plugin (Community)

[SonarQube](https://www.sonarqube.org) is an open platform to manage code quality. This plugin
adds Rust support to SonarQube with the focus on integration of existing Rust tools.

This plugin is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later version.

This plugin allows visualizing [Clippy lints](https://rust-lang.github.io/rust-clippy/master/) reports from the SonarQube UI
It is compatible with SonarQube 7.9+


## Installation

Just [download the latest plugin JAR file](https://github.com/elegoff/sonar-rust/releases) and copy it to the `extensions/plugins` directory of SonarQube and restart.

## Build the plugin from source

`mvn clean package`

## Generating clippy report files :

`cargo clippy --message-format=json &> <clippy report file>`

## Analysis parameter

Add SonarQube analysis parameter `sonar.rust.clippy.reportPaths=<clippy report file>`

