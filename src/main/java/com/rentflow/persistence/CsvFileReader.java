package com.rentflow.persistence;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CsvFileReader {

    public List<List<String>> read(Path path) {
        ensureFileExists(path);

        try {
            List<String> lines = Files.readAllLines(path);

            if (lines.size() <= 1) {
                return Collections.emptyList();
            }

            List<List<String>> rows = new ArrayList<>();

            for (int index = 1; index < lines.size(); index++) {
                String line = lines.get(index);
                if (!line.isBlank()) {
                    rows.add(parseLine(line));
                }
            }

            return rows;
        } catch (IOException exception) {
            throw new IllegalStateException("CSV dosyasi okunamadi: " + path, exception);
        }
    }

    private void ensureFileExists(Path path) {
        try {
            if (Files.notExists(path)) {
                if (path.getParent() != null) {
                    Files.createDirectories(path.getParent());
                }
                Files.createFile(path);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("CSV dosyasi olusturulamadi: " + path, exception);
        }
    }

    private List<String> parseLine(String line) {
        List<String> fields = new ArrayList<>();
        StringBuilder currentValue = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char currentChar = line.charAt(index);

            if (currentChar == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    currentValue.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (currentChar == ',' && !inQuotes) {
                fields.add(currentValue.toString());
                currentValue.setLength(0);
            } else {
                currentValue.append(currentChar);
            }
        }

        fields.add(currentValue.toString());
        return fields;
    }
}
