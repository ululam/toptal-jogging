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
import java.util.TimeZone;

@SpringBootApplication
public class Application {

	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(JoggingRepository jogRepository, UserRepository userRepository) {
		return (args) -> {
			User user = userRepository.save(new User("admin", "admin", User.Role.admin, new Date()));

			jogRepository.save(new Jog(user.getId(), new Date(), 3, 13*60_000));
			jogRepository.save(new Jog(user.getId(), new Date(), 10, 3_600_000));
			jogRepository.save(new Jog(user.getId(), new Date(), 42, 12_740_000));
			// fetch all customers
			log.info("Joggings found with findAll():");
			log.info("-------------------------------");
			for (Jog jog : jogRepository.findAll()) {
				log.info(jog.toString());
			}
			log.info("");

		};
	}

}
