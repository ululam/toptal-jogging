package com.toptal.entrance.alexeyz;

import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import com.toptal.entrance.alexeyz.repo.JoggingRepository;
import com.toptal.entrance.alexeyz.repo.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(JoggingRepository jogRepository, UserRepository userRepository) {
		return (args) -> {
			jogRepository.save(new Jog(new Date(), 99, 10));
			jogRepository.save(new Jog(new Date(), 20, 33));
			jogRepository.save(new Jog(new Date(), 1100, 420));
			// fetch all customers
			log.info("Joggings found with findAll():");
			log.info("-------------------------------");
			for (Jog jog : jogRepository.findAll()) {
				log.info(jog.toString());
			}
			log.info("");

			userRepository.save(new User("admin", "admin", User.Role.admin, new Date()));
		};
	}

}
