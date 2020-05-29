<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->
Sonar-rust plugin
==================

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)](http://www.apache.org/licenses/LICENSE-2.0)
[![SonarCloud](https://sonarcloud.io/images/project_badges/sonarcloud-orange.svg)](https://sonarcloud.io/dashboard?id=elegoff_sonar-rust)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=elegoff_sonar-rust&metric=alert_status)](https://sonarcloud.io/dashboard?id=elegoff_sonar-rust)
This plugin allows to visualize clippy lints report from the SonarQube UI
It is compatible with SonarQube 7.9+

Clippy lints are listed : https://rust-lang.github.io/rust-clippy/master/

## Build plugin

`mvn clean package`

* Copy the jar in your SonarQube server

`cp target/sonar-rust-plugin-0.0.1.jar [sonarqube_install_folder]/extensions/plugins/`

Please note this plugin can only be installed manually until it 
is available on the SonarQube Marketplace

* After plugin is copied, restart your SonarQube server

## Generate clippy report file:

`cargo clippy --message-format=json &> <clippy report file>`

## Analysis parameter

Add parameter `sonar.rust.clippy.reportPaths=<clippy report file>`

## Analyze this plugin with SonarQube

`mvn -Pcoverage clean install sonar:sonar`