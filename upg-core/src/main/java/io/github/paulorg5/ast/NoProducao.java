package io.github.paulorg5.ast;

import java.util.ArrayList;
import java.util.List;

import io.github.paulorg5.glc.Producao;

public class NoProducao implements No {

	private Producao producao;

	private List<No> filhos;

	private int tamanho;

	public NoProducao(Producao producao) {
		super();
		this.producao = producao;
		filhos = new ArrayList<>(producao.getSimbolos().size());
	}

	public Producao getProducao() {
		return producao;
	}

	public void setProducao(Producao producao) {
		this.producao = producao;
	}

	public List<No> getFilhos() {
		return filhos;
	}

	public void setFilhos(List<No> filhos) {
		this.filhos = filhos;
	}

	public int getTamanho() {
		return tamanho;
	}

	public void setTamanho(int tamanho) {
		this.tamanho = tamanho;
	}

	public boolean isCheio() {
		return tamanho == producao.getSimbolos().size();
	}

	public void addFilho(No no) {
		if (tamanho >= producao.getSimbolos().size()) {
			System.out.println("Está cheia");
		}

		filhos.add(tamanho++, no);
	}

	@Override
	public String toString() {
		return "NoProducao [producao=" + producao + ", filhos=" + filhos + ", tamanho=" + tamanho + "]";
	}

	@Override
	public String getNome() {
		return producao.getNt().getValor();
	}
}
