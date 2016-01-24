package com.toptal.entrance.alexeyz;

import com.toptal.entrance.alexeyz.db.JoggingRepository;
import com.toptal.entrance.alexeyz.db.UserRepository;
import com.toptal.entrance.alexeyz.domain.Jog;
import com.toptal.entrance.alexeyz.domain.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

import static com.toptal.entrance.alexeyz.util.Utils.*;

@SpringBootApplication
public class Application {
	private static final Logger log = LoggerFactory.getLogger(Application.class);

	public static final boolean PWD_HASH = false;

	public static void main(String[] args) {
		SpringApplication.run(Application.class);
	}

	@Bean
	public CommandLineRunner loadData(JoggingRepository jogRepository, UserRepository userRepository) {
		return (args) -> {
			User admin = userRepository.save(new User("admin", "admin", User.Role.admin, new Date()));
			User manager = userRepository.save(new User("manager", "manager", User.Role.manager, new Date()));
			User user = userRepository.save(new User("user", "user", User.Role.user, new Date()));

			jogRepository.save(new Jog(user.getId(), new Date(), 3, 13* M));
			jogRepository.save(new Jog(user.getId(), new Date(t()-D), 10, H));
			jogRepository.save(new Jog(user.getId(), new Date(t()-3*D), 42, 3*H+59*M+13*S));

			jogRepository.save(new Jog(manager.getId(), new Date(), 2, 9*M));
			jogRepository.save(new Jog(manager.getId(), new Date(t()-2*M), 1.5f, 4*M));


			jogRepository.save(new Jog(admin.getId(), new Date(t()-15*D), 1, 5*M));
			jogRepository.save(new Jog(admin.getId(), new Date(t()-14*D), 1, 5*M+19*S));
			jogRepository.save(new Jog(admin.getId(), new Date(t()-25*D), 2, 11*M+20*S));
			jogRepository.save(new Jog(admin.getId(), new Date(t()-3*M), 3, 15*M+42*S));
			jogRepository.save(new Jog(admin.getId(), new Date(t()-Y), 3, 14*M+35*S));

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
