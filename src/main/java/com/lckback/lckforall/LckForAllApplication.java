package com.lckback.lckforall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LckForAllApplication {

    public static void main(String[] args) {
        SpringApplication.run(LckForAllApplication.class, args);
    }

}
