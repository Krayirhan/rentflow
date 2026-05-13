package com.rentflow.exception;

public class ConsoleInputClosedException extends RuntimeException {

    public ConsoleInputClosedException() {
        super("Console input kapandi.");
    }
}
