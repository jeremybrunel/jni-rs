# Directory structure
SRC_DIR = bench
BUILD_DIR = build
RUST_LIBS_DIR = rustLibs

# Find all Java files
JAVA_FILES := $(shell find $(SRC_DIR) -name "*.java")

# Find all Rust library directories
RUST_LIB_DIRS := $(shell find $(RUST_LIBS_DIR) -maxdepth 1 -type d -not -path $(RUST_LIBS_DIR))

# 1) Build all Rust libraries
build_rust:
	@echo "==> Building Rust JNI libraries"
	@for lib in $(RUST_LIB_DIRS); do \
		echo "Building $$lib"; \
		cargo build --manifest-path $$lib/Cargo.toml; \
	done

# 2) Create build directory
create_build_dir:
	@echo "==> Creating build directory"
	mkdir -p $(BUILD_DIR)

# 3) Build the Java classes
build_java: create_build_dir
	@echo "==> Compiling Java classes"
	javac -d $(BUILD_DIR) $(JAVA_FILES)

# 4) Run the benchmark
run_benchmark:
	@echo "==> Running the Benchmark"
	java \
		-cp $(BUILD_DIR) \
		-Djava.library.path=$(shell echo $(RUST_LIB_DIRS) | sed 's/ /\/target\/debug:/g')/target/debug \
		Benchmark

# Clean build artifacts
clean:
	@echo "==> Cleaning build artifacts"
	rm -rf $(BUILD_DIR)
	@for lib in $(RUST_LIB_DIRS); do \
		echo "Cleaning $$lib"; \
		cargo clean --manifest-path $$lib/Cargo.toml; \
	done

# An "all" target if you want to build everything at once
all: build_rust build_java

# Debug target to check discovered paths
debug:
	@echo "Rust library directories:"
	@echo $(RUST_LIB_DIRS)
	@echo "Java library path:"
	@echo $(shell echo $(RUST_LIB_DIRS) | sed 's/ /\/target\/debug:/g')/target/debug