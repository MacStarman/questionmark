package login.repository;


import org.springframework.stereotype.Repository;
import org.springframework.data.mongodb.repository.MongoRepository;
import login.model.loginBody;

@Repository
public interface LoginRepository extends MongoRepository<loginBody, String>{
}
