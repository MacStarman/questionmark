package login;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import login.repository.LoginRepository;

@SpringBootApplication
@EnableMongoRepositories(basePackageClasses = LoginRepository.class)
public class Application {

    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args) {
        LOGGER.info("Starting application...");
        SpringApplication.run(Application.class, args);
    }
}