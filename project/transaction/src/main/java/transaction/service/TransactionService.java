package transaction.service;

import java.util.List;

import service.core.Transaction;

public interface TransactionService {

    List<Transaction> getAllTransactions();

    Transaction createTransaction(Transaction transaction);

    Transaction getTransactionById(String id);

    Transaction updateTransaction(Transaction transaction);

    void deleteTransactionById(String id);

    List<Transaction> getTransactionsBySenderAccountId(String senderAccountId);

    List<Transaction> getTransactionsByReceiverAccountId(String receiverAccountId);
}
