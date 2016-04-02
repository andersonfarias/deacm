package br.edu.ifpi.ads.deacm.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.edu.ifpi.ads.deacm.domain.Input;

/**
 * Spring Data ElasticSearch repository for the Input entity.
 */
public interface InputSearchRepository extends ElasticsearchRepository<Input, Long> {}