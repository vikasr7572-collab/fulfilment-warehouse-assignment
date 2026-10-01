# Java Code Assignment

This is a short code assignment that explores various aspects of software development, including API implementation, documentation, persistence layer handling, and testing.

## About the assignment

You will find the tasks of this assignment on [CODE_ASSIGNMENT](CODE_ASSIGNMENT.md) file

## About the code base

This is based on https://github.com/quarkusio/quarkus-quickstarts

### Requirements

To compile and run this demo you will need:

- JDK 17+

No Docker or external database is required for local development and tests: those profiles use an embedded H2
database and load the sample data from `import.sql`. Production uses PostgreSQL.

### Configuring JDK 17+

Make sure that `JAVA_HOME` environment variables has been set, and that a JDK 17+ `java` command is on the path.

## Building the demo

Execute the Maven build on the root of the project:

```sh
./mvnw package
```

For the full test and coverage verification, run `./mvnw verify`. The JaCoCo report is written to
`target/site/jacoco/index.html`; CI enforces at least 80% instruction coverage for the core Location,
Warehouse, and Fulfilment domain rules. Every GitHub Actions run uploads this report as the `jacoco-report`
artifact for review.

## Implementation notes

Warehouse rules are implemented in dedicated validators for creation and replacement. Warehouse mutations
validate active business-unit uniqueness, configured locations, location capacity/count limits, and
stock/capacity consistency. Replacement is transactional: it archives the active unit and creates its
same-location, same-stock replacement together.

The fulfilment module follows a hexagonal layout: REST and database adapters call an application use case;
the domain owns ports and a dedicated allocation validator. It validates an allocation with `storeId`,
`productId`, and active `warehouseId`, transactional count checks, and database uniqueness constraints. It
enforces two warehouses per product/store, three warehouses per store, and five product types per warehouse
without losing historical warehouse data after archive. Store changes publish CDI events, and the legacy
gateway is invoked by an `AFTER_SUCCESS` observer only after a confirmed database commit.

## Application evidence

The following screenshots were captured while the Quarkus application was running locally with its H2 sample data.

### Warehouse API

![Warehouse API response](docs/screenshots/warehouse-api.png)

### Product API

![Product API response](docs/screenshots/product-api.png)

### Store API

![Store API response](docs/screenshots/store-api.png)

### Bonus fulfilment allocation

![Successful fulfilment allocation](docs/screenshots/fulfilment-allocation-success.png)

## Running the demo

### Live coding with Quarkus

The Maven Quarkus plugin provides a development mode that supports
live coding. To try this out:

```sh
./mvnw quarkus:dev
```

The API starts at `http://localhost:8080`. Useful endpoints include:

- `GET /product`
- `GET /store`
- `GET /warehouse`
- `POST /fulfilment-allocation`

In this mode you can make changes to the code and have the changes immediately applied, by just refreshing your browser.

    Hot reload works even when modifying your JPA entities.
    Try it! Even the database schema will be updated on the fly.

## (Optional) Run Quarkus in JVM mode

When you're done iterating in developer mode, you can run the application as a conventional jar file.

First compile it:

```sh
./mvnw package
```

For production mode, make sure a PostgreSQL instance is running. To set one up with Docker:

```sh
docker run -it --rm=true --name quarkus_test -e POSTGRES_USER=quarkus_test -e POSTGRES_PASSWORD=quarkus_test -e POSTGRES_DB=quarkus_test -p 15432:5432 postgres:13.3
```

Connection properties for the Agroal datasource are defined in the standard Quarkus configuration file,
`src/main/resources/application.properties`.

Then run it:

```sh
java -jar ./target/quarkus-app/quarkus-run.jar
```
    Have a look at how fast it boots.
    Or measure total native memory consumption...

### Run the production profile with Docker Compose

Docker Compose starts the application together with PostgreSQL using the production profile:

```sh
./mvnw package
docker compose up --build
```

The API is then available at `http://localhost:8080`. Stop the stack with `docker compose down`.


## See the demo in your browser

Navigate to:

<http://localhost:8080/index.html>

Have fun, and join the team of contributors!

## Troubleshooting

Using **IntelliJ**, in case the generated code is not recognized and you have compilation failures, you may need to add `target/.../jaxrs` folder as "generated sources".
