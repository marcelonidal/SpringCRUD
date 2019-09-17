package br.com.marcelonidal.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.marcelonidal.crud.domain.Pedido;

// DAO
@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	//Superclasse abrange os filhos
}
