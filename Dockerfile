# Use official Rust base image with Cargo + Rust installed
FROM rust:1.72

# Create a working directory
WORKDIR /app

# Install Java + Make + curl
RUN apt-get update && \
    apt-get install -y --no-install-recommends openjdk-17-jdk make curl && \
    rm -rf /var/lib/apt/lists/*

# Copy all local files into the container
COPY . /app

# Create build directory
RUN mkdir -p build

# Build steps:
# 1) Build Rust JNI library
# 2) Build Java code
RUN make build_rust
RUN make build_java

# By default, run the benchmark script
CMD ["./run_benchmark.sh"]