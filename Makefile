# Example Makefile

# 1) Build the Rust library
build_rust:
	@echo "==> Building Rust JNI library"
	cargo build --manifest-path rustLib/Cargo.toml

# 2) Build the Java classes (compile them into the current directory)
build_java:
	@echo "==> Compiling Java classes"
	javac -d . bench/*.java

# 3) Run the benchmark
run_benchmark:
	@echo "==> Running the Benchmark"
	# We assume you have a "Benchmark" class with a "main" method,
	# compiled into ./Benchmark.class.
	java \
	  -cp . \
	  -Djava.library.path=rustLib/target/debug \
	  Benchmark

# An "all" target if you want to build everything at once
all: build_rust build_java
