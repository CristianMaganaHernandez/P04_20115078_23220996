package com.mycompany.onlineshopping;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

public class PizzaPlanetMainTest {

    private InputStream originalIn;

    @BeforeEach
    public void setUp() {
        originalIn = System.in; // Save the original System.in stream
    }

    @Test
    public void testMain() {
        // Simulate inputs for the entire program flow: name, address, menu selection, and checkout
        String simulatedInput = "John Doe\n123 Main St\n1\nx\n";  // Selecting the first item and then exiting with 'x'
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        assertDoesNotThrow(() -> PizzaPlanetMain.main(null), "main should not throw any exceptions");

        System.setIn(originalIn); // Restore the original input stream
    }

    @Test
    public void testGetValidInput() {
        String simulatedInput = "Pizza Planet\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner scanner = new Scanner(System.in);
        String prompt = "Please enter a valid input: ";
        String result = PizzaPlanetMain.getValidInput(scanner, prompt);

        assertEquals("Pizza Planet", result, "The input should be 'Pizza Planet'");

        System.setIn(originalIn);
    }

    @Test
    public void testGetValidInputWithEmptyInput() {
        String simulatedInput = "\nJohn Doe\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner scanner = new Scanner(System.in);
        String prompt = "Please enter a valid input: ";
        String result = PizzaPlanetMain.getValidInput(scanner, prompt);

        assertEquals("John Doe", result, "The input should skip empty entries and return 'John Doe'");

        System.setIn(originalIn);
    }

    @Test
    public void testGetValidInputMultipleEmptyEntries() {
        String simulatedInput = "\n\n\nJane Doe\n";
        System.setIn(new ByteArrayInputStream(simulatedInput.getBytes()));

        Scanner scanner = new Scanner(System.in);
        String prompt = "Please enter a valid input: ";
        String result = PizzaPlanetMain.getValidInput(scanner, prompt);

        assertEquals("Jane Doe", result, "The input should skip multiple empty entries and return 'Jane Doe'");

        System.setIn(originalIn);
    }

    @Test
    public void testGetValidInputLongInput() {
        String longInput = "ThisIsAReallyLongNameWithMultipleWordsToSimulateUserTyping\n";
        System.setIn(new ByteArrayInputStream(longInput.getBytes()));

        Scanner scanner = new Scanner(System.in);
        String prompt = "Please enter a valid input: ";
        String result = PizzaPlanetMain.getValidInput(scanner, prompt);

        assertEquals("ThisIsAReallyLongNameWithMultipleWordsToSimulateUserTyping", result, "The input should be processed correctly even if it is long");

        System.setIn(originalIn);
    }
}
