# Quarkus API Service

## Introduction

This application is the API service for the 
This application provides endpoints to view issues and create new issues. This service uses the Quarkus framework with RESTEasy Jax-RS 
for the REST API. Qute is used for the webpage templating. H2 database is used to store issue information.

## Technologies

Java 17.0.1\
Quarkus 2.6.2

## Running the Application in Dev Mode

The application can be run with ```./mvnw compile quarkus:dev``` and can be reached at
[localhost:8080/home](localhost:8080/home).

## Usage

Calls to the API will not be allowed unless the user is authenticated in the authentication service.
The user is authenticated as long as the 'quarkus-credential' cookie is present.
In the home page, links to each endpoint are available. There are links to view all the issues,
find a certain issue by ID, insert a custom issue with user input, insert an issue with arbitrary data,
and insert a specified number of issues with arbitrary data.

## Authentication

Before the API call is made, an interceptor checks for the 'quarkus-credential' cookie. If it is present then the call continues.
If it does not, then a 401 unauthorized status code and no data will be returned.