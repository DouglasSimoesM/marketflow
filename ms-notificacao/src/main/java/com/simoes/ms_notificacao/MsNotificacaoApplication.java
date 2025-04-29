package com.simoes.ms_notificacao;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication
@EnableMongoRepositories
public class MsNotificacaoApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsNotificacaoApplication.class, args);
	}

}
