package io.github.paulorg5.ast;

public class NoToken implements No {

	private Token valor;

	private boolean epsilon = false;

	public boolean isEpsilon() {
		return epsilon;
	}

	public void setEpsilon(boolean epsilon) {
		this.epsilon = epsilon;
	}

	public NoToken(Token valor, boolean epsilon) {
		super();
		this.valor = valor;
		this.epsilon = epsilon;
	}

	public Token getValor() {
		return valor;
	}

	public void setValor(Token valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return valor.getValor();
	}

	@Override
	public String getNome() {
		return valor.getSimbolo().getValor();
	}
}
