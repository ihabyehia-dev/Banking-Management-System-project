package person;

public class Money {
    public static final long MAX_BALANCE = 100000000L;

    public static long parse(String text) {
        if (text == null
                || !text.trim().matches("[0-9]+(\\.[0-9]{1,2})?")) {

            throw new IllegalArgumentException(
                "Enter an amount like 100 or 100.50."
            );
        }

        String[] parts = text.trim().split("\\.");

        try {
            long dollars = Long.parseLong(parts[0]);

            if (dollars > MAX_BALANCE / 100) {
                throw new IllegalArgumentException(
                    "The maximum amount is " + format(MAX_BALANCE)
                );
            }

            long cents = 0;

            if (parts.length == 2) {
                String decimalPart = parts[1];

                if (decimalPart.length() == 1) {
                    decimalPart += "0";
                }

                cents = Long.parseLong(decimalPart);
            }

            long total = dollars * 100 + cents;

            if (total > MAX_BALANCE) {
                throw new IllegalArgumentException(
                    "The maximum amount is " + format(MAX_BALANCE)
                );
            }

            return total;

        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                "The amount is too large."
            );
        }
    }

    public static String format(long cents) {
        long dollars = cents / 100;
        long remainder = cents % 100;

        return "$" + dollars + "."
            + (remainder < 10 ? "0" : "") + remainder;
    }
}