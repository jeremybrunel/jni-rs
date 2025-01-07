package interfaces;

public interface BenchmarkTask {
    /**
     * Perform a benchmarked task with arbitrary arguments.
     * @param args variable-length array of Objects (any type).
     * @return an Object that can represent any result type.
     */
    Object perform(Object... args) throws Exception ;
}
