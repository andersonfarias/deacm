package br.edu.ifpi.ads.deacm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpi.ads.deacm.domain.DMU;

/**
 * Spring Data JPA repository for the DMU entity.
 */
public interface DMURepository extends JpaRepository<DMU, Long> {}