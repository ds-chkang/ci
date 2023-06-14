package medousa.test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class DateTimeInputExample {
    public static void main(String[] args) {
        // Create a Scanner object to read user input
        Scanner scanner = new Scanner(System.in);

        // Get user input for date
        System.out.print("Enter a date (yyyy-MM-dd): ");
        String dateInput = scanner.nextLine();

        // Validate and parse the user input for date
        try {
            LocalDate date = LocalDate.parse(dateInput, DateTimeFormatter.ISO_DATE);
            System.out.println("Valid date: " + date);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format!");
        }

        // Get user input for time
        System.out.print("Enter a time (HH:mm:ss): ");
        String timeInput = scanner.nextLine();

        // Validate and parse the user input for time
        try {
            LocalTime time = LocalTime.parse(timeInput, DateTimeFormatter.ISO_TIME);
            System.out.println("Valid time: " + time);
        } catch (DateTimeParseException e) {
            System.out.println("Invalid time format!");
        }

        // Close the scanner
        scanner.close();
    }
}