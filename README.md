## Dropwizard User Management Service

### Setup

1. Start database container in docker
    - Change dir to `mysql`
    - Build image with `docker build -t user-management-db:0.0.1 .`
    - Run image
      with `docker run --detach --name=user-management-db --publish 6603:3306 user-management-db:0.0.1`
2. Start application
    - Change dir to root
    - Run app with `./gradlew run`
