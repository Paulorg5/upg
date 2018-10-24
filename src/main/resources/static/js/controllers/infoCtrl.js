angular.module("app").controller("infoCtrl", function ($scope) {
	$scope.cmOptLeitura = {
			lineNumbers: true,
			viewportMargin: Infinity,
			readOnly: true
		};
	
	$scope.exemploArquivoDescricao = 
		"@Header\n\n" +
		
		"NomeProjeto = \"Expressao Aritmetica\";\n" +
		"Pacote = \"br.com.expressao\";\n" +
		"Versao = \"1.0.0.0\";\n" +
		"TransformarGramatica = false;\n\n" +

		"@Lexico\n\n" +

		"MAIS = %%\"+\"%%;\n" +
		"VEZES = %%\"*\"%%;\n" +
		"ID = %%[0-9]+%%;\n" +
		"LEFT_PAREN = %%\"(\"%%;\n" +
		"RIGHT_PAREN = %%\")\"%%;\n\n" +

		"@Sintatico\n\n" +

		"<E> ::= <T> <EE>;\n" +
		"<EE> ::= MAIS <T> <EE> | £;\n" +
		"<T> ::= <F> <TT>;\n" +
		"<TT> ::= VEZES <F> <TT> | £;\n" +
		"<F> ::= LEFT_PAREN <E> RIGHT_PAREN | ID;";
	
	$scope.bnfArquivoDescricao =
		"<Arquivo> ::= ANOTACAO_HEADER <DeclHeader> ANOTACAO_LEXICO <DeclLexico> ANOTACAO_SINTATICO <DeclSintatico>;\n\n" +

		"<DeclHeader> ::= <KeywordHeader> \"=\" <LiteralOuKeywordBoolean> \";\";\n" +
		"<KeywordHeader> ::= KEYWORD_NOME_PROJETO | KEYWORD_VERSAO | KEYWORD_PACOTE | KEYWORD_AUTOR | KEYWORD_TRANSFORMAR_GRAMATICA;\n" +
		"<LiteralOuKeywordBoolean> ::= LITERAL | KEYWORD_TRUE | KEYWORD_FALSE;\n\n" +
 
		"<DeclLexico> ::= ID \"=\" \"%%\" REGEX \"%%\" <AnotacaoLexico> \";\";\n" +
		"<AnotacaoLexico> ::= KEYWORD_IGNORAR | &;\n\n" +
 
		"<DeclSintatico> ::= NAO_TERMINAL_GLC SIMBOLO_ATRIBUICAO_GLC <Expressao> \";\";\n" +
		"<Expressao> ::= <Producao> | <Producao> \"|\" <Producao>;\n" +
		"<Producao> ::= <NTouT> | <NTouT> <Producao>;\n" +
		"<NTouT> ::= NAO_TERMINAL_GLC | ID;";
	
});