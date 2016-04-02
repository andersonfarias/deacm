package br.edu.ifpi.ads.deacm.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.edu.ifpi.ads.deacm.domain.Output;

/**
 * Spring Data ElasticSearch repository for the Output entity.
 */
public interface OutputSearchRepository extends ElasticsearchRepository<Output, Long> {}