package br.com.fiap.global.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.fiap.global.model.User;



public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByEmail(String username);

}
