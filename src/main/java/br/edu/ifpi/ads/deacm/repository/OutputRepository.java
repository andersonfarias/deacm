package br.edu.ifpi.ads.deacm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpi.ads.deacm.domain.Output;

/**
 * Spring Data JPA repository for the Output entity.
 */
public interface OutputRepository extends JpaRepository<Output, Long> {

}