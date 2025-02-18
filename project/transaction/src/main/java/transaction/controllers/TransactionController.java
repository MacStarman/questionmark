package transaction.controllers;


import service.core.Transaction;
import service.core.TransactionForm;
import service.core.BalanceForm;
import service.core.AccountInfo;
import transaction.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;
import java.util.Comparator;

@RestController
public class TransactionController {
    
    @Autowired
    private TransactionService transactionService;

    @Value("${account.service.uri}")
    private String accountServiceURI;

    @Value("${balance.service.uri}")
    private String balanceServiceURI;

    @Value("${server.port}")
    private int port;

    @GetMapping(value="/transactions", produces="application/json")
    public ResponseEntity<List<Transaction>> getTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transactions);
    }

    @PostMapping(value="/transactions", produces="application/json")
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionForm form) {
        RestTemplate template = new RestTemplate();
        
        ResponseEntity<AccountInfo> senderResponse = template.getForEntity(accountServiceURI + "/accounts/" +
                form.senderAccountId, AccountInfo.class);

        AccountInfo senderAccount = senderResponse.getBody();

        if(senderAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }else{
            if(senderAccount.balance < form.amount) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        }

        ResponseEntity<AccountInfo> receiverResponse = template.getForEntity(accountServiceURI + "/accounts/" +
                form.receiverAccountId, AccountInfo.class);

        AccountInfo receiverAccount = receiverResponse.getBody();
        
        if(receiverAccount == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        Transaction transaction = new Transaction(form.senderAccountId, form.receiverAccountId, form.amount, form.message);
        transactionService.createTransaction(transaction);

        BalanceForm receiverBalanceForm = new BalanceForm(form.receiverAccountId, form.amount);

        template.postForEntity(balanceServiceURI + "/balance/add", 
                                receiverBalanceForm, 
                                BalanceForm.class);

        BalanceForm senderBalanceForm = new BalanceForm(form.senderAccountId, form.amount);

        template.postForEntity(balanceServiceURI + "/balance/subtract", 
                                senderBalanceForm, 
                                BalanceForm.class);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .header("Location", getHost() + "/transactions/" + transaction.transactionId)
                .body(transaction);
    }

    @DeleteMapping(value = "/transactions/{id}")
    public ResponseEntity<Transaction> refundTransaction(@PathVariable String id) {
        RestTemplate template = new RestTemplate();

        Transaction transaction = transactionService.getTransactionById(id);

        if(transaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        BalanceForm receiverBalanceForm = new BalanceForm(transaction.receiverAccountId, transaction.amount);

        template.postForEntity(balanceServiceURI + "/balance/add", 
                                receiverBalanceForm, 
                                BalanceForm.class);

        BalanceForm senderBalanceForm = new BalanceForm(transaction.senderAccountId, transaction.amount);

        template.postForEntity(balanceServiceURI + "/balance/subtract", 
                                senderBalanceForm, 
                                BalanceForm.class);

        transactionService.deleteTransactionById(id);

        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @GetMapping(value="/transactions/{id}", produces="application/json")
    public ResponseEntity<Transaction> getTransaction(@PathVariable String id) {
        Transaction transaction = transactionService.getTransactionById(id);

        if(transaction == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(transaction);
    }

    @GetMapping(value="/transactions/listTo/{id}", produces="application/json")
    public ResponseEntity<List<Transaction>> listTransactionsTo(@PathVariable String id) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<AccountInfo> response = template.getForEntity(accountServiceURI + "/accounts/" + id, AccountInfo.class);
        AccountInfo account = response.getBody();

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        List<Transaction> transactions = transactionService.getTransactionsByReceiverAccountId(id);
        return ResponseEntity.status(HttpStatus.FOUND).body(transactions);
    }

    @GetMapping(value="/transactions/listFrom/{id}", produces="application/json")
    public ResponseEntity<List<Transaction>> listTransactionsFrom(@PathVariable String accountId) {
        RestTemplate template = new RestTemplate();
        ResponseEntity<AccountInfo> response = template.getForEntity(accountServiceURI + "/accounts/" + accountId, AccountInfo.class);
        AccountInfo account = response.getBody();

        if(account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        
        List<Transaction> transactions = transactionService.getTransactionsBySenderAccountId(accountId);
        return ResponseEntity.status(HttpStatus.FOUND).body(transactions);
    }

    static class SortbyTime implements Comparator<Transaction>
    {
        public int compare(Transaction a, Transaction b)
        {
            if(a.timestamp.after(b.timestamp)) return -1;

            if(a.timestamp.before(b.timestamp)) return 1;

            return 0;
        }
    }

    private String getHost() {
        try {
            return InetAddress.getLocalHost().getHostAddress() + ":" + port;
        } catch (UnknownHostException e) {
            return "localhost:" + port;
        }
    }
}
