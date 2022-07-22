## Description

The purpose of this demo is to reproduce an issue with spring-data-cassandra 3.4.2, 
where it's not possible to resolve the name of a table, that already exists in the database.

#### Exception Message: 

```com.datastax.oss.driver.api.core.servererrors.InvalidQueryException: table SAMPLE does not exist```

If we force the spring-data-cassandra 3.4.1 version, everything works as expected (uncomment the lines 36 - 40 in `pom.xml`)

## Example demonstrating the issue

This module exposes a single endpoint `/test` which tries to write a new `Sample` entity into the database.
There is a http file under `src/test/http/test.http` that will call the `/test` endpoint.

## Prerequisites

In order to start this module in your local machine, the following software must be installed in your machine:

* Oracle's JDK, version 17
* Maven 3.8.6
* Docker 20.10.17

#### Hosts
The following hosts must be added in your `/etc/hosts` file:

    127.0.0.1	cassandra

#### Services
The module also depends on the following services

#### Required services

**Cassandra: 4.0.4**

    docker run -d --name cassandra -p 9042:9042 cassandra:4.0.4

Schema is created automatically during application startup.

## Build Process

After installing all necessary software and services, you can build the module by issuing the following command:

    mvn clean install

## Run

You may run this module as a Spring Boot application:

    mvn spring-boot:run
