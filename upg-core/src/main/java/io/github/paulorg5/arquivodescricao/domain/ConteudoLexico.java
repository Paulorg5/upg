package io.github.paulorg5.arquivodescricao.domain;

import java.util.ArrayList;
import java.util.List;

public class ConteudoLexico {

	private List<RegraLexico> regras = new ArrayList<>();

	public List<RegraLexico> getRegras() {
		return regras;
	}

	public void setRegras(List<RegraLexico> regras) {
		this.regras = regras;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---> Conteúdo Léxico");
		for (RegraLexico regra : regras) {
			sb.append("----> " + regra.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
}
