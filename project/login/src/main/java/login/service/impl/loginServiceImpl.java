package login.service.impl;

import java.util.List;
import login.service.loginService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import login.model.loginBody;
import login.repository.LoginRepository;

@Service
public class loginServiceImpl implements loginService {

     private static final Logger LOGGER = LogManager.getLogger(loginServiceImpl.class);
    
    @Autowired
    private LoginRepository loginRepositoryDAO;
    @Override
    public loginBody registerAccount(loginBody loginBody){
        if (loginRepositoryDAO.existsById(loginBody.getEmail())){
            return null;
        }
        else{return loginRepositoryDAO.save(loginBody);}
    }
    @Override
    public boolean checkPassword(loginBody loginBody){
        if(loginRepositoryDAO.existsById(loginBody.getEmail())){
            loginBody savedLoginBody = loginRepositoryDAO.findById(loginBody.getEmail()).orElse(null);
            if(savedLoginBody.getPassword().equals(loginBody.getPassword())){
                return true;
            }
        }
        return false;
    }
    public boolean isEmailInUse(String email){
        return loginRepositoryDAO.findById(email) != null;
    }
    //just for testing
    public List<loginBody> getAllLoginInfo(){
        return loginRepositoryDAO.findAll();
    }

   

    
}
