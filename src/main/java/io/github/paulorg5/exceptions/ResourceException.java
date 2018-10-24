package io.github.paulorg5.exceptions;

import org.springframework.http.HttpStatus;

public class ResourceException extends RuntimeException {
	private static final long serialVersionUID = -5845070048759336060L;
	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

	public ResourceException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
	}

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

}
