package login.service;

import login.model.loginBody;

public interface loginService {

    public loginBody registerAccount(loginBody loginBody);

    public boolean checkPassword(loginBody loginBody);

    
    
}
