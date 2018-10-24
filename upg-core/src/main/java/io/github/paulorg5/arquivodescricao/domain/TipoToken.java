package io.github.paulorg5.arquivodescricao.domain;

public enum TipoToken {
	EOF("", false),
	
	WHITESPACE("[ \t\n\r]+", true),
	
	KEYWORD_IGNORAR("\"@Ignorar\"", false),
	ANOTACAO_HEADER("\"@Header\"", false), 
	ANOTACAO_LEXICO("\"@Lexico\"", false), 
	ANOTACAO_SINTATICO("\"@Sintatico\"", false),
	KEYWORD_NOME_PROJETO("\"NomeProjeto\"", false), 
	KEYWORD_VERSAO("\"Versao\"", false), 
	KEYWORD_PACOTE("\"Pacote\"", false),
	KEYWORD_AUTOR("\"Autor\"", false),
	KEYWORD_TRANSFORMAR_GRAMATICA("\"TransformarGramatica\"", false),
	KEYWORD_TRUE("\"true\"", false),
	KEYWORD_FALSE("\"false\"", false),
	
	ID("[_a-zA-Z][_a-zA-Z0-9]{0,30}", false), 
	SIMBOLO_EPSILON("\"£\"", false),
	SIMBOLO_OU_GLC("\"|\"", false),
	SIMBOLO_PONTO_VIRGULA("\";\"", false),
	SIMBOLO_ATRIBUICAO_NORMAL("\"=\"", false), 
	SIMBOLO_ATRIBUICAO_GLC("\"::=\"", false), 
	NAO_TERMINAL_GLC("[<_a-zA-Z][_a-zA-Z0-9>]{0,30}", false),
	REGEX("\\%\\%([^\\%\\%]+)\\%\\%", false),
	LITERAL("\\\"[^\\\"]*\\\"", false);

	private String regex;

	private boolean ignorar;
	
	private TipoToken() { }

	private TipoToken(String regex, Boolean ignorar) {
		this.regex = regex;
		this.ignorar = ignorar;
	}
	
	public String getRegex() {
		return regex;
	}
	
	public boolean isIgnorar() {
		return ignorar;
	}
}
