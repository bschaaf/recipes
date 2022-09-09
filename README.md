# Cooking #

Good day,

This implementation is done with Spring Boot and Postgres. When de Spring boot application starts the recipes.json file is read and the records are stored in the Postgres database. The database has two tables: `recipes`, and `ingredients`. From each ingredient there is a reference (foreign key) to the corresponding recipe.

There is a Docker Compose file, `cooking.yml` in the root location which runs three containers: the Postgres database, PgAdmin for database management and the Spring Boot app which exposes the REST api and performs the main business logic. To start the containers type: `docker-compose -f cooking.yml up`. On `localhost:8090` there is PgAdmin and on `localhost:8080/swagger-ui.html` the REST api can be accessed with Swagger (Open API). These ports can be modified in `cooking.yml`. PgAdmin requires you to login (`postgres@gmail.com/password`) and the first time the database can be configured using the Docker link `pg-server` (only needed for database monitoring).

Especially the uri /api/cooking/v1/recipes/filter can be used to test the requested search functionality.

A Dockerfile exists in the root directory from which the Docker image for the app can be built. Please note that in the `application.properties` there are two lines for the datasource url configuration. One of those lines needs to be commented out so that there is one active datasource. The datasource url containing `pg-server` is the one used in the Docker Compose file and links the various containers to the database. When the app is run as a standalone jar outside the Docker container the other datasource line needs to be activated and configured to the location of the database.

## Configuration ##

In application.properties the following properties can be configured:

- spring.datasource.url=jdbc:postgresql://localhost:5432/postgres
- #spring.datasource.url=jdbc:postgresql://pg-server:5432/postgres (use this setting for Docker Compose, link `pg-server`)
- spring.datasource.username=postgres
- spring.datasource.password=password

- abnamro.baseUrl=http://localhost:8080
- abnamro.recipesFile=recipes.json



## Execution ##

Besides with Docker Compose (recommended) the application can be run with `mvn spring-boot:run` and also as standalone jar with `java -jar app.jar` (assuming a database is available).

To build the image for the app with the Dockerfile, type the following from the root directory containing `Dockerfile`:
- `docker build -t bschaaf/cooking .`



## Production ##

Currently the following setting in `application.properties` enables automatic creation of the database tables when the application is started:
- `spring.jpa.hibernate.ddl-auto=create`

In a production environment this automatic creation needs to be disabled, e.g. by value `none`.



## Further steps ##

Further steps in the implementation:
- more tests could be written
- use in-memory h2 database for integration tests



## Contact ##

For any clarification: benoit.schaaf@yahoo.com, +31625051061.

Best regards,
Benoît

