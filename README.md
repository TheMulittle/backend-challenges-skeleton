# API test automation skeleton

## Background

Ever needed to start a new test automation framework for APIs but suffered from analysis paralysis due to the enermous amount of options for creating a test framework? This project aims to help you by giving a base project that you can further enchance / modify as per your needs. The project includes:

* Modern stack: Gradle 7, JUnit 5, Cucumber 7, Spring WebTestClient
* Html Reporter to generate a good looking report with rich information such as all HTTP requests/responses logged for each step
* Support for parallel execution out of the box
* (TODO) Groovy support

## Using the mock server

This skeleton project is shipped with wiremock files (`test/resources/wiremock` and `test/resources/services.json`) so you have a base mock server to play with. More details on how to use it can be found [here](https://wiremock.org/studio/docs/getting-started/desktop/)

## Running tests

Once the wiremock server is running. Run the following command:

```bash
./gradlew clean build
```

Tests will be executed, the results will be found at `build/extentReport/Spark.html`

## Licensing

This project is licensed under BSD3. Feel free fork it and change it as you need as long as you comply with BSD3
