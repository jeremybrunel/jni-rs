# Directory structure
SRC_DIR = src
BUILD_DIR = build
JAR_NAME = MyTask.jar

# Find all Java files
JAVA_FILES := $(shell find $(SRC_DIR) -name "*.java")

# 1) Compile Java classes
build_java:
	@echo "==> Compiling Java classes"
	mkdir -p $(BUILD_DIR)
	javac -d $(BUILD_DIR) $(JAVA_FILES)

# 2) Package into a JAR
build_jar: build_java
	@echo "==> Packaging into $(JAR_NAME)"
	jar cfe $(BUILD_DIR)/$(JAR_NAME) src.MyTask -C $(BUILD_DIR) .

# 3) Clean build artifacts
clean:
	@echo "==> Cleaning build artifacts"
	rm -rf $(BUILD_DIR)

# Default target
all: build_jar
