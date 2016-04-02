package br.edu.ifpi.ads.deacm.repository.search;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import br.edu.ifpi.ads.deacm.domain.DMU;

/**
 * Spring Data ElasticSearch repository for the DMU entity.
 */
public interface DMUSearchRepository extends ElasticsearchRepository<DMU, Long> {}