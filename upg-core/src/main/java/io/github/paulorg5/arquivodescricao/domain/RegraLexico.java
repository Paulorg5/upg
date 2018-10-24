package io.github.paulorg5.arquivodescricao.domain;

import java.util.ArrayList;
import java.util.List;

public class RegraLexico {

	private String id;

	private String regex;

	private List<TipoToken> anotacoes = new ArrayList<>();

	public boolean contemIgnorar() {
		for (TipoToken tipo : anotacoes) {
			if (tipo.equals(TipoToken.KEYWORD_IGNORAR)) {
				return true;
			}
		}

		return false;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public List<TipoToken> getAnotacoes() {
		return anotacoes;
	}

	public void setAnotacoes(List<TipoToken> anotacoes) {
		this.anotacoes = anotacoes;
	}

	@Override
	public String toString() {
		return "RegraLexico [id=" + id + ", regex=" + regex + ", anotacoes=" + anotacoes + "]";
	}
}
