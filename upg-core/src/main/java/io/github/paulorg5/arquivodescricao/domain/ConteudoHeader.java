package io.github.paulorg5.arquivodescricao.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ConteudoHeader {

	private Set<Configuracao> configuracoes = new HashSet<>();

	public Set<Configuracao> getConfiguracoes() {
		return configuracoes;
	}

	public void setConfiguracoes(Set<Configuracao> configuracoes) {
		this.configuracoes = configuracoes;
	}

	public Boolean deveTransformarGramatica() {
		Configuracao configuracao = getConfiguracaoAPartir(TipoToken.KEYWORD_TRANSFORMAR_GRAMATICA);
		if (configuracao == null) {
			return true;
		}

		return Boolean.valueOf(configuracao.getValor());
	}

	public Configuracao getConfiguracaoAPartir(TipoToken tipoToken) {
		Iterator<Configuracao> itr = configuracoes.iterator();
		while (itr.hasNext()) {
			Configuracao current = itr.next();
			if (current.getNome().equals(tipoToken)) {
				return current;
			}
		}

		return null;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("---> Conteúdo Header");
		for (Configuracao conf : configuracoes) {
			sb.append("----> " + conf.toString());
		}
		sb.append("\n");
		return sb.toString();
	}
}
