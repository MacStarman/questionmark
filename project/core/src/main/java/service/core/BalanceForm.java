package service.core;

public class BalanceForm {
    
    public BalanceForm(String accountId, double amount) {
        this.accountId = accountId;
        this.amount = amount;
    }

    public BalanceForm() {}
    
    public String accountId;
    public double amount;
}
