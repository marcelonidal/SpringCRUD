package br.com.marcelonidal.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.marcelonidal.crud.domain.Cliente;

// DAO
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer> {
	//Spring Data - ler documentacao - usar nomes corretos evita a criacao manual das querys
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);
}
