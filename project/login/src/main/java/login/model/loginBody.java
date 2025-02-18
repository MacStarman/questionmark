package login.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "login")
public class loginBody {

    @Id
    private String email;

    private String password;

    public loginBody(String email,String password){
        this.email = email;
        this.password = password;
    }
    public String getEmail(){return this.email;}
    public String getPassword(){return this.password;}
    
}