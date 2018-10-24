package io.github.paulorg5.ll1;

import io.github.paulorg5.ast.ArvoreAnalise;
import io.github.paulorg5.glc.Simbolo;

public class PilhaDerivacaoSimbolo {

	private Simbolo simbolo;

	private ArvoreAnalise arvore;

	public PilhaDerivacaoSimbolo(Simbolo simbolo, ArvoreAnalise arvore) {
		super();
		this.simbolo = simbolo;
		this.arvore = arvore;
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
		return simbolo.getValor();
	}
}
