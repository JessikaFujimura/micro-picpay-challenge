package br.com.micropicpaychallenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

@EnableJdbcAuditing
@SpringBootApplication
public class MicroPicpayChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroPicpayChallengeApplication.class, args);
	}

}
