package io.github.paulorg5.glc;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Producao implements Serializable {
	private static final long serialVersionUID = -1288675332353956417L;

	private NaoTerminal nt;

	private List<Simbolo> simbolos = new ArrayList<>();

	public NaoTerminal getNt() {
		return nt;
	}

	public void setNt(NaoTerminal nt) {
		this.nt = nt;
	}

	public List<Simbolo> getSimbolos() {
		return simbolos;
	}

	public void setSimbolos(List<Simbolo> simbolos) {
		this.simbolos = simbolos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(nt);
		sb.append(" ::= ");
		for (Simbolo simbolo : simbolos) {
			sb.append(simbolo);
			sb.append(" ");
		}
		return sb.toString();
	}

	public String toStringSomenteSimbolos() {
		StringBuilder sb = new StringBuilder();
		for (Simbolo simbolo : simbolos) {
			sb.append(simbolo);
			sb.append(" ");
		}
		return sb.toString();
	}
}
