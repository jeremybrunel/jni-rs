
public class Benchmark {

    public static void main(String[] args) {
        
        CsvSearchBenchmark csvBearchBench = new CsvSearchBenchmark();
        try{
            csvBearchBench.run();
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
