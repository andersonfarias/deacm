package br.edu.ifpi.ads.deacm.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpi.ads.deacm.domain.Authority;

/**
 * Spring Data JPA repository for the Authority entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}