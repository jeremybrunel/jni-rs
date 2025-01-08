package src;

import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;

public class MyTask implements ITask {
    private static boolean libraryLoaded = false;
    private static final Object loadLock = new Object();

    // Declare the JNI method
    private static native byte[] performInRust(byte[] jsonSchemaInputsAsBytes);

    // Load the Rust library dynamically
    private static void loadRustLibrary(String relativeLibPath) {
        synchronized (loadLock) {
            if (!libraryLoaded) {
                try {
                    String absolutePath = Paths.get(relativeLibPath).toAbsolutePath().toString();
                    System.out.println("Loading Rust library from: " + absolutePath);
                    System.load(absolutePath);
                    libraryLoaded = true;
                } catch (Exception e) {
                    throw new RuntimeException("Failed to load the Rust library: " + e.getMessage(), e);
                }
            }
        }
    }

    // Public method to perform the task
    public byte[] perform(String relativeLibPath, byte[] jsonSchemaInputsAsBytes) {
        if (jsonSchemaInputsAsBytes == null) {
            throw new IllegalArgumentException("Input bytes cannot be null");
        }

        // Load the library
        loadRustLibrary(relativeLibPath);

        // Call the native method
        byte[] result = performInRust(jsonSchemaInputsAsBytes);
        if (result == null) {
            throw new RuntimeException("Rust function returned null");
        }
        return result;
    }

    // Main method for testing
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: java -jar MyTask.jar <path-to-rust-lib> <json-schema-input-as-base64>");
            System.exit(1);
        }

        try {
            String libPath = args[0];
            String base64Input = args[1];
            byte[] jsonSchemaInputsAsBytes = java.util.Base64.getDecoder().decode(base64Input);

            ITask task = new MyTask();
            byte[] result = task.perform(libPath, jsonSchemaInputsAsBytes);

            String base64Result = java.util.Base64.getEncoder().encodeToString(result);
            String humanReadableResult = new String(result, StandardCharsets.UTF_8);

            System.out.println("Base64 Result:");
            System.out.println(base64Result);
            System.out.println("\nHuman Readable Result:");
            System.out.println(humanReadableResult);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}