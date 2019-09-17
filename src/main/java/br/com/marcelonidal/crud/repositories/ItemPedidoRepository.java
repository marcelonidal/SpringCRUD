package br.com.marcelonidal.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.marcelonidal.crud.domain.ItemPedido;

// DAO
@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Integer>{

}
