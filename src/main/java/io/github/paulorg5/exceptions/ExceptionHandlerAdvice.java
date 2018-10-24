package io.github.paulorg5.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlerAdvice {

	@ExceptionHandler(ResourceException.class)
	public ResponseEntity<ErroResponse> exceptionHandler(ResourceException ex) {
		ErroResponse erro = new ErroResponse();
		erro.setCodigoErro(ex.getHttpStatus().value());
		erro.setMensagem(ex.getMessage());
		return new ResponseEntity<ErroResponse>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
