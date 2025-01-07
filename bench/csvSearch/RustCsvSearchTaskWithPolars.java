
import interfaces.BenchmarkTask;

public class RustCsvSearchTaskWithPolars implements BenchmarkTask {

    static {
      System.loadLibrary("csvsearch");
    }

    // We'll define a native method that returns a JSON or string with row,col pairs.
    // We pass only "searchWord" from Java to Rust. The CSV path is also hard-coded in Rust.
    private static native String findInRustCsvWithPolars(String searchWord, String csvPath);

    @Override
    public Object perform(Object... args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Need the searchWord argument and csv file path.");
        }

        String searchWord = (String) args[0];
        String csvPath = (String) args[1];

        return findInRustCsvWithPolars(searchWord, csvPath);
    }
}
