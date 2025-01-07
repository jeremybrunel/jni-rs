import interfaces.BenchmarkTask;

public class RustSearchTask implements BenchmarkTask {

    // Load the native library (name depends on your compiled .so)
    static {
        System.loadLibrary("rustLib"); 
    }

    // Declare the JNI method
    private static native boolean findStringInRust(String haystack, String needle);

    @Override
    public Object perform(Object... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Requires at least 2 arguments: haystack, needle");
        }

        // 1) Cast to Strings
        String haystack = (String) args[0];
        String needle   = (String) args[1];

        // 2) Call the Rust JNI method
        boolean found = findStringInRust(haystack, needle);

        // 3) Return (auto-boxed) Boolean
        return found;
    }
}
