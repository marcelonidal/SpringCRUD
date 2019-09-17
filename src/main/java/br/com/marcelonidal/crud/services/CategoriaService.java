package br.com.marcelonidal.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.marcelonidal.crud.domain.Categoria;
import br.com.marcelonidal.crud.dto.CategoriaDTO;
import br.com.marcelonidal.crud.repositories.CategoriaRepository;
import br.com.marcelonidal.crud.services.exceptions.DataIntegrityException;
import br.com.marcelonidal.crud.services.exceptions.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired //Injecao de dependencia - instancia na interface
	private CategoriaRepository repo;
			
	public Categoria find(Integer id) throws ObjectNotFoundException {
		Optional<Categoria> cat = repo.findById(id);
		
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
	}
	
	public Categoria insert(Categoria obj) {
		obj.setId(null);
		return repo.save(obj);
	}
	
	public Categoria update(Categoria obj) {
		// precisa estar monitorado pelo jpa (na memoria - insert ou get anterior ao update)
		Categoria newObj = find(obj.getId());
		updateData(newObj, obj);
		return repo.save(newObj);
	}
	
	public void delete(Integer id) {
		find(id);
		try {
			repo.deleteById(id);
		}catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Nao e possivel excluir uma categoria que possui produtos!");
		}
	}
	
	public List<Categoria> findAll() {
		return repo.findAll();
	}
	
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageReq = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		return repo.findAll(pageReq);
	}
	
	public Categoria fromDTO(CategoriaDTO objDTO) {
		return new Categoria(objDTO.getId(), objDTO.getNome());
	}
	
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
}
