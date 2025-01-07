import interfaces.BenchmarkTask;

public class JavaSearchTask implements BenchmarkTask {

    @Override
    public Object perform(Object... args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Requires at least 2 arguments: haystack, needle");
        }
        
        // 1) Cast the arguments to Strings (or handle them differently if needed)
        String haystack = (String) args[0];
        String needle   = (String) args[1];
        
        // 2) Perform the search
        boolean found = haystack.contains(needle);
        
        // 3) Return a Boolean (but the interface says Object, so that's fine)
        return found;
    }
}
