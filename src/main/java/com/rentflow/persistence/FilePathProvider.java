package com.rentflow.persistence;

import java.nio.file.Files;
import java.nio.file.Path;

public class FilePathProvider {

    private final Path baseDirectory;

    public FilePathProvider(Path baseDirectory) {
        this.baseDirectory = baseDirectory;
    }

    public Path getVehiclesFilePath() {
        return resolve("vehicles.csv");
    }

    public Path getCustomersFilePath() {
        return resolve("customers.csv");
    }

    public Path getRentalsFilePath() {
        return resolve("rentals.csv");
    }

    private Path resolve(String fileName) {
        try {
            Files.createDirectories(baseDirectory);
        } catch (Exception exception) {
            throw new IllegalStateException("Veri klasoru olusturulamadi: " + baseDirectory, exception);
        }

        return baseDirectory.resolve(fileName);
    }
}
