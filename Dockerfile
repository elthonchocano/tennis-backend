# --- Stage 1: Build the native executable ---
FROM quay.io/quarkus/ubi9-quarkus-mandrel-builder-image:23.1-java21 AS build
COPY --chown=quarkus:quarkus mvnw /code/mvnw
COPY --chown=quarkus:quarkus .mvn /code/.mvn
COPY --chown=quarkus:quarkus pom.xml /code/pom.xml

USER root
RUN chmod +x /code/mvnw
USER quarkus

WORKDIR /code
RUN ./mvnw dependency:go-offline

# Copy the entire src directory maintaining your hexagonal architecture layout
COPY --chown=quarkus:quarkus src /code/src

RUN ./mvnw package -Dnative -Dquarkus.native.container-build=false

# --- Stage 2: Create the runtime container ---
FROM quay.io/quarkus/ubi9-quarkus-micro-image:2.0
WORKDIR /work/
COPY --from=build --chown=quarkus:quarkus /code/target/*-runner /work/application

EXPOSE 8080
USER 10085

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]