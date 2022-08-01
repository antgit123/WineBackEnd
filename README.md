# Getting Started with Wine Backend App

This project is a springboot app that runs the backend service for retrieving wine Data. It uses an in memory H2 database
to process the wine dataset (collection of JSON documents) and expose it in the form of Rest API services

# Approach/ Design Considerations

A wine is collection of grapes that have specific properties. Keeping that in mind and assuming the properties are fixed (not schemaless), a relational database design 
has been chosen for this service. The database stores the wine and grape component as separate entities with the wine entity having a 1 to many relationship with grape 
component. Also, on analyzing the dataset the lotCode is assumed to be the primary key of the wine entity as it was found to be unique for each wine entry.

## Depenedencies

Please install Lombok if there are compiler issues associated with Lombok in your IDE --> https://codippa.com/lombok/installing-lombok/

## Available Scripts

In the project directory, you can run:

### `./gradlew build or gradle build`

Compiles and builds the project to construct the build artifacts and run tests. 


### `./gradlew bootRun or gradle bootRun`

Starts and runs the backend service from command line. Curl commands can be used to verify the working of the service
Example: curl http://localhost:8081/wines/11YVCHAR001

### `./gradew test or gradle test`

Runs the test suite for the wine backend service app.

