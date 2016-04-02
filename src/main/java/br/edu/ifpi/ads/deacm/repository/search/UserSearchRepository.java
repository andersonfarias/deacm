package br.edu.ifpi.ads.deacm.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.edu.ifpi.ads.deacm.domain.User;

/**
 * Spring Data ElasticSearch repository for the User entity.
 */
public interface UserSearchRepository extends ElasticsearchRepository<User, Long> {}