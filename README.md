#![Cloudogu logo](https://github.com/cloudogu/website/blob/master/images/logo.png)
# Integrations-Tests
https://cloudogu.com

This repository consists of integration-tests for the cloudogu ecosystem setup via setup.json. The tests are implemented in Java inside a Maven project which uses Selenium and Gauge as plugins for testing using automated browser input.

### Quick start
* Prerequisites:
 - Install [maven](https://maven.apache.org/download.cgi)
 - Install [gauge](http://getgauge.io/get-started)
 - Install [chromedriver](https://sites.google.com/a/chromium.org/chromedriver/downloads)
 - Make sure Google Chrome is installed
 - Make sure repository cloudogu/ecosystem is cloned
* Clone repository cloudogu/integration-tests
* Start ecosystem via `vagrant up`
* Make sure there is an admin rights user `admin` with password `adminpw`
* Go into your integration-tests repository
* If your ecosystem url is `https://192.168.115.112` do one of the following:
  * Set `gauge_jvm_args=-Deco.system=${url}` in file `env/default/java.properties` for persistence
  * Or type `gauge_jvm_args=-Deco.system=${url} mvn test` without saving the url
* `mvn test` will start the test phase

### Development
* Use [NetBeans](https://netbeans.org/downloads/index.html) or similiar IDEs to open integration-tests project
* Feel free to create additional spec files inside `specs/` directory using gauge markdown
* [gauge documentation](http://getgauge.io/documentation/user/current/) should help you to implement quickly
* [Selenium](http://www.seleniumhq.org/docs/index.jsp) cares for browser automation
* Take a look at the two driver classes written in Java which instantiate a WebDriver
* `@BeforeSuite` hooked driver method will be executed before testing single Steps
* There are three types of Java-classes in the source directory
* Page classes to locate WebElements of certain Websites (E.g. Redmine)
* API classes to access REST APIs via client
* EcoSystem class consisting of globally used methods
* Implement steps writing Java classes in the test directory with `@Step` annotation
* A Step like `* Login as "admin" with password "adminpw"` must be implemented as:
```java
@Step("Login as <user> with password <password>")
public void login(String user, String password){
  // some test code injecting user as "admin" and password as "adminpw"
}
```
* Now feel free to implement your own Steps

---
### What is Cloudogu?
Cloudogu is an open platform, which lets you choose how and where your team creates great software. Each service or tool is delivered as a [Dōgu](https://translate.google.com/?text=D%26%23x014d%3Bgu#ja/en/%E9%81%93%E5%85%B7), a Docker container, that can be easily integrated in your environment just by pulling it from our registry. We have a growing number of ready-to-use Dōgus, e.g. SCM-Manager, Jenkins, Nexus, SonarQube, Redmine and many more. Every Dōgu can be tailored to your specific needs. You can even bring along your own Dōgus! Take advantage of a central authentication service, a dynamic navigation, that lets you easily switch between the web UIs and a smart configuration magic, which automatically detects and responds to dependencies between Dōgus. Cloudogu is open source and it runs either on-premise or in the cloud. Cloudogu is developed by Cloudogu GmbH under [MIT License](https://github.com/cloudogu/website/blob/master/LICENSE.md) and it runs either on-premise or in the cloud.

### How to get in touch?
Want to talk to the Cloudogu team? Need help or support? There are several ways to get in touch with us:

* [Website](https://cloudogu.com)
* [Mailing list](https://groups.google.com/forum/#!forum/cloudogu)
* [Email hello@cloudogu.com](mailto:hello@cloudogu.com)

---
&copy; 2016 Cloudogu GmbH - MADE WITH :heart: FOR DEV ADDICTS. [Legal notice / Impressum](https://cloudogu.com/imprint.html)
