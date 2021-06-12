##FAQ


* ### How do I build the plugin from source

This project is built with Maven
Use : `mvn clean package`


* ### Why can't I find the plugin listed in the SonarQube Marketplace ?

It is not listed yet on the MarketPlace
It may finds its way some day when some minimal feedback of its usage proves it working as expected

* ### Can I use this plugin on SonarCloud ?

SonarCloud is a little different to SonarQube in that it doesn’t currently provide the ability to add third-party plugins like `Sonar rust`.

* ### Can I use this plugin on SonarLint ?

Currently `Sonar Rust` is not supported by SonarLint.

* ### Do I need a SonarQube license to use this plugin ?

You don't : the (free) Community edition v7.9+ is compatible
(and it also works on Commercial editions of SonarQube)

* ### Can I scan my Rust source code within a  CI/CD pipeline ?

There is nothing specific for this plugin. Any CI pipeline which allows a SonarQube analysis can trigger an analysis on a Rust project
The only requirement is that the `Sonar Rust` plugin is installed on your SonarQube instance

* ### How many coding rules / Quality Profiles does this Sonar Rust plugin  provide ?

No Rust rule or Quality profile are defined by this plugin. The plugin only allows to import issues that Clippy has found

* ### My project is a mix of Rust and other languages, will this work ?

Yes, the issues on other languages than Rust will be detected by their respective language analyzer

* ### How do I reach out for issues, feature requests or questions not listed here ?

Issues / Feature requests specific to this plugin can be tracked after you [create a Github issue](https://docs.github.com/en/issues/tracking-your-work-with-issues/creating-issues/creating-an-issue)
Issues that are related to SonarQube behaviour should be reported in their [Community forum](https://community.sonarsource.com/)

* ### How can I contribute to improving/fixing this plugin ?

We would love to have more contributors. 
Please read this nice [article](https://gist.github.com/MarcDiethelm/7303312) to get ideas how you can help






