import interfaces.BenchmarkTask;

public class CsvSearchBenchmark {

    String lightData = "data/light-data.csv";
    String mediumData = "data/medium-data.csv";
    String heavyData = "data/heavy-data.csv";

    BenchmarkTask javaTask = new JavaCsvSearchTask();
    BenchmarkTask rustTask = new RustCsvSearchTask();
    BenchmarkTask rustPolarsTask = new RustCsvSearchTaskWithPolars();

    public void run() throws Exception {

        printTitle("Light data (10 lines)");

        runCsvSearch("word", lightData, false);
        runCsvSearch("colehumphrey", lightData, false);
        runCsvSearch("??", lightData, false);
        runCsvSearch("846-790-4623x4715", lightData, false);
        runCsvSearch("https://www.salinas.net/", lightData, false);


        printTitle("Medium data (1000 lines)");

        runCsvSearch("word", mediumData, false);
        runCsvSearch("Calderon Ltd,", mediumData, false);
        runCsvSearch("2021-03-22", mediumData, false);
        runCsvSearch("cnewman@price-morrow.com", mediumData, false);
        runCsvSearch("http://www.cox2.com/", mediumData, false);

        printTitle("Heavy data (10.000 lines)");

        runCsvSearch("word", mediumData, false);
        runCsvSearch("charlottemercer", mediumData, false);
        runCsvSearch("word", mediumData, false);
        runCsvSearch("+1-081-243-1144x34701", mediumData, false);
        runCsvSearch("EaD5472E5B3ffd8", mediumData, false);
        
    }

    public void runCsvSearch(String searchWord, String csvPath, boolean isWarmup) throws Exception {

        System.out.println("\n---------------------------------------------");
        if (isWarmup) {
            System.out.println("CsvSearchBenchmark - warmup phase");
        } else {
            System.out.println("CsvSearchBenchmark - searching for: '" + searchWord + "' in file [" + csvPath + "]");
        }

        // Run Java benchmark
        long startJava = System.nanoTime();
        Object javaMatches = javaTask.perform(searchWord, csvPath);
        long endJava = System.nanoTime();
        long durationJava = endJava - startJava;

        // Run Rust benchmark
        long startRust = System.nanoTime();
        Object rustMatches = rustTask.perform(searchWord, csvPath);
        long endRust = System.nanoTime();
        long durationRust = endRust - startRust;

        // Run Rust benchmark
        long startRustPolars = System.nanoTime();
        Object rustMatchesPolars = rustPolarsTask.perform(searchWord, csvPath);
        long endRustPolars = System.nanoTime();
        long durationRustPolars = endRustPolars - startRustPolars;

        // Print results
        System.out.println("File " + csvPath + ":");
        System.out.println(" > Java found: " + javaMatches + " (took " + durationJava + " ns)");
        System.out.println(" > Rust found: " + rustMatches + " (took " + durationRust + " ns)");
        System.out.println(" > Pola found: " + rustMatchesPolars + " (took " + durationRustPolars + " ns)");

        // Optional: Calculate and print performance difference
        double speedup = (double) durationJava / durationRust;
        System.out.println("Rust is " + String.format("%.2fx", speedup) + " faster for " + csvPath + " data");
        double speedupPolars = (double) durationJava / durationRustPolars;
        System.out.println("Rust with Polars is " + String.format("%.2fx", speedupPolars) + " faster for " + csvPath + " data");
    }

    private void printTitle(String title){
        System.out.println("\n\n===============================================");
        System.out.println("                   " + title);
        System.out.println("===============================================");
    }
}
