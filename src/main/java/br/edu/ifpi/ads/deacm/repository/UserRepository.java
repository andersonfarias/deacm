package br.edu.ifpi.ads.deacm.repository;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.edu.ifpi.ads.deacm.domain.User;

/**
 * Spring Data JPA repository for the User entity.
 */
public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findOneByActivationKey( String activationKey );

	List<User> findAllByActivatedIsFalseAndCreatedDateBefore( ZonedDateTime dateTime );

	Optional<User> findOneByResetKey( String resetKey );

	Optional<User> findOneByEmail( String email );

	Optional<User> findOneByLogin( String login );

	Optional<User> findOneById( Long userId );

	@Override
	void delete( User t );

}