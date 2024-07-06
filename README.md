# API test automation skeleton

## Background

Ever needed to start a new test automation framework for APIs but suffered from analysis paralysis due to the enormous amount of options for creating a test framework? This project aims to help you by giving a base project that you can further enchance / modify as per your needs. The project includes:

* Modern stack: Gradle 8, JUnit 5, Cucumber 7, Spring WebTestClient
* Html Reporter to generate a good looking report with rich information such as all HTTP requests/responses logged for each step
* Support for parallel execution out of the box
* Unit tests for non-glue code classes

## Running the tests

### Running unit tests

This project include unit tests for classes that are not cucumber related (glue code). The reasoning behind it is that
we need to ensure an automation suite is:

* Reliable - test automation shouldn´t fail due to poor coding (creating false positives/negatives)
* Readable - unit tests aids in keeping the code more digestable to whoever is reading it
* Maintainable - refactoring is much easier when unit tests can back them

In order to run the unit tests

```bash
./gradlew clean ut
```

### Running integration tests

There are different options to run Cucumber test scenarios. They will be executed and results will be found at `build/extentReport/Spark.html`

* To run ALL test scenarios inside ALL `.feature` files:

```bash
./gradlew clean it
```

* To run only scenarios tagged with `@wip`:

```bash
./gradlew clean wip
```

#### Updating test cases on XRay

This project comes with a Cucumber Reporter responsible for updating XRay test runs in JIRA that are linked to a  particular test execution. More about XRay [here](https://docs.getxray.app/display/XRAYCLOUD/About+Xray)

Scenarios that have a tag following `@TC:CC-NN` format are eligeble to be updated. In the format, CC-NN is the JIRA ID of a XRay test case, such as `PB-11`. They are eligible, but not necessarly will be updated as two conditions need to be met: the integration needs to be enabled and the Test Case with id `CC-NN` must to be linked to the test execution

To enable the integration, the following command line arguments must be passed to Gradle:

* -DtestExecution: the test execution JIRA ID whose test runs we want to update. Ex: "BS-11"
* -DxRayId: XRay's client ID. More information [here](https://docs.getxray.app/display/XRAYCLOUD/Authentication+-+REST)
* -DxRaySecret: XRay's client secret. More information [here](https://docs.getxray.app/display/XRAYCLOUD/Authentication+-+REST)
* -DxRayBaseUrl: XRay's base URL. For cloud JIRA, it will always be <https://xray.cloud.getxray.app/api>

Example command to run tests marked with @wip and update test runs of a particular test execution in XRay:

```bash
./gradlew clean wip \
-DtestExecution=[JIRA Test Execution] \
-DxRayId=[Client ID] \ 
-DxRaySecret=[Client Secret] \ 
-DxRayBaseUrl="https://xray.cloud.getxray.app/api" 
```

#### Debugging integration tests

While some IDEs have integrated debuging capabilities for Gradle that makes the debug initialization transparent for the developers , others don't. Therefore use `./gradlew_debug` if you want to open a debug session for 'wip' that can be attached by your favorite debugger. Parameters, such `-DtestExecutionId`, can be passed normally

## Using the mock server

This skeleton project is shipped with wiremock files (`test/resources/wiremock` and `test/resources/services.json`) so you have a base mock server to play with. More details on how to use it can be found [here](https://wiremock.org/studio/docs/getting-started/desktop/)

### Running it

Open a command line tool and go to [the folder where wiremock is](src\test\resources\wiremock-studio-2.32.0-18.jar) and run `java -jar wiremock-studio-2.32.0-18.jar` to start the mock server

**NOTE:** it is better to run tests in sequence because multiple scenarios will touch the same endpoint and due to wiremock´s nature
we don´t have a work around (in the future Wiremock will be substituted, meanwhile that´s the best option). To run in sequence set `cucumber.execution.parallel.enabled=false` in [junit properties file](src\test\resources\junit-platform.properties)

**NOTE 2:** even if run the tests in sequence, some scenarios will pass and others will fail. That´s on porpouse to show both passed and failed scenarios

## The framework

### Sending requests and asserting responses

There are two flavours of sending requests and asserting the response content that can be used
    1. Directly defining the request payload and response body in the step
    2. Having dedicated steps for sending requests and asserting their contents

For the first case, the developer is required to write the steps following the example on XXXXXXX
For the second case:

Defining the request payload directly in the payload is usually straightforward. It is just a matter of using the step `When I send request to '/endpoint' with payload` and then specifying the payload as triple quotted
string. For example:

```text
When I send request to '/endpoint' with payload
""" 
  {
    "property1": "value",
    "property2": {
      "innerProperty": 0
    }
  }
""" 
```

### Placeholders

It is not so straightforward, tough, when dynamic values are needed. For instance, consider a scenario wherein a new user needs to be registred, but a unique user is required everytime, how to handle such scenario? Placeholders are the answer. For example:

When I send request to '/endpoint' with payload
"""
  {
    "userName": "New user %[UUID]",
  }
"""

In above payload, the placeholder `%[UUID]` will be substituted by a randomly generated UUID. Then an assertion can be made using:

```text
And response body is
"""
  {
    "message": "User created successfully!"
  }
"""
```

What happens if there is a need for checking that this value appears in the response as well? A more complex placeholder can be used:

```text
When I send request to '/endpoint' with payload
"""
  {
    "userName": "New user ![userID=%[UUID]]",
  }
"""
```

In above payload, the placeholder have two functions: first it will generate a random UUID; secondly it will assign this value to a variable called "userID" that can be latter retrieved. So the following step may be used:

```text
Then response body is
"""
  {
    "userName": "User with the following name was created: New user $[userID]"
  }
"""
```

In the response, `$[userID]` will be replaced by the value stored in 'userID' variable

What can be done if the variable value is required in the request endpoint? A placeholder can simply be used in the step with a stored variable: 

```text
When I send request to '/$[userId]' with payload
```

### Running tests in isolation

@isolated

## Licensing

This project is licensed under BSD3. Feel free fork it and change it as you need as long as you comply with BSD3
