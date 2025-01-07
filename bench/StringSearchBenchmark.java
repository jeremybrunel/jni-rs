import interfaces.BenchmarkTask;

public class StringSearchBenchmark {   

    public void run() throws Exception  {
        shortStringSearch();
        longStringSearch();
    }

    private void runStringSearchTest(String scenario, String bigString, String keyword) throws Exception {
        System.out.println("\n---------------------------------------------");
        System.out.println(scenario);
        System.out.println("---------------------------------------------");

        // Create tasks
        BenchmarkTask javaTask = new JavaSearchTask();
        BenchmarkTask rustTask = new RustSearchTask();

        // Time the Java approach
        long startJava = System.nanoTime();
        Object javaResult = javaTask.perform(bigString, keyword);
        long endJava = System.nanoTime();
        long durationJava = endJava - startJava;

        // Time the Rust approach
        long startRust = System.nanoTime();
        Object rustResult = rustTask.perform(bigString, keyword);
        long endRust = System.nanoTime();
        long durationRust = endRust - startRust;

        // Print results
        System.out.println("Java result: " + javaResult + " (took " + durationJava + " ns)");
        System.out.println("Rust result: " + rustResult + " (took " + durationRust + " ns)");
        double ratio = (double) durationJava / durationRust;
        System.out.println("Performance ratio (Java / Rust) = " + ratio);
    }

    /**
     * Demonstrates a short string search (few words).
     */
    private void shortStringSearch() throws Exception  {
        String scenario = "shortStringSearch - less than 10 words";
        String bigString = "Hello from Java, Hello from Rust, JNI is cool!";
        String keyword = "Rust";

        runStringSearchTest(scenario, bigString, keyword);
    }

    /**
     * Demonstrates a large string search (10 million+ chars).
     */
    private void longStringSearch() throws Exception  {
        String scenario = "longStringSearch - more than 10_000_000 chars";

        // We embed the keyword somewhere in between two large blocks of random text.
        String keyword   = "mySpecialKeyword";
        String bigString = createLargeRandomString(10_000_000)
                         + keyword
                         + createLargeRandomString(10_000_000);

        runStringSearchTest(scenario, bigString, keyword);
    }

    /**
     * Generates a large random ASCII string of given length.
     */
    private static String createLargeRandomString(int length) {
        StringBuilder sb = new StringBuilder(length);
        java.util.Random random = new java.util.Random(42); // seeded
        for (int i = 0; i < length; i++) {
            sb.append((char)(random.nextInt(26) + 'a'));
        }
        return sb.toString();
    }
}
