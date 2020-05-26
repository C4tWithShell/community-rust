Sonar-rust plugin
==================

This plugin allows to visualize clippy lints report from the SonarQube UI
It is compatible with SonarQube 7.9+

Build plugin
-------------
`mvn clean package`

* Copy the jar in your SonarQube server

`cp target/sonar-rust-plugin-1.0.0.jar [sonarqube_install_folder]/extensions/plugins/`

Please note this plugin can only be installed manually until it 
is available on the SonarQube Marketplace

* After plugin is copied, restart your SonarQube server

Generate clippy report file:
------------------------
`cargo clippy --message-format=json &> <clippy report file>`

Analysis parameter
--------------------
Add parameter `sonar.rust.clippy.reportPaths=<clippy report file>`