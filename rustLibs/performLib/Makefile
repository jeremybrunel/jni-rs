# Project structure
SRC_DIR = src
BUILD_DIR = target
LIB_NAME = performLib

# Build configuration
RELEASE_LIB_PATH = $(BUILD_DIR)/release/lib$(LIB_NAME).so
DEBUG_LIB_PATH = $(BUILD_DIR)/debug/lib$(LIB_NAME).so

# Default target: Build the release version
all: release

# Build the release version
release:
	@echo "==> Building release version of $(LIB_NAME)"
	cargo build --release

# Build the debug version
debug:
	@echo "==> Building debug version of $(LIB_NAME)"
	cargo build

# Clean the build artifacts
clean:
	@echo "==> Cleaning build artifacts"
	cargo clean

# Display the release build output path
output:
	@echo "Release library built at: $(RELEASE_LIB_PATH)"
	@echo "Debug library built at: $(DEBUG_LIB_PATH)"
