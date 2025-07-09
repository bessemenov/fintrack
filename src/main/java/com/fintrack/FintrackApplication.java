package com.fintrack;

import com.fintrack.entity.User;
import com.fintrack.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class FintrackApplication {

    public static void main(String[] args) {
        SpringApplication.run(FintrackApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(UserRepository userRepository) {
        return args -> {
            User user = User.builder()
                    .username("topuria")
                    .email("topuria@ufc.com")
                    .password("matador123")
                    .build();

            userRepository.save(user);

            System.out.println("User kaydedildi: " + user.getUsername());
        };
    }
}
