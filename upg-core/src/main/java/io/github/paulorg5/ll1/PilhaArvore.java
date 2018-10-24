package io.github.paulorg5.ll1;

import io.github.paulorg5.ast.ArvoreAnalise;
import io.github.paulorg5.glc.Simbolo;

public class PilhaArvore {

	private Simbolo simbolo;

	private ArvoreAnalise arvore;
	
	public PilhaArvore(Simbolo simbolo, ArvoreAnalise arvore) {
		super();
		this.simbolo = simbolo;
		this.arvore = arvore;
	}

	public PilhaArvore(Simbolo simbolo) {
		super();
		this.simbolo = simbolo;
	}

	public Simbolo getSimbolo() {
		return simbolo;
	}

	public void setSimbolo(Simbolo simbolo) {
		this.simbolo = simbolo;
	}

	public ArvoreAnalise getArvore() {
		return arvore;
	}

	public void setArvore(ArvoreAnalise arvore) {
		this.arvore = arvore;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(simbolo.getValor());
		sb.append(" -> ");
		sb.append("\n");
		sb.append("\t\t" + arvore);
		return sb.toString();
	}
}
