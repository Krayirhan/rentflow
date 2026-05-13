package com.rentflow;

import com.rentflow.app.Main;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RentFlowConsoleEndToEndTest {

    @Test
    void mainConsoleFlowShouldProduceExpectedOutput() {
        String simulatedInput = String.join(System.lineSeparator(),
                "1",
                "7",
                "5",
                "C-1",
                "34ABC123",
                "2026-05-10",
                "2026-05-13",
                "12",
                "6",
                "34ABC123",
                "2026-05-14",
                "13",
                "15",
                "0"
        ) + System.lineSeparator();

        InputStream originalIn = System.in;
        PrintStream originalOut = System.out;
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            System.setIn(new ByteArrayInputStream(simulatedInput.getBytes(StandardCharsets.UTF_8)));
            System.setOut(new PrintStream(outputStream, true, StandardCharsets.UTF_8));

            Main.main(new String[]{});
        } finally {
            System.setIn(originalIn);
            System.setOut(originalOut);
        }

        String output = outputStream.toString(StandardCharsets.UTF_8);

        assertTrue(output.contains("Tum musteriler:"));
        assertTrue(output.contains("Arac basariyla kiralandi."));
        assertTrue(output.contains("Aktif kiralamalar:"));
        assertTrue(output.contains("Arac basariyla iade edildi."));
        assertTrue(output.contains("Tamamlanan kiralamalar:"));
        assertTrue(output.contains("Final ucret: 4800.0"));
        assertTrue(output.contains("Toplam gelir: 4800.0"));
        assertTrue(output.contains("Programdan cikiliyor..."));
    }
}
