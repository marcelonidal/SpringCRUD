package br.com.marcelonidal.crud.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.marcelonidal.crud.domain.Cidade;

// DAO
@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Integer>{

}
