package account.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import service.core.AccountInfo;

@Repository
public interface AccountsRepository extends MongoRepository<AccountInfo, String>{
}
