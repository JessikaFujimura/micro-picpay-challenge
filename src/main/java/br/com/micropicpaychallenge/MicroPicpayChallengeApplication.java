package br.com.micropicpaychallenge;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.config.TopicBuilder;

@SpringBootApplication
public class MicroPicpayChallengeApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroPicpayChallengeApplication.class, args);
	}

	@Bean
	NewTopic notificationTopic() {
		return TopicBuilder.name("transaction-notification").build();
	}

}
