package br.edu.ifpi.ads.deacm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpi.ads.deacm.domain.Input;

/**
 * Spring Data JPA repository for the Input entity.
 */
public interface InputRepository extends JpaRepository<Input, Long> {

}