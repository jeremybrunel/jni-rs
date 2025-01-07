import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import interfaces.BenchmarkTask;

public class JavaCsvSearchTask implements BenchmarkTask {

    @Override
    public Object perform(Object... args) throws Exception {
        if (args.length < 2) {
            throw new IllegalArgumentException("Need the searchWord argument and csv path.");
        }

        String searchWord = (String) args[0];
        String csvPath = (String) args[1];
        List<String> matches = new ArrayList<>();

        try {
            String content = new String(Files.readAllBytes(Paths.get(csvPath)));
            String[] lines = content.split("\n");

            for (int rowIdx = 0; rowIdx < lines.length; rowIdx++) {
                String line = lines[rowIdx];
                int fieldStart = 0;
                boolean inQuotes = false;
                int colIdx = 0;

                for (int i = 0; i < line.length(); i++) {
                    char c = line.charAt(i);
                    switch (c) {
                        case '"':
                            inQuotes = !inQuotes;
                            if (!inQuotes) {
                                String field = line.substring(fieldStart, i).trim().replace("\"", "");
                                if (field.contains(searchWord)) {
                                    matches.add(String.format("(%d, %d)", rowIdx, colIdx));
                                }
                                fieldStart = i + 1;
                            } else {
                                fieldStart = i + 1;
                            }
                            break;
                        case ',':
                            if (!inQuotes) {
                                String field = line.substring(fieldStart, i).trim().replace("\"", "");
                                if (field.contains(searchWord)) {
                                    matches.add(String.format("(%d, %d)", rowIdx, colIdx));
                                }
                                fieldStart = i + 1;
                                colIdx++;
                            }
                            break;
                    }
                }

                // Check last field
                if (fieldStart < line.length()) {
                    String field = line.substring(fieldStart).trim().replace("\"", "");
                    if (field.contains(searchWord)) {
                        matches.add(String.format("(%d, %d)", rowIdx, colIdx));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return matches;
    }
}
