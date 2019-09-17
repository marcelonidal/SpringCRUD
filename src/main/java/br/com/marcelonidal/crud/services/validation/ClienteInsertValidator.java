package br.com.marcelonidal.crud.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.marcelonidal.crud.domain.Cliente;
import br.com.marcelonidal.crud.domain.enums.TipoCliente;
import br.com.marcelonidal.crud.dto.ClienteNewDTO;
import br.com.marcelonidal.crud.repositories.ClienteRepository;
import br.com.marcelonidal.crud.resources.exceptions.FieldMessage;
import br.com.marcelonidal.crud.services.validation.utils.validaCPFCNPJ;

public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	@Autowired
	private ClienteRepository repo;
	
	@Override
	public void initialize(ClienteInsert ann) {
		
	}
	
	@Override
	public boolean isValid(ClienteNewDTO objDTO, ConstraintValidatorContext context) {
		List<FieldMessage> list = new ArrayList<>();
		
		if(objDTO.getTipo().equals(TipoCliente.PESSOAFISICA.getCod()) && !validaCPFCNPJ.isValidCPF(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF invaido"));
		}
		if(objDTO.getTipo().equals(TipoCliente.PESSOAJURIDICA.getCod()) && !validaCPFCNPJ.isValidCPF(objDTO.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ invaido"));
		}
		
		Cliente aux = repo.findByEmail(objDTO.getEmail());
		if(aux != null) list.add(new FieldMessage("email", "Email ja existente"));
		
		//incluir testes aqui, add erros na list
		for (FieldMessage field : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(field.getMessage()).addPropertyNode(field.getFieldName()).addConstraintViolation();
		}
		return list.isEmpty();
	}
}
