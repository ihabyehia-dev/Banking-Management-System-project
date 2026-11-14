package person;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Bank {
    private BankAccount[] accounts = new BankAccount[100];
    private int accountCount = 0;
    private int nextAccountNumber = 1001;

    public BankAccount createAccount(
            int type,
            String name,
            long initialBalance) {

        if (accountCount == accounts.length
                || nextAccountNumber == Integer.MAX_VALUE) {

            throw new IllegalArgumentException(
                "The bank has reached its account limit."
            );
        }

        BankAccount account;

        if (type == 1) {
            account = new CurrentAccount(
                nextAccountNumber, name, initialBalance
            );

        } else if (type == 2) {
            account = new SavingAccount(
                nextAccountNumber, name, initialBalance
            );

        } else {
            throw new IllegalArgumentException(
                "Choose 1 for current or 2 for savings."
            );
        }

        account.addTransaction(
            "Account opened | Balance: "
            + Money.format(initialBalance)
        );

        accounts[accountCount] = account;
        accountCount++;
        nextAccountNumber++;

        return account;
    }

    public BankAccount findAccount(int number) {
        for (int i = 0; i < accountCount; i++) {
            if (accounts[i].getAccountNumber() == number) {
                return accounts[i];
            }
        }

        throw new IllegalArgumentException("Account not found.");
    }

    public int getAccountCount() {
        return accountCount;
    }

    public void displayAllAccounts() {
        if (accountCount == 0) {
            System.out.println("No accounts yet.");
            return;
        }

        for (int i = 0; i < accountCount; i++) {
            accounts[i].displayDetails();
            System.out.println("--------------------");
        }
    }

    public void saveToFile(String filename) throws IOException {
        File file = new File(filename);
        File temporaryFile = new File(filename + ".tmp");

        try (PrintWriter writer =
                new PrintWriter(temporaryFile, "UTF-8")) {

            writer.println("BANKING_SYSTEM_V1");
            writer.println(accountCount);

            for (int i = 0; i < accountCount; i++) {
                BankAccount account = accounts[i];

                writer.println(
                    account.getAccountType() + "|"
                    + account.getAccountNumber() + "|"
                    + account.getOwnerName() + "|"
                    + account.getBalance() + "|"
                    + account.getTransactionCount()
                );

                for (int j = 0; j < account.getTransactionCount(); j++) {
                    writer.println(account.getTransaction(j));
                }
            }

            if (writer.checkError()) {
                throw new IOException(
                    "Could not write all account data."
                );
            }
        }

        Files.move(
            temporaryFile.toPath(),
            file.toPath(),
            StandardCopyOption.REPLACE_EXISTING
        );
    }

    public void loadFromFile(String filename) throws IOException {
        File file = new File(filename);

        if (!file.exists()) {
            return;
        }

        Bank loadedBank = new Bank();

        try (Scanner reader = new Scanner(file, "UTF-8")) {
            if (!reader.nextLine().equals("BANKING_SYSTEM_V1")) {
                throw new IllegalArgumentException(
                    "Incorrect file header."
                );
            }

            int count = Integer.parseInt(reader.nextLine());

            if (count < 0 || count > accounts.length) {
                throw new IllegalArgumentException(
                    "Invalid number of accounts."
                );
            }

            for (int i = 0; i < count; i++) {
                String[] parts = reader.nextLine().split("\\|", -1);

                if (parts.length != 5) {
                    throw new IllegalArgumentException(
                        "Invalid account record."
                    );
                }

                int number = Integer.parseInt(parts[1]);
                long balance = Long.parseLong(parts[3]);
                int historyCount = Integer.parseInt(parts[4]);

                if (number < 1001
                        || number == Integer.MAX_VALUE
                        || historyCount < 1) {

                    throw new IllegalArgumentException(
                        "Invalid account information."
                    );
                }

                for (int j = 0; j < loadedBank.accountCount; j++) {
                    if (loadedBank.accounts[j].getAccountNumber()
                            == number) {

                        throw new IllegalArgumentException(
                            "Duplicate account number."
                        );
                    }
                }

                BankAccount account;

                if (parts[0].equals("CURRENT")) {
                    account = new CurrentAccount(
                        number, parts[2], balance
                    );

                } else if (parts[0].equals("SAVINGS")) {
                    account = new SavingAccount(
                        number, parts[2], balance
                    );

                } else {
                    throw new IllegalArgumentException(
                        "Unknown account type."
                    );
                }

                for (int j = 0; j < historyCount; j++) {
                    account.addTransaction(reader.nextLine());
                }

                loadedBank.accounts[loadedBank.accountCount] = account;
                loadedBank.accountCount++;

                if (number >= loadedBank.nextAccountNumber) {
                    loadedBank.nextAccountNumber = number + 1;
                }
            }

            if (reader.hasNextLine()) {
                throw new IllegalArgumentException(
                    "Unexpected extra data."
                );
            }

            if (reader.ioException() != null) {
                throw reader.ioException();
            }

        } catch (IllegalArgumentException | NoSuchElementException e) {
            throw new IOException(
                "Cannot load bank data: " + e.getMessage(), e
            );
        }

        accounts = loadedBank.accounts;
        accountCount = loadedBank.accountCount;
        nextAccountNumber = loadedBank.nextAccountNumber;
    }
}