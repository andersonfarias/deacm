package br.edu.ifpi.ads.deacm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.edu.ifpi.ads.deacm.security.xauth.TokenProvider;

/**
 * Configures x-auth-token security.
 */
@Configuration
public class XAuthConfiguration {

	@Bean
	public TokenProvider tokenProvider( JHipsterProperties jHipsterProperties ) {
		String secret = jHipsterProperties.getSecurity().getAuthentication().getXauth().getSecret();
		int validityInSeconds = jHipsterProperties.getSecurity().getAuthentication().getXauth().getTokenValidityInSeconds();
		return new TokenProvider( secret, validityInSeconds );
	}
}