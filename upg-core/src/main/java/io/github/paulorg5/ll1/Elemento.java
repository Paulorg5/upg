package io.github.paulorg5.ll1;

import io.github.paulorg5.ast.Token;
import io.github.paulorg5.glc.Producao;

public class Elemento {

	private Producao producao;

	private Token token;

	public Elemento(Token token) {
		super();
		this.token = token;
	}

	public Elemento(Producao producao) {
		super();
		this.producao = producao;
	}

	public Producao getProducao() {
		return producao;
	}

	public void setProducao(Producao producao) {
		this.producao = producao;
	}

	public Token getToken() {
		return token;
	}

	public void setToken(Token token) {
		this.token = token;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		if (producao == null) {
			sb.append(" Token -> " + token.getSimbolo().getValor());
		} else {
			sb.append(" Produção -> " + producao.getNt() + " ::= " + producao.getSimbolos());
		}
		return sb.toString();
	}
	
}
