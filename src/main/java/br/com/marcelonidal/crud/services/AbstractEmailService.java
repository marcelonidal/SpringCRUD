package br.com.marcelonidal.crud.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.marcelonidal.crud.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	@Autowired
	private TemplateEngine templateEng;
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	@Value("${default.sender}")
	private String sender;
	
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {
		SimpleMailMessage smm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(smm);
	}

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {
		SimpleMailMessage smm = new SimpleMailMessage();
		smm.setTo(obj.getCliente().getEmail());
		smm.setFrom(sender);
		smm.setSubject("Pedido confirmado! Código: " + obj.getId());
		smm.setSentDate(new Date(System.currentTimeMillis()));
		smm.setText(obj.toString());
		
		return smm;
	}
	
	protected String htmlFromTemplatePedido(Pedido obj) {
		// THYMELEAF
		Context context = new Context();
		context.setVariable("pedido", obj);
		return templateEng.process("email/confirmacaoPedido", context);
	}
	
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {
		MimeMessage mm;
		try {
			mm = prepareMimeMailMessageFromPedido(obj);
			sendHtmlEmail(mm);
		} catch (MessagingException e) {
			e.getCause();
			sendOrderConfirmationEmail(obj);
		}
	}

	protected MimeMessage prepareMimeMailMessageFromPedido(Pedido obj) throws MessagingException {
		MimeMessage mm = javaMailSender.createMimeMessage();
		MimeMessageHelper mmh = new MimeMessageHelper(mm, true);
		mmh.setTo(obj.getCliente().getEmail());
		mmh.setFrom(sender);
		mmh.setSubject("Pedido confirmado! Codigo: " + obj.getId());
		mmh.setSentDate(new Date(System.currentTimeMillis()));
		mmh.setText(htmlFromTemplatePedido(obj), true);
		
		return mm;
	}
	
}
