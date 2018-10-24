package io.github.paulorg5.arquivodescricao.parser;

import java.util.ArrayList;
import java.util.List;

import com.fulmicoton.multiregexp.Token;

import io.github.paulorg5.arquivodescricao.domain.TipoToken;

public class LeitorToken {

	private List<Token<TipoToken>> tokens = new ArrayList<>();

	private Integer index = -1;

	public LeitorToken(List<Token<TipoToken>> tokens) {
		super();
		this.tokens = tokens;
	}

	public void avancar() {
		if (index < tokens.size()) {
			index++;
		}
	}

	public void voltar() {
		if (index > -1) {
			index--;
		}
	}

	public Token<TipoToken> getTokenAtual() {
		return index > -1 ? tokens.get(index) : null;
	}

	public Token<TipoToken> getProximoToken() {
		if (index < tokens.size()) {
			return tokens.get(++index);
		}

		return null;
	}
}
