package io.github.paulorg5.ast;

import java.util.ArrayList;
import java.util.List;

public class ArvoreAnalise {

	private Token pai;

	private List<ArvoreAnalise> filhos = new ArrayList<>();

	public ArvoreAnalise(Token pai) {
		super();
		this.pai = pai;
	}

	public Token getPai() {
		return pai;
	}

	public void setPai(Token pai) {
		this.pai = pai;
	}

	public List<ArvoreAnalise> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<ArvoreAnalise> filhos) {
		this.filhos = filhos;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Pai: " + pai);
		for (ArvoreAnalise arvore : filhos) {
			sb.append(arvore);
		}
		return sb.toString();
	}
}
