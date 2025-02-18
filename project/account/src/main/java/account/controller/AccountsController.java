package account.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.core.AccountInfo;
import account.service.AccountService;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

@RestController
public class AccountsController {
    private static final Logger LOGGER = LogManager.getLogger(AccountsController.class);

    @Autowired
    private AccountService accountService;

    @Value("${server.port}")
    private int port;

    @GetMapping(value = "/test", produces = "application/json")
    public ResponseEntity<String> test(){
        return ResponseEntity
            .status(HttpStatus.OK)
            .body("Test successful");
    }

    @GetMapping(value = "/accounts", produces = "application/json")
    public ResponseEntity<List<AccountInfo>> getAllAccounts(){
        LOGGER.info("Getting all accounts");
        return ResponseEntity.status(HttpStatus.OK)
            .body(accountService.getAllAccountInfo());
    }

    @GetMapping(value = "/accounts/{id}", produces = "application/json")
    public ResponseEntity<AccountInfo> getAccount(@PathVariable("id") String id){
        AccountInfo accountInfo = accountService.getAccountInfoById(id);

        if(accountInfo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        return ResponseEntity.status(HttpStatus.OK)
            .body(accountInfo);
    }

    @PostMapping(value = "/accounts", consumes = "application/json")
    public  ResponseEntity <AccountInfo> addAccount(@RequestBody AccountInfo newAccount){
        AccountInfo accountInfo = accountService.createAccount(newAccount);

        LOGGER.info("Account created: " + accountInfo.accountId);
        return ResponseEntity.status(HttpStatus.CREATED)
            .header("Location", "http://" + getHost() + "/accounts/" + accountInfo.accountId)
            .body(accountInfo);
    }

    @PutMapping(value = "/accounts/{accountId}", consumes = "application/json")
    public ResponseEntity<AccountInfo> updateAccount(@PathVariable("accountId") String accountId, @RequestBody AccountInfo accountInfo){
        accountInfo.accountId = accountId;
        AccountInfo updatedAccount = accountService.updateAccountInfo(accountInfo);

        if(updatedAccount == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        LOGGER.info("Account updated: " + accountId);

        return ResponseEntity.status(HttpStatus.OK)
            .header("Location", "http://" + getHost() + "/accounts/" + accountId)
            .body(updatedAccount);
    }

    @DeleteMapping(value = "/accounts/{accountId}")
    public ResponseEntity<Void> deleteAccount(@PathVariable("accountId") String accountId){
        AccountInfo accountInfo = accountService.getAccountInfoById(accountId);

        if(accountInfo == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .build();
        }

        accountService.deleteAccountInfoById(accountId);
        LOGGER.info("Account deleted: " + accountId);

        return ResponseEntity.status(HttpStatus.OK)
            .build();
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
