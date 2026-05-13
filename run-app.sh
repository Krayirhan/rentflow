#!/bin/bash
# RentFlow Desktop Application - Linux/macOS Startup Script
# Usage: ./run-app.sh [dev|prod]

PROFILE="dev"
if [ ! -z "$1" ]; then
    PROFILE=$1
fi

echo ""
echo "===================================="
echo "  RentFlow Desktop Application v4.0"
echo "===================================="
echo ""
echo "Starting in $PROFILE mode..."
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "Error: Java is not installed or not in PATH"
    echo "Please install Java 17 or higher"
    exit 1
fi

# Get the directory where this script is located
SCRIPT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

# Set database password if production mode
if [ "$PROFILE" == "prod" ]; then
    echo ""
    echo "============================================"
    echo "  Production Mode - Database Configuration"
    echo "============================================"
    echo "Make sure PostgreSQL is running on localhost:5432"
    echo "with database name: rentflow"
    echo "username: rentflow"
    echo ""
    read -s -p "Enter PostgreSQL password for 'rentflow' user: " DB_PASSWORD
    echo ""
    DB_PASSWORD_ARG="-DDB_PASSWORD=$DB_PASSWORD"
fi

# Run the application
echo "Launching RentFlow..."
cd "$SCRIPT_DIR"

JAR_FILE=$(find target -name "rentflow-*-exec.jar" 2>/dev/null | head -1)

if [ -z "$JAR_FILE" ]; then
    echo ""
    echo "Error: Application JAR not found!"
    echo "Please run: mvn clean package"
    echo ""
    exit 1
fi

java $DB_PASSWORD_ARG -jar "$JAR_FILE" --spring.profiles.active=$PROFILE
