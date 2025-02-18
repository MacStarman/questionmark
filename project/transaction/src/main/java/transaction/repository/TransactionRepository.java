package transaction.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import service.core.Transaction;

import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    public List<Transaction> findBySenderAccountId(String senderAccountId);

    public List<Transaction> findByReceiverAccountId(String receiverAccountId);
}
