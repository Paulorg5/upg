package io.github.paulorg5.glc;

import java.io.Serializable;

public abstract class Simbolo implements Serializable {
	private static final long serialVersionUID = 2551772878404072530L;

	private String valor;

	private boolean isTerminal;

	public Simbolo(String valor, boolean isTerminal) {
		super();
		this.valor = valor;
		this.isTerminal = isTerminal;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	public boolean isTerminal() {
		return isTerminal;
	}

	public void setTerminal(boolean isTerminal) {
		this.isTerminal = isTerminal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (isTerminal ? 1231 : 1237);
		result = prime * result + ((valor == null) ? 0 : valor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Simbolo other = (Simbolo) obj;
		if (isTerminal != other.isTerminal)
			return false;
		if (valor == null) {
			if (other.valor != null)
				return false;
		} else if (!valor.equals(other.valor))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return isTerminal ? valor : "<" + valor + ">";
	}
}
