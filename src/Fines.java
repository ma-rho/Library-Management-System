import java.util.InputMismatchException;
import java.util.Scanner;

public class Fines {

    // Function to calculate and manage overdue fines
    public static void manageFine() {
        Scanner scanner = new Scanner(System.in);
        double finePerDay = 0.30;
        int daysLate;
        double totalFine;
        double amountPaid;

        // Validate the number of days late input
        while (true) {
            try {
                System.out.print("Enter the number of days the book is late: ");
                daysLate = scanner.nextInt();
                if (daysLate < 0) {
                    System.out.println("Error: The number of days cannot be negative. Please enter a valid number.");
                    continue;
                }
                if (daysLate == 0) {
                    System.out.println("You have no fine as the book is not late.");
                    return;
                }

                // Calculate the fine if the book is overdue
                totalFine = daysLate * finePerDay;
                totalFine = Math.round(totalFine * 100.0) / 100.0;
                System.out.println("The total fine for " + daysLate + " days late is: £" + totalFine);
                break;

            } catch (InputMismatchException e) { // Handle invalid input type
                System.out.println("Error: Invalid input. Please enter a valid number for days late.");
                scanner.nextLine();
            }
        }

        // Validate the amount paid input
        while (true) {
            try {
                System.out.print("Enter the amount you can pay: £");
                amountPaid = scanner.nextDouble();
                if (amountPaid < 0) {
                    System.out.println("Error: The amount paid cannot be negative. Please enter a valid amount.");
                    continue;
                }
                break;
            } catch (InputMismatchException e) {  // Handle invalid input type
                System.out.println("Error: Invalid input. Please enter a valid numeric value for the amount.");
                scanner.nextLine();
            }
        }

        // Remaining fine
        totalFine -= amountPaid;
        totalFine = Math.round(totalFine * 100.0) / 100.0;
        if (totalFine <= 0) {
            System.out.println("Your fine has been cleared. Thank you for your payment.");
        } else {
            System.out.println("You still owe £" + totalFine + " for the overdue book.");
        }
        scanner.close();
    }

    public static void main(String[] args) {
        manageFine();
    }
}