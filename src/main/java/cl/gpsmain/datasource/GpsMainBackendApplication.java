package cl.gpsmain.datasource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class GpsMainBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(GpsMainBackendApplication.class, args);
    }

}
