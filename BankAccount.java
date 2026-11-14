package person;

public abstract class BankAccount {
    private int accountNumber;
    private String ownerName;
    private long balance;
    private String[] transactions;
    private int transactionCount;

    public BankAccount(int accountNumber, String ownerName, long balance) {
        if (accountNumber <= 0) {
            throw new IllegalArgumentException(
                "Account number must be positive."
            );
        }

        if (ownerName == null || ownerName.trim().isEmpty()) {
            throw new IllegalArgumentException(
                "Owner name cannot be empty."
            );
        }

        if (ownerName.contains("|")
                || ownerName.contains("\n")
                || ownerName.contains("\r")) {
            throw new IllegalArgumentException(
                "Owner name cannot contain | or a new line."
            );
        }

        if (balance < 0 || balance > Money.MAX_BALANCE) {
            throw new IllegalArgumentException(
                "Invalid starting balance."
            );
        }

        this.accountNumber = accountNumber;
        this.ownerName = ownerName.trim();
        this.balance = balance;

        transactions = new String[10];
        transactionCount = 0;
    }

    public int getAccountNumber() {
        return accountNumber;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public long getBalance() {
        return balance;
    }

    public abstract String getAccountType();

    protected abstract long getMinimumBalance();

    public void deposit(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Deposit must be greater than zero."
            );
        }

        if (amount > Money.MAX_BALANCE - balance) {
            throw new IllegalArgumentException(
                "Balance cannot exceed "
                + Money.format(Money.MAX_BALANCE)
            );
        }

        balance += amount;

        addTransaction(
            "Deposit: " + Money.format(amount)
            + " | Balance: " + Money.format(balance)
        );
    }

    public void withdraw(long amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                "Withdrawal must be greater than zero."
            );
        }

        if (amount > balance) {
            throw new IllegalArgumentException(
                "Insufficient balance."
            );
        }

        if (balance - amount < getMinimumBalance()) {
            throw new IllegalArgumentException(
                "This account must keep "
                + Money.format(getMinimumBalance())
            );
        }

        balance -= amount;

        addTransaction(
            "Withdrawal: " + Money.format(amount)
            + " | Balance: " + Money.format(balance)
        );
    }

    void addTransaction(String text) {
        if (text == null
                || text.trim().isEmpty()
                || text.contains("\n")
                || text.contains("\r")) {
            throw new IllegalArgumentException(
                "Invalid transaction record."
            );
        }

        if (transactionCount == transactions.length) {
            String[] biggerArray =
                new String[transactions.length * 2];

            for (int i = 0; i < transactionCount; i++) {
                biggerArray[i] = transactions[i];
            }

            transactions = biggerArray;
        }

        transactions[transactionCount] = text;
        transactionCount++;
    }

    public int getTransactionCount() {
        return transactionCount;
    }

    public String getTransaction(int index) {
        if (index < 0 || index >= transactionCount) {
            throw new IllegalArgumentException(
                "Invalid transaction index."
            );
        }

        return transactions[index];
    }

    public void displayDetails() {
        System.out.println("Account number: " + accountNumber);
        System.out.println("Owner: " + ownerName);
        System.out.println("Type: " + getAccountType());
        System.out.println("Balance: " + Money.format(balance));
    }

    public void displayTransactions() {
        System.out.println(
            "Transactions for account " + accountNumber + ":"
        );

        for (int i = 0; i < transactionCount; i++) {
            System.out.println(
                (i + 1) + ". " + transactions[i]
            );
        }
    }
}