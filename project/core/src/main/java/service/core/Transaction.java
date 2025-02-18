package service.core;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;

@Document(collection = "transactions")
public class Transaction {

    public Transaction(
            String senderAccountId, 
            String receiverAccountId,
            double amount,
            String message) {

        this.senderAccountId = senderAccountId;
        this.receiverAccountId = receiverAccountId;
        this.amount = amount;
        this.message = message;
        
        this.timestamp = new Date(System.currentTimeMillis());
    }
    
    public Transaction(
            String senderAccountId, 
            String receiverAccountId,
            double amount) {

        this(senderAccountId, receiverAccountId, amount, new String(""));
    }

    public Transaction() {}

    @Id
    public String transactionId;
    public Date timestamp;     // Set on successful transaction
    public String senderAccountId;
    public String receiverAccountId;
    public double amount;
    public String message;
}
