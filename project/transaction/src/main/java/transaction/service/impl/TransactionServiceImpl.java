package transaction.service.impl;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import service.core.Transaction;
import transaction.repository.TransactionRepository;
import transaction.service.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    private static final Logger LOGGER = LogManager.getLogger(TransactionServiceImpl.class);

    @Autowired
    private TransactionRepository transactionRepositoryDAO;

    public List<Transaction> getAllTransactions() {
        LOGGER.debug("Getting all transactions");
        return transactionRepositoryDAO.findAll();
    }

    public Transaction getTransactionById(String id) {
        LOGGER.debug("Getting transaction by id: " + id);
        return transactionRepositoryDAO.findById(id).orElse(null);
    }

    public Transaction createTransaction(Transaction transaction) {
        LOGGER.debug("Creating transaction: " + transaction);
        return transactionRepositoryDAO.save(transaction);
    }

    public Transaction updateTransaction(Transaction transaction) {
        LOGGER.debug("Updating transaction: " + transaction);
        String id = transaction.transactionId;

        if (transactionRepositoryDAO.existsById(id)) {
            return transactionRepositoryDAO.save(transaction);
        } else {
            return null;
        }
    }

    public void deleteTransactionById(String id) {
        LOGGER.debug("Deleting transaction by id: " + id);
        transactionRepositoryDAO.deleteById(id);
    }

    public List<Transaction> getTransactionsBySenderAccountId(String senderAccountId) {
        LOGGER.debug("Getting transactions by sender account number: " + senderAccountId);
        return transactionRepositoryDAO.findBySenderAccountId(senderAccountId);
    }

    public List<Transaction> getTransactionsByReceiverAccountId(String receiverAccountId) {
        LOGGER.debug("Getting transactions by receiver account number: " + receiverAccountId);
        return transactionRepositoryDAO.findByReceiverAccountId(receiverAccountId);
    }
    
}
