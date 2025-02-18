package balance.controller;


import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import service.core.BalanceForm;
import service.core.AccountInfo;

@RestController
public class BalanceController {
    private static final Logger LOGGER = LogManager.getLogger(BalanceController.class);

    @Value("${server.port}")
    private int port;

    @Value("${account.service.uri}")
    private String accountServiceURI;

    @PostMapping(value = "/balance/add", produces = "application/json")
    public ResponseEntity<AccountInfo> addToBalance(@RequestBody BalanceForm form) {
        LOGGER.info("Adding " + form.amount + " to account " + form.accountId);
        RestTemplate template = new RestTemplate();

        ResponseEntity<AccountInfo> accountResponse = template.getForEntity(accountServiceURI + "/accounts/" +
                form.accountId, AccountInfo.class);

        AccountInfo account = accountResponse.getBody();

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            account.balance += form.amount;
            template.put(accountServiceURI + "/accounts/" + form.accountId, account);
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    @PostMapping(value="/balance/subtract", produces="application/json")
    public ResponseEntity<AccountInfo> subtractFromBalance(@RequestBody BalanceForm form) {
        LOGGER.debug("Subtracting " + form.amount + " from account " + form.accountId);
        RestTemplate template = new RestTemplate();

        ResponseEntity<AccountInfo> accountResponse = template.getForEntity(accountServiceURI + "/accounts/" +
                form.accountId, AccountInfo.class);

        AccountInfo account = accountResponse.getBody();

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            if(account.balance < form.amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            
            account.balance -= form.amount;
            template.put(accountServiceURI + "/accounts/" + form.accountId, account);
        }

        return ResponseEntity.status(HttpStatus.OK).body(account);
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
