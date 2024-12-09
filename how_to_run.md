## Requirements

* Java >= 21
* Docker
* Docker Compose
* Maven

## Run Locally

To run locally, run the following command. It will expose application `localhost:8080`.
It will start the database at `localhost:3306`.

```shell
mvn spring-boot:run
```

## Run everything in Docker

If you don't have Maven installed, you can run everything in the Docker. It will expose application `localhost:8080`.
It will start the database at `localhost:3306`.

```shell
make docker-compose
```