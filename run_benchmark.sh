#!/usr/bin/env bash
set -e

echo "==> Running benchmark inside Docker..."
echo "    (Comparing pure Java vs. JNI/Rust)"

# Optionally rebuild (but we already did in the Dockerfile steps)
make build_rust
make build_java

make run_benchmark
