package service.core;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.annotation.Id;

@Document(collection = "accounts")
public class AccountInfo {
    @Id
    public String accountId;
    public String email;
    public String firstName;
    public String lastName;
    public Currency currency;
    public double balance;

    public AccountInfo(String firstName, String lastName,String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.balance = 0;
    }

    public AccountInfo() {
    }
}
