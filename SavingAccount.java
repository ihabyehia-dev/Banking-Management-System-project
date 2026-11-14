package person;

public class SavingAccount extends BankAccount {
    public static final long MINIMUM_BALANCE = 1000L;

    public SavingAccount(
            int accountNumber,
            String ownerName,
            long balance) {

        super(accountNumber, ownerName, balance);

        if (balance < MINIMUM_BALANCE) {
            throw new IllegalArgumentException(
                "A savings account needs at least "
                + Money.format(MINIMUM_BALANCE)
            );
        }
    }

    @Override
    public String getAccountType() {
        return "SAVINGS";
    }

    @Override
    protected long getMinimumBalance() {
        return MINIMUM_BALANCE;
    }
}