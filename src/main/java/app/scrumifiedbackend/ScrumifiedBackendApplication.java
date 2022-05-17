package app.scrumifiedbackend;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@OpenAPIDefinition
@EnableScheduling
public class ScrumifiedBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScrumifiedBackendApplication.class, args);
    }

}
