# Tennis League Manager Backend API

A production-grade, highly scalable, and 100% serverless backend engine built with **Java 21** and the **Quarkus Framework**. This microservice handles business logic, automated tournament orchestration, and reactive database persistence for the Tennis League platform.

## 🚀 Key Architectural Features

* **Reactive & Non-Blocking Architecture:** Built on top of **Quarkus Reactive Streams** using the Vert.x engine and `quarkus-reactive-pg-client` for high throughput and optimal resource utilization under heavy API loads.
* **Fully Serverless Deployment:** Native integration with **AWS Lambda** via API Gateway HTTP API proxying, scaling down to absolute zero when idle to eliminate infrastructure idle costs.
* **VPC Isolation & Enterprise Security:** Safely deployed inside private subnets within a custom AWS VPC. Outbound database traffic is strictly restricted via targeted Security Groups.
* **OAuth2 / OIDC Identity Federation:** Secure JWT access token validation integrated natively with **AWS Cognito** and federated Google Social Sign-In.

---

## 🛠️ Tech Stack & Extensions

* **Runtime:** Java 21 / Quarkus 3.x
* **Database Client:** Hibernate Reactive with Panache (PostgreSQL)
* **Infrastructure Code:** Terraform IaC
* **CI/CD Pipeline:** AWS CodePipeline & AWS CodeBuild (fully decoupled from frontend)
* **Security Framework:** Quarkus Security OIDC

## 💻 Local Development

### Prerequisites
* Java 21 JDK installed.
* A local PostgreSQL database instance running.

### Running in Dev Mode
To launch the application with active live-coding enabled (changes apply instantly without restarting):

```shell
./mvnw compile quarkus:dev
```
Note: The Quarkus Dev UI is available locally at: http://localhost:8080/q/dev/

### Environment Variables for Local Context
Create a local .env or set up your system environment before launching quarkus:dev:

```shell
DB_HOST=localhost
DB_PORT=5432
DB_NAME=tennis_league
DB_USERNAME=your_local_user
DB_PASSWORD=your_local_password
COGNITO_USER_POOL_ID=your_aws_user_pool_id
COGNITO_CLIENT_ID=your_aws_client_id
```

## 📦 Packaging and Deployment Artifacts

### Standard JVM Build
To compile and package the deployment bundle into a standard runnable layout:
```shell
./mvnw package
```
This produces the artifact outputs inside the target/quarkus-app/ directory.

### AWS Lambda Bundle Preparation
The project is optimized to bundle its runtime stream handlers directly into an AWS-compliant layout. For building the specific zip package pushed by our CI/CD CodeBuild automation to the Lambda execution context, use:
```shell
./mvnw clean package
```
The deployment package is output directly to ./target/function.zip.

## 🌐 Production Deployment & Infrastructure Note
This infrastructure is fully provisioned using Terraform.

### ⚠️ Critical Infrastructure Note (Initial Deployment):
During the first terraform apply, Terraform requires a physical file to exist at ./target/function.zip to pass AWS API validations and register the aws_lambda_function resource.

If the deployment is executed before the local Java compilation is finished, a placeholder/dummy ZIP can be used to unblock the initial infrastructure provisioning. Once the infrastructure is online, the AWS CodePipeline takes full ownership of the Lambda code. The pipeline will automatically compile the real Quarkus code in Java 21 via AWS CodeBuild and hot-swap the dummy file with the actual production code on the subsequent Git pushes.

### 🤖 CI/CD Automation Flow
This repository features zero-touch automation via AWS CodePipeline.
```
[ Git Push to main ] ➔ [ GitHub Webhook Trigger ] ➔ [ AWS CodeBuild (Java 21 Build) ] ➔ [ AWS Lambda Live Code Update ]
```
1. Code changes are pushed to the main branch.

2. AWS CodeBuild intercepts the event, boots an optimized Linux container environment, and executes the compilation targets defined in buildspec.yml.

3. The newly generated function.zip artifact is dynamically updated into the active production AWS Lambda function block without downtime.

### 📖 Related Framework Documentation
* [AWS Lambda HTTP/REST API Gateway Guide](https://quarkus.io/guides/aws-lambda-http)
* [Reactive SQL Clients in Quarkus](https://quarkus.io/guides/reactive-sql-clients)
* [OpenID Connect & Bearer JWT Security Guide](https://quarkus.io/guides/security-openid-connect)