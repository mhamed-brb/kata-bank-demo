package kata.bank.demo;

import kata.bank.model.Transaction;
import kata.bank.service.OperationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class KataBankDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(KataBankDemoApplication.class, args);
    }

}
