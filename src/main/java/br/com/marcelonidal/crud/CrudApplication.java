package br.com.marcelonidal.crud;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudApplication implements CommandLineRunner {
	
	public static void main(String[] args) {
		SpringApplication.run(CrudApplication.class, args);
	}

	@Override // testes de inicializacao de banco podem ser feitos aqui
	public void run(String... args) throws Exception {
		
	}

}
