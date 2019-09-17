package br.com.marcelonidal.crud.services;

import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.marcelonidal.crud.domain.Pedido;

public interface EmailService {

	// PLAIN TEXT
	void sendOrderConfirmationEmail(Pedido obj);

	void sendEmail(SimpleMailMessage msg);

	// HTML TEMPLATE
	void sendOrderConfirmationHtmlEmail(Pedido obj);

	void sendHtmlEmail(MimeMessage msg);
}
