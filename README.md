# logback-testng
Logback appender for TestNG Reporter

This appender enables users to direct the Logback output from TestNG tests to the TestNG Reporter. This output will be captured in the test result artifacts of each test method. This appender extends the standard [OutputStreamAppender](http://logback.qos.ch/manual/appenders.html#OutputStreamAppender), providing the full range of encoding options.

**NOTE**: Although the `ReporterAppender` class extends [OutputStreamAppender](http://logback.qos.ch/apidocs/ch/qos/logback/core/OutputStreamAppender.html), selection of a target output stream is not supported. Output from this appender is always directed to a `ReporterOutputStream`, which emits its contents to the TestNG [Reporter](http://testng.org/javadocs/org/testng/Reporter.html).

## Full configuration example

Add `logback-testng` and `logback-classic` as library dependencies to your project.

```xml
[maven pom.xml]
<dependency>
    <groupId>com.github.sbabcoc</groupId>
    <artifactId>logback-testng</artifactId>
    <version>1.0.0</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.1.2</version>
    <scope>runtime</scope>
</dependency>
```

This is an example `logback.xml` that uses a common `PatternLayout` to encode a log message as a string.

```xml
[src/main/resources/logback.xml]
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
    <appender name="STDOUT" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- This is the ReporterAppender -->
    <appender name="TestNG" class="com.github.sbabcoc.logback.testng.ReporterAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <root level="info">
        <appender-ref ref="STDOUT" />
        <appender-ref ref="TestNG" />
    </root>
</configuration>

```

You may also look at the [complete configuration examples](src/example/resources/logback.xml)

## License

This project is licensed under the [Apache License Version 2.0](LICENSE).
