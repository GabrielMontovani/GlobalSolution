package br.com.fiap.global.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.global.model.Doador;

public interface DoadorRepository extends JpaRepository<Doador, Long> {
	
	Page<Doador> findByTitleLike(String title, Pageable pageable);

}
