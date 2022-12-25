# Demo with minimal dependencies.

This app is the same example as the other ones with spring, quarkus, micronaut etc. but with a _pure_ approach. Meaning,
it uses no framework and only uses a couple of dependencies.

The goal is to demonstrate the implementation of the same functionality that is normally provided by well known java
frameworks.

Besides, another goal of this app is to show the overhead that those java frameworks may have as this application uses
none of them. This can be best experienced when the app is running in a docker container with very limited resources (
i.e. 1 CPU)

## Features

* Simple JDK HttpServer is used (no Tomcat, Jetty, etc.)
* Virtual Threads are also used and therefore the app needs at least JDK 19.
* In Memory H2 database.

## Dependencies

* Flyway for data migration.
* Jackson for JSON serialization.
* Hikari for connection pool.