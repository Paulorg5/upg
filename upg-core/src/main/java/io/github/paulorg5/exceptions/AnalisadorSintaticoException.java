package io.github.paulorg5.exceptions;

public class AnalisadorSintaticoException extends RuntimeException {
	private static final long serialVersionUID = -6241305852191703901L;

	public AnalisadorSintaticoException() {
		super();
	}

	public AnalisadorSintaticoException(String mensagem) {
		super(mensagem);
	}

}
