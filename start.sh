#!/bin/bash

echo "================================= StockMaster Deployment Script ================================="

# Check Java version
JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
echo "Java version: $JAVA_VERSION"

if [[ ! "$JAVA_VERSION" =~ ^17 ]]; then
    echo "Error: Java 17 is required"
    exit 1
fi

# Check Maven
if ! command -v mvn &> /dev/null; then
    echo "Error: Maven is not installed"
    exit 1
fi

# Build project
echo "Building project..."
mvn clean package -DskipTests

if [ $? -ne 0 ]; then
    echo "Build failed"
    exit 1
fi

# Run application
echo "Starting StockMaster application..."
java -jar target/StockMaster-1.0.0.jar
