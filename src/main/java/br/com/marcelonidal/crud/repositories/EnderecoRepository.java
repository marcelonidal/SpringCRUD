package br.com.marcelonidal.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.marcelonidal.crud.domain.Endereco;

// DAO
@Repository
public interface EnderecoRepository extends JpaRepository<Endereco, Integer>{

}
