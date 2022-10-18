package com.example.care;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableJpaAuditing
@EnableScheduling
public class CareApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(CareApplication.class);
        application.addListeners(new ApplicationPidFileWriter());
        application.run(args);
    }

}
