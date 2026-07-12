#!/bin/bash
set -e

echo "Fetching S3 bucket name..."
BUCKET_NAME=$(terraform -chdir=../tennis-infra output -raw pipeline_bucket_name)

echo "Step 1: Compiling native binary..."
mvn clean package -Dnative -Dquarkus.native.container-build=true -Plambda

echo "Step 2: Preparing package..."
# No necesitas hacer 'cp' ni buscar el 'runner', el plugin ya creó 'target/bootstrap'
zip -j target/function.zip target/bootstrap

echo "Step 3: Uploading to S3..."
aws s3 cp target/function.zip s3://${BUCKET_NAME}/builds/latest-function.zip

echo "Deployment preparation complete. Run 'git push' to trigger pipeline."