# Introduction

This application is the API service for the Issue application, with the main goal of being used to compare against similar frameworks. 
This application provides endpoints to view issues and create new issues. This service uses the Quarkus framework with RESTEasy Jax-RS.

# Running the Application in Dev Mode

The application can be run with the shell script:
./mvnw compile quarkus:dev

# Usage

Calls to the API will not be allowed unless the user is authenticated in the authentication service.
The user is authenticated as long as the 'quarkus-credential' cookie is present.
In the home page, links to each endpoint are available. There are links to view all the issues,
find a certain issue by ID, insert a custom issue with user input, insert an issue with arbitrary data,
and insert a specified number of issues with arbitrary data.