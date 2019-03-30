# News Scraper Application

## How to run
- Install maven:
    * Mac: brew install maven
    * Linux: https://www.vultr.com/docs/how-to-install-apache-maven-on-ubuntu-16-04
- In terminal type: mvn clean spring-boot:run
- Once application is started, an info log with the following message will be printed "Started NewsScraperApplication in x seconds".
- Application will start up on port: 8080 by default.

## How to access
- Since this is the backend for a Rest API, every controller can be tested through PostMan / Web Browser.
- A list of endpoints is mentioned below in the Endpoints section.
- Swagger UI integration has also been provided for ease of access, and can be accessed at: http://localhost:8080/swagger-ui.html#/

## Endpoints
Following is the list of currently configured endpoints: (Can also be seen in Swagger-ui)
- /data-scrape/refresh-data - This will scrape data from The Hindu website Archive and insert into ES.
- /news/authors - This will provide a Lexicographically sorted list of Authors present in the data.
- /news/search (Params: "author", "description", "topic") - This will lookup the documents in ES which contain the provided set of parameters.

## Technologies used
- Java 8 - As a base language for the application.
- Spring Boot Framework - For IoC, Dependency Injection and Bean Management.
- Maven - For dependency management.
- HTML Unit - An open source Java library to process HTML as DOM elements and to open various URLs. 
- ElasticSearch - To store and search scraped data.
- Guava library - For various small String/Collection related operations.

## Configurability
- -Ddata-scrape-limit=n (Default value=5) can be passed to configure how many nested crawls need to be made. This has been set to avoid doing a lot of network calls. 