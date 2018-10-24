package io.github.paulorg5.ast;

import io.github.paulorg5.glc.Simbolo;

public class Token {

	private Simbolo simbolo;

	private Integer linha;

	private Integer colunaInicio;

	private Integer colunaFim;

	private String valor;

	public Token() {
		super();
	}

	public Token(Simbolo simbolo) {
		super();
		this.simbolo = simbolo;
	}

	public Simbolo getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(Simbolo simbolo) {
		this.simbolo = simbolo;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	public Integer getColunaInicio() {
		return colunaInicio;
	}

	public void setColunaInicio(Integer colunaInicio) {
		this.colunaInicio = colunaInicio;
	}

	public Integer getColunaFim() {
		return colunaFim;
	}

	public void setColunaFim(Integer colunaFim) {
		this.colunaFim = colunaFim;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public String toString() {
		return "Token [simbolo=" + simbolo + ", linha=" + linha + ", colunaInicio=" + colunaInicio + ", colunaFim="
				+ colunaFim + ", valor=" + valor + "]";
	}
}
