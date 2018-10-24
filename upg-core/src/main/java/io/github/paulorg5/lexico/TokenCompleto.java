package io.github.paulorg5.lexico;

import java.io.Serializable;

import io.github.paulorg5.glc.Terminal;

public class TokenCompleto implements Serializable {
	private static final long serialVersionUID = -7484384394049081572L;

	private String tipoString;

	private boolean ignorar;

	private String valor;

	private Integer inicio;

	private Integer fim;

	private Integer linha;

	public Terminal getTerminal() {
		return new Terminal(tipoString);
	}

	public TokenCompleto(String tipoString) {
		super();
		this.tipoString = tipoString;
	}

	public TokenCompleto(String tipoString, boolean ignorar, String valor) {
		super();
		this.tipoString = tipoString;
		this.ignorar = ignorar;
		this.valor = valor;
	}

	public TokenCompleto(String tipoString, boolean ignorar) {
		super();
		this.tipoString = tipoString;
		this.ignorar = ignorar;
	}

	public TokenCompleto(String tipoString, boolean ignorar, String valor, Integer inicio, Integer fim) {
		super();
		this.tipoString = tipoString;
		this.ignorar = ignorar;
		this.valor = valor;
		this.inicio = inicio;
		this.fim = fim;
	}

	public TokenCompleto(String tipoString, boolean ignorar, String valor, Integer inicio, Integer fim, Integer linha) {
		super();
		this.tipoString = tipoString;
		this.ignorar = ignorar;
		this.valor = valor;
		this.inicio = inicio;
		this.fim = fim;
		this.linha = linha;
	}

	public String getTipoString() {
		return tipoString;
	}

	public void setTipoString(String tipoString) {
		this.tipoString = tipoString;
	}

	public boolean isIgnorar() {
		return ignorar;
	}

	public void setIgnorar(boolean ignorar) {
		this.ignorar = ignorar;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public Integer getInicio() {
		return inicio;
	}

	public void setInicio(Integer inicio) {
		this.inicio = inicio;
	}

	public Integer getFim() {
		return fim;
	}

	public void setFim(Integer fim) {
		this.fim = fim;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	@Override
	public String toString() {
		return "TokenCompleto [tipoString=" + tipoString + ", ignorar=" + ignorar + ", valor=" + valor + "]";
	}
}
