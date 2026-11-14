package person;



import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class main {
    private static final String DATA_FILE = "bank-data.txt";
    private static Scanner input = new Scanner(System.in);
    private static Bank bank = new Bank();

    public static void main(String[] args) {
        try {
            bank.loadFromFile(DATA_FILE);

        } catch (IOException e) {
            System.out.println(e.getMessage());

            System.out.println(
                "Startup stopped. Your existing data file was not overwritten."
            );

            return;
        }

        System.out.println("STUDENT BANKING SYSTEM");
        System.out.println(
            "Loaded accounts: " + bank.getAccountCount()
        );

        boolean running = true;

        while (running) {
            displayMenu();

            try {
                int choice = readInt("Choose an option: ");

                switch (choice) {
                    case 1:
                        createAccount();
                        bank.saveToFile(DATA_FILE);
                        break;

                    case 2:
                        deposit();
                        bank.saveToFile(DATA_FILE);
                        break;

                    case 3:
                        withdraw();
                        bank.saveToFile(DATA_FILE);
                        break;

                    case 4:
                        selectAccount().displayDetails();
                        break;

                    case 5:
                        selectAccount().displayTransactions();
                        break;

                    case 6:
                        bank.displayAllAccounts();
                        break;

                    case 0:
                        bank.saveToFile(DATA_FILE);
                        System.out.println("Data saved. Goodbye!");
                        running = false;
                        break;

                    default:
                        System.out.println(
                            "Please choose an option from 0 to 6."
                        );
                }

            } catch (IllegalArgumentException e) {
                System.out.println("Error: " + e.getMessage());

            } catch (IOException e) {
                System.out.println(
                    "Saving failed: " + e.getMessage()
                );

                System.out.println(
                    "Changes are still in memory. "
                    + "Fix the file problem and choose 0 to retry."
                );

            } catch (NoSuchElementException e) {
                try {
                    bank.saveToFile(DATA_FILE);

                    System.out.println(
                        "\nInput closed. Data saved."
                    );

                } catch (IOException saveError) {
                    System.out.println(
                        "\nInput closed. Could not save: "
                        + saveError.getMessage()
                    );
                }

                running = false;
            }
        }
    }

    private static void displayMenu() {
        System.out.println("\n1. Create account");
        System.out.println("2. Deposit money");
        System.out.println("3. Withdraw money");
        System.out.println("4. View account and balance");
        System.out.println("5. View transaction history");
        System.out.println("6. View all accounts");
        System.out.println("0. Save and exit");
    }

    private static void createAccount() {
        System.out.println(
            "1. Current account - no minimum balance"
        );

        System.out.println(
            "2. Savings account - minimum balance $10.00"
        );

        int type = readInt("Account type: ");

        System.out.print("Owner name: ");
        String name = input.nextLine();

        long initialBalance = readMoney("Starting balance: ");

        BankAccount account = bank.createAccount(
            type, name, initialBalance
        );

        System.out.println(
            "Account created. Number: " + account.getAccountNumber()
        );
    }

    private static BankAccount selectAccount() {
        return bank.findAccount(
            readInt("Account number: ")
        );
    }

    private static void deposit() {
        BankAccount account = selectAccount();

        account.deposit(
            readMoney("Deposit amount: ")
        );

        System.out.println(
            "Deposit successful. Balance: "
            + Money.format(account.getBalance())
        );
    }

    private static void withdraw() {
        BankAccount account = selectAccount();

        account.withdraw(
            readMoney("Withdrawal amount: ")
        );

        System.out.println(
            "Withdrawal successful. Balance: "
            + Money.format(account.getBalance())
        );
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Integer.parseInt(
                    input.nextLine().trim()
                );

            } catch (NumberFormatException e) {
                System.out.println(
                    "Please enter a whole number."
                );
            }
        }
    }

    private static long readMoney(String message) {
        while (true) {
            System.out.print(message);

            try {
                return Money.parse(input.nextLine());

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}