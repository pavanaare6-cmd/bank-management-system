#!/bin/bash
# Render build script for Bank Management System

echo "=== Building Bank Management System ==="
cd backend
mvn clean package -DskipTests

# Check if build succeeded
if [ -f "target/bank-management-1.0.0.jar" ]; then
    echo "✅ Build successful!"
else
    echo "❌ Build failed - JAR not found"
    exit 1
fi

echo "=== Build Complete ==="
