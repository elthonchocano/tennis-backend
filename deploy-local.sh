#!/bin/bash
set -e

echo "Step 1: Compiling native binary..."
./mvnw clean package -Dnative

echo "Step 2: Compressing native binary with UPX..."
upx-ucl --best target/tennis-backend-1.0.0-SNAPSHOT-runner

echo "Native compilation and compression complete. Run 'git commit' and 'git push' to deploy."