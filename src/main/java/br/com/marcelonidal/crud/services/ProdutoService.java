package br.com.marcelonidal.crud.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.marcelonidal.crud.domain.Categoria;
import br.com.marcelonidal.crud.domain.Produto;
import br.com.marcelonidal.crud.repositories.CategoriaRepository;
import br.com.marcelonidal.crud.repositories.ProdutoRepository;
import br.com.marcelonidal.crud.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired //Injecao de dependencia
	private ProdutoRepository repo;
	@Autowired
	private CategoriaRepository categoriaRepo;
			
	public Produto find(Integer id) throws ObjectNotFoundException {
		Optional<Produto> cat = repo.findById(id);
		
		return cat.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto nao encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));
	}
	
	public Page<Produto> search(String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		PageRequest pageReq = PageRequest.of(page, linesPerPage,Direction.valueOf(direction), orderBy);
		List<Categoria> categorias = categoriaRepo.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageReq);
	}
	
}
