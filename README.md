
|                                                                                                                                                                           |                                                                                                          |     
|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------|----------------------------------------------------------------------------------------------------------| 
| [![Quality Gate](https://sonarcloud.io/api/project_badges/measure?project=elegoff_sonar-rust&metric=alert_status)](https://sonarcloud.io/dashboard?id=elegoff_sonar-rust) | ![Coverage](https://sonarcloud.io/api/project_badges/measure?project=elegoff_sonar-rust&metric=coverage) |
| [![License: GPL v3](https://img.shields.io/badge/License-GPLv3-blue.svg)](https://www.gnu.org/licenses/gpl-3.0)                                                           | [Download latest release](https://github.com/elegoff/sonar-rust/releases)                                |

## SonarQube plugin for Rust (Community)

The plugin enables analysis of Rust language within [SonarQube](https://www.sonarqube.org), which is an open platform to
manage code quality.

### Compatibility with SonarQube versions :

| SonarQube   | Community Rust plugin |
|-------------|-----------------------|
| 10.4+       | 0.2.3                 |
| 10.0 - 10.3 | 0.2.2                 | 
| 9.9 LTS     | 0.2.1                 |
| 8.9 - 9.9   | 0.1.0                 |
| <8.9        | not supported         |

It leverages [Clippy lints](https://rust-lang.github.io/rust-clippy/master/) to raise issues against coding
rules,  [LCOV](https://wiki.documentfoundation.org/Development/Lcov)
or [Cobertura](http://cobertura.github.io/cobertura/) for code coverage.

### How ?

#### tl;dr

* Generate a Clippy report

`cargo clippy --message-format=json &> <CLIPPY REPORT FILE>`

* Import it into SonarQube

set analysis parameter `community.rust.clippy.reportPaths=<CLIPPY REPORT FILE>`

* Optionally import tests measures (`junit` report)

use `community.rust.test.reportPath`

* Optionally import coverage measures

use either

`community.rust.lcov.reportPaths`

or

`community.rust.cobertura.reportPaths`

For more details, you may want to read :

* The [documentation](./DOC.md)
* The [FAQ page](./FAQ.md)

***
*This plugin is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
License as published by the Free Software Foundation; either version 3 of the License, or (at your option) any later
version.*

Your contribution and/or user feedback is welcomed

*Contact :* <community-rust@pm.me>

### Thanks to Eric, the original creater of community-rust plugin

Dear Community Rust plugin users,

I hope this message finds you well. I am writing to you today with an important announcement regarding the future of our beloved project.

After much consideration and reflection, I have made the difficult decision to step down as the maintainer of that Rust plugin for SonarQube. This decision has not been easy, but recent changes in my personal and professional life have made it increasingly challenging for me to dedicate the time and energy necessary to effectively maintain the project.

I want to take this opportunity to express my deepest gratitude to each and every one of you who have supported and contributed to the project over the years.

While I will no longer be actively maintaining the project, I am committed to ensuring that it continues to thrive in the hands of those who are passionate about its mission and goals. As such, I am open to transferring ownership of the project to someone who is willing to take on the responsibilities of maintaining it. If you are interested in becoming the new maintainer of tjis Sonar plugin or have any questions about the transition process, please don't hesitate to reach out to me directly.

In the coming weeks, I will work on facilitating a smooth transition process for the new maintainer(s). I will provide guidance on how to proceed with the transfer of ownership and offer support wherever needed.

Once again, thank you all for your incredible support and dedication to [Project Name]. It has been an honor and a privilege to serve as the project's maintainer, and I look forward to seeing it continue to grow and thrive in the future.

Best regards,

Eric

