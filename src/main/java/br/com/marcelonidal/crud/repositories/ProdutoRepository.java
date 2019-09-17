package br.com.marcelonidal.crud.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.marcelonidal.crud.domain.Categoria;
import br.com.marcelonidal.crud.domain.Produto;

// DAO
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer>{

	//JPQL
//	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj>categorias.cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
//	Page<Produto> search(@Param("nome") String nome, @Param("categorias") List<Categoria> categorias, Pageable pageReq);
	
	//Spring Data
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageReq);
}
