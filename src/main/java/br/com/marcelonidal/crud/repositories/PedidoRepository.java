package br.com.marcelonidal.crud.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.marcelonidal.crud.domain.Cliente;
import br.com.marcelonidal.crud.domain.Pedido;

// DAO
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	//Superclasse abrange os filhos
	
	// RESTRICAO DE CONTEUDO POR CLIENTE
	@Transactional(readOnly = true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageRequest);
}
