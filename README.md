# Banking Management System

A Java console project to practice object-oriented programming and manage basic bank accounts.

## Features

- Create current or savings accounts with unique account numbers
- Deposit and withdraw amounts with validation
- View account details, balances, and transaction history
- Save records to a local text file and load them on startup
- Report invalid amounts and handle common file errors

## Object-oriented design

- `BankAccount` is an **abstract class** containing shared account behavior.
- `CurrentAccount` and `SavingAccount` extend `BankAccount`.
- `Bank` manages accounts using a fixed-size `BankAccount[]` array (up to 100 accounts).
- `Money` converts entered dollar amounts to integer cents to avoid floating-point balance rounding.
- `main` provides the console menu and user input.

This project demonstrates **classes, inheritance, encapsulation, polymorphism, arrays, exceptions, and file handling**.

## Files

`Bank.java`, `BankAccount.java`, `CurrentAccount.java`, `SavingAccount.java`, `Money.java`, and `main.java`.

All source files use the Java package `person`.

## How to run

Requires a Java Development Kit (JDK 8 or later).

Open a terminal in the repository folder and run:

```bash
javac -d out *.java
java -cp out person.main
```

In Eclipse, create a Java project, make a package named `person`, import the six source files into that package, then run the `main` class.

## Try it

1. Create a current or savings account.
2. Deposit and withdraw money.
3. View the balance and transaction history.
4. Choose Save and Exit.
5. Launch the application again to see the saved account data.

Saved information is kept in `bank-data.txt` in the working directory. Avoid committing personal or sensitive information.

## Limitations

This is a local academic simulation, not real banking software. It has no login system or external database, and it stores at most 100 accounts.
