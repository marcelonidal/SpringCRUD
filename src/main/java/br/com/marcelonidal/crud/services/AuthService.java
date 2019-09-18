package br.com.marcelonidal.crud.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.marcelonidal.crud.domain.Cliente;
import br.com.marcelonidal.crud.repositories.ClienteRepository;
import br.com.marcelonidal.crud.services.exceptions.ObjectNotFoundException;

@Service
public class AuthService {

	@Autowired
	private ClienteRepository clienteRepo;
	@Autowired
	private BCryptPasswordEncoder encoder;
	@Autowired
	private EmailService emailService;

	private Random rand = new Random();

	public void sendNewPassword(String email) {
		Cliente cli = clienteRepo.findByEmail(email);
		if (cli == null) {
			throw new ObjectNotFoundException("Email nao encontrado");
		}
		String newPass = newPassword();
		cli.setSenha(encoder.encode(newPass));
		clienteRepo.save(cli);
		emailService.sendNewPasswordEmail(cli, newPass);
	}

	private String newPassword() {
		char[] vetor = new char[10];
		for (int i = 0; i < 10; i++) {
			vetor[i] = randomChar();
		}
		return new String(vetor);
	}

	private char randomChar() {
		// USA A TABELA UNICODE
		int opt = rand.nextInt(3);
		if (opt == 0) {
			// GERA DIGITO
			return (char) (rand.nextInt(10) + 48);
		} else if (opt == 1) {
			// GERA MAIUSCULA
			return (char) (rand.nextInt(26) + 65);
		} else {
			// GERA MINUSCULA
			return (char) (rand.nextInt(26) + 97);
		}
	}
	
}
