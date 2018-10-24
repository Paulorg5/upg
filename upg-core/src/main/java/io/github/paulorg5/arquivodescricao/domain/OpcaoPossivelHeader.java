package io.github.paulorg5.arquivodescricao.domain;

public enum OpcaoPossivelHeader {

	AUTOR(false, TipoToken.KEYWORD_AUTOR), 
	NOME_PROJETO(true, TipoToken.KEYWORD_NOME_PROJETO), 
	PACOTE(true, TipoToken.KEYWORD_PACOTE), 
	VERSAO(false, TipoToken.KEYWORD_VERSAO),
	TRANSFORMAR_GRAMATICA(true, TipoToken.KEYWORD_TRANSFORMAR_GRAMATICA);

	private boolean isObrigatoria;

	private TipoToken tipoTokenReferencia;

	private OpcaoPossivelHeader(boolean isObrigatoria, TipoToken tipoToken) {
		this.isObrigatoria = isObrigatoria;
		this.tipoTokenReferencia = tipoToken;
	}

	public static boolean isOpcaoValida(TipoToken tipoToken) {
		switch (tipoToken) {
		case KEYWORD_AUTOR:
		case KEYWORD_NOME_PROJETO:
		case KEYWORD_PACOTE:
		case KEYWORD_VERSAO:
		case KEYWORD_TRANSFORMAR_GRAMATICA:
			return true;
		default:
			return false;
		}
	}

	public boolean isObrigatoria() {
		return isObrigatoria;
	}

	public TipoToken getTipoTokenReferencia() {
		return tipoTokenReferencia;
	}
}
