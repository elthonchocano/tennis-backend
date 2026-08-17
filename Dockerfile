FROM quay.io/quarkus/ubi9-quarkus-micro-image:2.0

WORKDIR /work/
COPY target/tennis-backend-1.0.0-SNAPSHOT-runner /work/application
RUN chmod +x /work/application

EXPOSE 8080
USER 10085

CMD ["./application", "-Dquarkus.http.host=0.0.0.0"]