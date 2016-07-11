## About this template

This is a template to get started with a Gauge project that uses Selenium as the driver to interact with a web browser.

## Installing this template

    gauge --install java_maven_selenium

## Building on top of this template

### Defining Specifications

* This template includes a sample specification which opens up a browser and navigates to `Get Started` page of Gauge.
* Add more specifications on top of sample specification.

Read more about [Specifications](http://getgauge.io/documentation/user/current/specifications/README.html)

### Writing the implementations

This is where the java implementation of the steps would be implemented. Since this is a Selenium based project, the java implementation would invoke Selenium APIs as required.

_We recommend considering modelling your tests using the [Page Object](https://github.com/SeleniumHQ/selenium/wiki/PageObjects) pattern, and the [Webdriver support](https://github.com/SeleniumHQ/selenium/wiki/PageFactory) for creating them._

- Note that every Gauge step implementation is annotated with a `Step` attribute that takes the Step text pattern as a parameter.
Read more about [Step implementations in Java](http://getgauge.io/documentation/user/current/test_code/java/java.html)

### Execution

* You can execute the specification as:

```
mvn gauge:execute
```

## Install Apache Maven

Maven is mandatory because the integration test project is written as an maven project. To install maven on your linux system perform the following steps.

- First you have to download maven from https://maven.apache.org/download.cgi.
- Ensure you have java installed and its location set as environment variable.
- You either got an `.zip` or an `.gz` file. The choice is yours. Now extract maven like presented.
```
unzip apache-maven-3.3.9-bin.zip
```
or
```
tar xzvf apache-maven-3.3.9-bin.tar.gz
```    
- Now add the `bin` directory of the created directory `apache-maven-3.3.9` to the `PATH` environment variable.
- Verify your version with `mvn -v`.

## Installing Gauge

The integration tests are executed via gauge an light-weight cross-platform test automation tool. To run the tests you have to download gauge as described.

- Download Gauge from http://getgauge.io/get-started/.
- Then execute the folowing commands:
```
unzip gauge-$VERSION-$OS.$ARCH.zip
./install.sh
```

## Installing chromedriver

It is necessary to have chromedriver installed on your system to run integration tests via Google Chrome. Google Chrome is preferable to Firefox due to connection issues.

- Assuming you are running a 64-bit OS, download the latest version of chromedriver and unzip it.
```
wget -N http://chromedriver.storage.googleapis.com/2.10/chromedriver_linux64.zip -P ~/Downloads
```
```
unzip ~/Downloads/chromedriver_linux64.zip -d ~/Downloads
```
- Make the file you just extracted executable and move it to `/usr/local/share`.
```
chmod +x ~/Download/chromedriver
```
```
sudo mv -f ~/Downloads/chromedriver /usr/local/share/chromedriver
```
- Now create symlinks to chromedriver.
```
sudo ln -s /usr/local/share/chromedriver /usr/local/bin/chromedriver
```
```   
sudo ln -s /usr/local/share/chromedriver /usr/bin/chromedriver
```
- Now chromedriver will be found on your system.    

# Executing integration-tests

In the following section setting up variables to start integration-tests will be described. Short examples show how to start Gauge tests using Maven via command line.

## Configurate Url

If you are in the `integration-tests` folder, you might configurate the test environment variables using the command line as follows without changing `gauge_jvm_args` manually in the file `./env/default/java.properties`:
```
gauge_jvm_args= -Deco.system=${url} mvn test
```
e.g. if the url is `https://192.168.115.136`
```
gauge_jvm_args= -Deco.system=https://192.168.115.136 mvn test
```
This command will define `-D` the system property `eco.system` which is needed for the test environment and execute `mvn test` to start the integration-tests with that property. The specified url should be the same url to open cloudogu in the browser. Once configurated integration tests can be started with `mvn test`.

## Start tests using tags

While `mvn test` will start all Gauge specifications defined in the integration-tests, it might be useful only to start certain tests. The scenarios inside the specification files are marked with tags. So if only that scenario should be started you have to enter `mvn test -Dtags=${tag}` where `${tag}` is predefined in the spec files in the `specs` directory. An command could be `mvn test -Dtags=jenkins` to start all Jenkins scenarios. The following values for `${tag}` are allowed:

Tag             | Definition
---             | ----------
jenkins         | Starts all tests associated with Jenkins
redmine         | ... with Redmine
usermgt         | ... with Usermgt
sonar           | ... Sonar
nexus           | ... with Nexus
scm             | ... with SCM

workflow        | Starts all Workflow tests
rest_api        | Starts all REST Api tests
rest_api_token  | Starts REST Api tests using tokens/keys
single_sign_out | Starts all Single Sign Out tests
groups          | Starts tests involving groups
attributes      | Starts tests to check user attributes

Besides using only one tag it is possible to use a set of tags, assumed the certain set is defined. To use multiple tags use `mvn test -Dtags="${tag0},${tag1}"`. For example: The command `mvn test -Dtags="jenkins, workflow"` will test the workflow of Jenkins. Allowed tag combinations are presented in the table:

Specification tag | Scenario tags
----------------- | -------------
                  | workflow | rest_api | rest_api_token | single_sign_out | groups | attributes
                  |---------:|---------:|---------------:|----------------:|-------:|-----------:|
jenkins           | yes      | yes      | yes            | yes             | yes    | yes
redmine           | yes      | yes      | yes            | yes             | yes    | yes
usermgt           | yes      | yes      | no             | yes             | no     | no
sonar             | yes      | yes      | yes            | yes             | yes    | yes
nexus             | yes      | yes      | yes            | yes             | yes    | no
scm               | yes      | yes      | no             | yes             | yes    | yes
