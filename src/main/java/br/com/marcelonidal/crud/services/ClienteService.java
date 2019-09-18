package br.com.marcelonidal.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.marcelonidal.crud.domain.Cidade;
import br.com.marcelonidal.crud.domain.Cliente;
import br.com.marcelonidal.crud.domain.Endereco;
import br.com.marcelonidal.crud.domain.enums.Perfil;
import br.com.marcelonidal.crud.domain.enums.TipoCliente;
import br.com.marcelonidal.crud.dto.ClienteDTO;
import br.com.marcelonidal.crud.dto.ClienteNewDTO;
import br.com.marcelonidal.crud.repositories.ClienteRepository;
import br.com.marcelonidal.crud.repositories.EnderecoRepository;
import br.com.marcelonidal.crud.security.UserSpringSecurity;
import br.com.marcelonidal.crud.services.exceptions.AuthorizationException;
import br.com.marcelonidal.crud.services.exceptions.DataIntegrityException;
import br.com.marcelonidal.crud.services.exceptions.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired //Injecao de dependencia
	private ClienteRepository repo;
	@Autowired
	private EnderecoRepository enderecoRepo;
	@Autowired
	private BCryptPasswordEncoder pwEncoder;
			
	public Cliente find(Integer id) throws ObjectNotFoundException {
		
		UserSpringSecurity user = UserService.authenticated();
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> cat = repo.findById(id);
		
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));
	}
	
	public Cliente insert(Cliente obj) {
		obj.setId(null);
		obj = repo.save(obj);
		enderecoRepo.saveAll(obj.getEnderecos());
		return obj;
	}
	
	public Cliente update(Cliente obj) {
		// precisa estar monitorado pelo jpa
		Cliente newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Nao e possivel excluir um Cliente que possui pedidos!");
		}
	}
	
	public List<Cliente> findAll() {
		return repo.findAll();
	}
	
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageReq = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return repo.findAll(pageReq);
	}
	
	public Cliente fromDTO(ClienteDTO objDTO) {
		return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
	}

	public Cliente fromDTO(ClienteNewDTO objDTO) {
		Cliente cli = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(), TipoCliente.toEnum(objDTO.getTipo()), pwEncoder.encode(objDTO.getSenha()));
		Cidade cid = new Cidade(objDTO.getCidadeId(), null, null);
		Endereco end = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(), objDTO.getBairro(), objDTO.getCep(), cli, cid);
		cli.getEnderecos().add(end);
		cli.getTelefones().add(objDTO.getTelefone1());
		if(objDTO.getTelefone2() != null) cli.getTelefones().add(objDTO.getTelefone2());
		if(objDTO.getTelefone3() != null) cli.getTelefones().add(objDTO.getTelefone3());
		
		return cli;
	}
	
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
}
