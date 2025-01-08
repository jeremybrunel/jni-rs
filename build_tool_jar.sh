#!/bin/bash

# Exit on any error
set -e

# Print commands before executing them
set -x

# Define base directory (assuming script is run from project root)
BASE_DIR=$(pwd)
RUST_DIR="$BASE_DIR/rustLibs/performLib"
JAVA_DIR="$BASE_DIR/javaLibTool"

# Colors for output
GREEN='\033[0;32m'
RED='\033[0;31m'
NC='\033[0m' # No Color

echo -e "${GREEN}Building Rust Library...${NC}"
cd "$RUST_DIR"
cargo clean
cargo build --release

echo -e "${GREEN}Building Java Components...${NC}"
cd "$JAVA_DIR"

# Create build directory if it doesn't exist
mkdir -p build

# Compile Java code and generate header
javac -d build src/*.java
javac -h . src/MyTask.java

# Create JAR
jar cfe build/MyTask.jar src.MyTask -C build .

echo -e "${GREEN}Build completed successfully!${NC}"
echo "You can now run the program with:"
echo "cd $JAVA_DIR"
echo "java -jar ./build/MyTask.jar ../rustLibs/performLib/target/release/libperform.so eyJrZXkiOiJ2YWx1ZSJ9"