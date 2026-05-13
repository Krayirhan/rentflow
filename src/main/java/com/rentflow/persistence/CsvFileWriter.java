package com.rentflow.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class CsvFileWriter {

    public void write(Path path, List<String> header, List<List<String>> rows) {
        try {
            if (path.getParent() != null) {
                Files.createDirectories(path.getParent());
            }

            List<String> lines = new ArrayList<>();
            lines.add(String.join(",", header));

            for (List<String> row : rows) {
                lines.add(formatRow(row));
            }

            Files.write(path, lines);
        } catch (IOException exception) {
            throw new IllegalStateException("CSV dosyasi yazilamadi: " + path, exception);
        }
    }

    private String formatRow(List<String> row) {
        List<String> escapedValues = new ArrayList<>();

        for (String value : row) {
            escapedValues.add(escape(value));
        }

        return String.join(",", escapedValues);
    }

    private String escape(String value) {
        String safeValue = value == null ? "" : value;

        if (safeValue.contains(",") || safeValue.contains("\"")) {
            return "\"" + safeValue.replace("\"", "\"\"") + "\"";
        }

        return safeValue;
    }
}
