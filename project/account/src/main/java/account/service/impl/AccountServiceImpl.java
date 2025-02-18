package account.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.core.AccountInfo;
import account.repository.AccountsRepository;
import account.service.AccountService;

import java.util.List;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger LOGGER = LogManager.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountsRepository accountsRepositoryDAO;

    @Override
    public List<AccountInfo> getAllAccountInfo() {
        LOGGER.debug("Getting all accounts");
        return accountsRepositoryDAO.findAll();
    }

    @Override
    public AccountInfo getAccountInfoById(String accountId){
        LOGGER.debug("Getting account by id: " + accountId);
        return accountsRepositoryDAO.findById(accountId).orElse(null);
    }

    @Override
    public AccountInfo createAccount(AccountInfo accountInfo){
        LOGGER.debug("Creating account: " + accountInfo);
        return accountsRepositoryDAO.save(accountInfo);
    }

    @Override
    public AccountInfo updateAccountInfo(AccountInfo accountInfo){
        LOGGER.debug("Updating account: " + accountInfo.accountId);
        String id = accountInfo.accountId;

        if(accountsRepositoryDAO.existsById(id)){
            return accountsRepositoryDAO.save(accountInfo);
        } else {
            return null;
        }
    }

    @Override
    public void deleteAccountInfoById(String accountId){
        LOGGER.debug("Deleting account by accountId: " + accountId);
        accountsRepositoryDAO.deleteById(accountId);
    }
}
