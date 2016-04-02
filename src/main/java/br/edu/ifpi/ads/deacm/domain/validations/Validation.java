package br.edu.ifpi.ads.deacm.domain.validations;

public interface Validation {

	public void validate( Object... args ) throws RuntimeException;

}