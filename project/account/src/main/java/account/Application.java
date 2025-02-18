package account;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import account.repository.AccountsRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = AccountsRepository.class)
public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Starting application...");
        SpringApplication.run(Application.class, args);
    }
}