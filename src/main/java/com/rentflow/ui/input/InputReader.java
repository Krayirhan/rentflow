package com.rentflow.ui.input;

import com.rentflow.exception.ConsoleInputClosedException;

import java.util.NoSuchElementException;
import java.util.Scanner;

public class InputReader {

    private final Scanner scanner;

    public InputReader() {
        this.scanner = new Scanner(System.in);
    }

    public String readString(String message) {
        System.out.print(message);

        try {
            return scanner.nextLine();
        } catch (NoSuchElementException exception) {
            throw new ConsoleInputClosedException();
        }
    }

    public int readInt(String message) {
        return Integer.parseInt(readString(message));
    }

    public double readDouble(String message) {
        return Double.parseDouble(readString(message));
    }

    public boolean readYesNo(String message) {
        String answer = readString(message).trim().toLowerCase();

        if (answer.equals("e") || answer.equals("evet")) {
            return true;
        }

        if (answer.equals("h") || answer.equals("hayir")) {
            return false;
        }

        throw new IllegalArgumentException("Bu alan icin e/h girilmelidir.");
    }
}
