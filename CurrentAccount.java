package person;

public class CurrentAccount extends BankAccount {

    public CurrentAccount(
            int accountNumber,
            String ownerName,
            long balance) {

        super(accountNumber, ownerName, balance);
    }

    @Override
    public String getAccountType() {
        return "CURRENT";
    }

    @Override
    protected long getMinimumBalance() {
        return 0;
    }
}