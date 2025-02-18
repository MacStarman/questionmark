// Authored by Daniel McCarthy, 19350093
package service.core;

public class TransactionForm {
    public String senderAccountId;
    public String receiverAccountId;
    public double amount;
    public String message;

    public TransactionForm() {}

    public TransactionForm(String senderAccountId,
                        String receiverAccountId,
                        double amount,
                        String message) {
        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.message = message;
    }
}
