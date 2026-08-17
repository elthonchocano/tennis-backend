#!/bin/bash
set -e

echo "Step 1: Compiling native binary..."
./mvnw clean package -Dnative

echo "Step 2: Preparing native directory..."
mkdir -p native
cp target/tennis-backend-1.0.0-SNAPSHOT-runner native/application-runner

echo "Step 3: Compressing native binary with UPX..."
upx --best native/application-runner

echo "Step 4: Staging binary for version control..."
git add -f native/application-runner

echo "Native compilation and compression complete. Run 'git commit' and 'git push' to deploy."