# Demo for Java in (Docker) Containers

The purpose of this demo is to show the different runtime aspects of different java frameworks when running in containers.

One of the easiest aspects to demonstrate is the startup time. 


## How to run each demo

* First, build each application so that the executable jar is generated.
* Run the application in Docker by executing the provided `runInDocker.sh` script.

You can play a little bit with the settings of the container to see how they affect the startup time of the application.

## The application

Each demo implements the same application with a different framework. The application is quite simple and it is just for demo purposes. Every demo uses more or less the same features in order to be able to compare them. For instance, no application uses JPA but it uses its corresponding JDBC Data approach.

The only exception is the demo called “minimal”. This is a pure Java (well, written in Kotlin but only using Java APIs) application with just 3 dependencies.

The common features of the application are as follows:

* Load application configuration.
* Perform a database migration.
* Expose the data via REST.

All applications expose their data via the Endpoint

```
http://localhost:8080/authors
```