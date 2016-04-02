package br.edu.ifpi.ads.deacm.web.rest.dto;

public class ResponseError {

	private final String message;

	public ResponseError( String message ) {
		super();
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

}