package io.github.paulorg5.arquivodescricao.parser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.fulmicoton.multiregexp.Lexer;
import com.fulmicoton.multiregexp.ScanException;
import com.fulmicoton.multiregexp.Scanner;
import com.fulmicoton.multiregexp.Token;

import dk.brics.automaton.RegExp;
import io.github.paulorg5.arquivodescricao.domain.ArquivoDescricao;
import io.github.paulorg5.arquivodescricao.domain.Configuracao;
import io.github.paulorg5.arquivodescricao.domain.OpcaoPossivelHeader;
import io.github.paulorg5.arquivodescricao.domain.RegraLexico;
import io.github.paulorg5.arquivodescricao.domain.TipoToken;
import io.github.paulorg5.exceptions.ArquivoDescricaoParserException;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Terminal;
import io.github.paulorg5.glc.ValidadorGramatica;

public class ArquivoDescricaoParser {

	private Scanner<TipoToken> scanner;

	private List<Token<TipoToken>> tokens = new ArrayList<>();

	private LeitorToken leitorToken;

	private ArquivoDescricao arquivoDescricao;

	private ArquivoDescricaoParser() {
	}

	private static class ArquivoDescricaoParserInstance {
		private static final ArquivoDescricaoParser INSTANCE = new ArquivoDescricaoParser();
	}

	public static ArquivoDescricaoParser getInstance() {
		return ArquivoDescricaoParserInstance.INSTANCE;
	}

	public ArquivoDescricao parse(String conteudoArquivo) throws ArquivoDescricaoParserException {
		scanner = null;
		tokens = new ArrayList<>();
		leitorToken = null;
		arquivoDescricao = null;

		classificar(conteudoArquivo);

		ArquivoDescricao arquivo = arquivo(tokens);

		Configuracao transformarGramatica = arquivo.getConteudoHeader()
				.getConfiguracaoAPartir(TipoToken.KEYWORD_TRANSFORMAR_GRAMATICA);

		if (Boolean.valueOf(transformarGramatica.getValor())) {
			if (!ValidadorGramatica.validar(arquivo.getConteudoSintatico().getGramatica())) {
				throw new ArquivoDescricaoParserException("A gramática tem ciclos ou produções Epsilon!");
			}
		}

		return arquivo;
	}

	private void classificar(String conteudoArquivo) {
		Lexer<TipoToken> lexer = criarAnalisadorLexico();
		try {
			scanner = lexer.scannerFor(conteudoArquivo);
		} catch (IllegalArgumentException e) {
			System.out.println(e.getMessage());
		}

		try {
			while (scanner.next()) {
				if (!scanner.type.isIgnorar()) {
					tokens.add(new Token<TipoToken>(scanner.type, scanner.tokenString().toString(),
							scanner.startColumnToken, scanner.endColumnToken, scanner.line));
				}
			}

			tokens.add(new Token<TipoToken>(TipoToken.EOF, "EOF"));

			System.out.println("Tokens classificados");

			for (Token<TipoToken> token : tokens) {
				System.out.println(token.toString());
			}

		} catch (ScanException e) {
			System.out.println(e.getMessage());
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
	}

	private Lexer<TipoToken> criarAnalisadorLexico() {
		Lexer<TipoToken> lexer = new Lexer<>();
		for (TipoToken tipo : TipoToken.values()) {
			if (!tipo.equals(TipoToken.EOF)) {
				lexer.addRule(tipo, tipo.getRegex());
			}
		}
		return lexer;
	}

	private ArquivoDescricao arquivo(List<Token<TipoToken>> tokens) {
		arquivoDescricao = new ArquivoDescricao();
		arquivoDescricao.getConteudoSintatico().setGramatica(new Gramatica());

		if (tokens.size() == 1) {
			throw new ArquivoDescricaoParserException("O arquivo de descrição não pode estar vazio");
		}

		leitorToken = new LeitorToken(tokens);

		if (!leitorToken.getProximoToken().type.equals(TipoToken.ANOTACAO_HEADER)) {
			throw new ArquivoDescricaoParserException("Não foi encontrado a anotação @Header",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		} else {
			declHeader();
			if (!isHeaderComTodasPropriedadesObrigatorias()) {
				throw new ArquivoDescricaoParserException("O Header não tem todas as propriedades obrigátorias");
			}
		}

		if (!leitorToken.getProximoToken().type.equals(TipoToken.ANOTACAO_LEXICO)) {
			throw new ArquivoDescricaoParserException("Não foi encontrado a anotação @Lexico",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		} else {
			declLexico();
		}

		if (!leitorToken.getProximoToken().type.equals(TipoToken.ANOTACAO_SINTATICO)) {
			throw new ArquivoDescricaoParserException("Não foi encontrado a anotação @Sintatico",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		} else {
			declSintatico();
		}

		return arquivoDescricao;
	}

	private boolean isHeaderComTodasPropriedadesObrigatorias() {
		for (OpcaoPossivelHeader opcaoPossivel : Arrays.asList(OpcaoPossivelHeader.values())) {
			if (opcaoPossivel.isObrigatoria()) {
				if (arquivoDescricao.getConteudoHeader()
						.getConfiguracaoAPartir(opcaoPossivel.getTipoTokenReferencia()) == null) {
					return false;
				}
			}
		}

		return true;
	}

	private void declHeader() {
		TipoToken tipoTokenEncontrado = keywordHeader();
		if (tipoTokenEncontrado != null) {
			if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_ATRIBUICAO_NORMAL)) {
				throw new ArquivoDescricaoParserException("Era esperado o símbolo de atribuição = ",
						leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
						leitorToken.getTokenAtual().end);
			}

			switch (tipoTokenEncontrado) {
			case KEYWORD_TRANSFORMAR_GRAMATICA:
				if (!leitorToken.getProximoToken().type.equals(TipoToken.KEYWORD_TRUE)
						&& !leitorToken.getTokenAtual().type.equals(TipoToken.KEYWORD_FALSE)) {
					throw new ArquivoDescricaoParserException("Era esperado um literal",
							leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
							leitorToken.getTokenAtual().end);
				}

				arquivoDescricao.getConteudoHeader().getConfiguracaoAPartir(tipoTokenEncontrado)
						.setValor(leitorToken.getTokenAtual().str);

				break;
			default:
				if (!leitorToken.getProximoToken().type.equals(TipoToken.LITERAL)) {
					throw new ArquivoDescricaoParserException("Era esperado um literal",
							leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
							leitorToken.getTokenAtual().end);
				}

				if (arquivoDescricao.getConteudoHeader().getConfiguracaoAPartir(tipoTokenEncontrado) != null) {
					arquivoDescricao.getConteudoHeader().getConfiguracaoAPartir(tipoTokenEncontrado)
							// Precisa remover os ""
							.setValor(leitorToken.getTokenAtual().str.replace("\"", ""));
				}

				break;
			}

			if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_PONTO_VIRGULA)) {
				throw new ArquivoDescricaoParserException("Era esperado um ponto e vírgula",
						leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
						leitorToken.getTokenAtual().end);
			}

			declHeader();
		}
	}

	private TipoToken keywordHeader() {
		if (leitorToken.getProximoToken().type.equals(TipoToken.ANOTACAO_LEXICO)
				&& arquivoDescricao.getConteudoHeader().getConfiguracoes().isEmpty()) {
			throw new ArquivoDescricaoParserException("O header não pode estar vazio", leitorToken.getTokenAtual().line,
					leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
		}

		if (leitorToken.getTokenAtual().type.equals(TipoToken.ANOTACAO_LEXICO)
				&& !arquivoDescricao.getConteudoHeader().getConfiguracoes().isEmpty()) {
			leitorToken.voltar();
			return null;
		}

		if (!OpcaoPossivelHeader.isOpcaoValida(leitorToken.getTokenAtual().type)) {
			throw new ArquivoDescricaoParserException(
					"Era esperado alguma opção válida no Header, mas foi encontrado (" + leitorToken.getTokenAtual().str
							+ ") ",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		} else {
			arquivoDescricao.getConteudoHeader().getConfiguracoes()
					.add(new Configuracao(leitorToken.getTokenAtual().type));
		}

		return leitorToken.getTokenAtual().type;
	}

	private void declLexico() {
		RegraLexico regra = new RegraLexico();

		if (!leitorToken.getProximoToken().type.equals(TipoToken.ID)) {
			throw new ArquivoDescricaoParserException(
					"Era esperado um identificador mas foi encontrado " + leitorToken.getTokenAtual().toString(),
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		}
		regra.setId(leitorToken.getTokenAtual().str);

		if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_ATRIBUICAO_NORMAL)) {
			throw new ArquivoDescricaoParserException("Era esperado um = ", leitorToken.getTokenAtual().line,
					leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
		}

		if (!leitorToken.getProximoToken().type.equals(TipoToken.REGEX)) {
			throw new ArquivoDescricaoParserException("Não é possível utilizar uma regex vazia ",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		}

		regra.setRegex(removerColchetesDaRegex(leitorToken.getTokenAtual().str));

		try {
			new RegExp(regra.getRegex());
		} catch (IllegalArgumentException e) {
			StringBuilder sb = new StringBuilder();
			sb.append("Erro na expressão regular " + regra.getRegex());
			sb.append(" Em Linha: " + leitorToken.getTokenAtual().line);
			sb.append(" Coluna: " + leitorToken.getTokenAtual().start + " - " + leitorToken.getTokenAtual().end);
			throw new ArquivoDescricaoParserException(sb.toString());
		}

		List<Token<TipoToken>> anotacoes = anotacaoLexico();
		if (!anotacoes.isEmpty()) {
			for (Token<TipoToken> token : anotacoes) {
				regra.getAnotacoes().add(token.type);
			}
		}

		if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_PONTO_VIRGULA)) {
			throw new ArquivoDescricaoParserException("Era esperado um ; ", leitorToken.getTokenAtual().line,
					leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
		}

		arquivoDescricao.getConteudoLexico().getRegras().add(regra);

		if (!leitorToken.getProximoToken().type.equals(TipoToken.ANOTACAO_SINTATICO)) {
			leitorToken.voltar();
			declLexico();
		} else {
			leitorToken.voltar();
		}
	}

	private String removerColchetesDaRegex(String regex) {
		return regex.substring(2, regex.length() - 2);
	}

	private List<Token<TipoToken>> anotacaoLexico() {
		List<Token<TipoToken>> anotacoes = new ArrayList<>();

		if (leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_PONTO_VIRGULA)) {
			leitorToken.voltar();
			return anotacoes;
		}
		leitorToken.voltar();

		if (!leitorToken.getProximoToken().type.equals(TipoToken.KEYWORD_IGNORAR)) {
			throw new ArquivoDescricaoParserException("Era esperado a anotação @Ignorar ou ;",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		} else {
			anotacoes.add(leitorToken.getTokenAtual());

			if (leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_PONTO_VIRGULA)) {
				leitorToken.voltar();
			} else {
				throw new ArquivoDescricaoParserException("Era esperado um ;", leitorToken.getTokenAtual().line,
						leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
			}
		}

		return anotacoes;
	}

	private void declSintatico() {
		if (!leitorToken.getProximoToken().type.equals(TipoToken.NAO_TERMINAL_GLC)) {
			throw new ArquivoDescricaoParserException("Era esperado um símbolo Não Terminal",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		}

		NaoTerminal ntEsquerdo = new NaoTerminal(formatarNaoTerminal(leitorToken.getTokenAtual().str));

		arquivoDescricao.getConteudoSintatico().getGramatica().getNaoTerminais().add(ntEsquerdo);

		if (arquivoDescricao.getConteudoSintatico().getGramatica().getNtInicial() == null) {
			arquivoDescricao.getConteudoSintatico().getGramatica().setNtInicial(ntEsquerdo);
		}

		if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_ATRIBUICAO_GLC)) {
			throw new ArquivoDescricaoParserException("Era esperado um símbolo de atribuiçao da GLC ::=",
					leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
					leitorToken.getTokenAtual().end);
		}

		expressao(ntEsquerdo);

		if (!leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_PONTO_VIRGULA)) {
			throw new ArquivoDescricaoParserException("Era esperado o símbolo ;", leitorToken.getTokenAtual().line,
					leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
		}

		if (leitorToken.getProximoToken().type.equals(TipoToken.NAO_TERMINAL_GLC)) {
			leitorToken.voltar();
			declSintatico();
		} else {
			if (!leitorToken.getTokenAtual().type.equals(TipoToken.EOF)) {
				throw new ArquivoDescricaoParserException("Era esperado um nt ou EOF", leitorToken.getTokenAtual().line,
						leitorToken.getTokenAtual().start, leitorToken.getTokenAtual().end);
			}
		}
	}

	private void expressao(NaoTerminal ntEsquerdo) {
		arquivoDescricao.getConteudoSintatico().getGramatica().getProducoes().add(producao(ntEsquerdo));

		if (leitorToken.getProximoToken().type.equals(TipoToken.SIMBOLO_OU_GLC)) {
			expressao(ntEsquerdo);
		} else {
			leitorToken.voltar();
		}
	}

	private Producao producao(NaoTerminal nt) {
		Producao producao = new Producao();
		producao.setNt(nt);

		while (leitorToken.getProximoToken().type.equals(TipoToken.NAO_TERMINAL_GLC)
				|| leitorToken.getTokenAtual().type.equals(TipoToken.ID)
				|| leitorToken.getTokenAtual().type.equals(TipoToken.LITERAL)
				|| leitorToken.getTokenAtual().type.equals(TipoToken.SIMBOLO_EPSILON)) {

			switch (leitorToken.getTokenAtual().type) {
			case NAO_TERMINAL_GLC:
				System.out.println(leitorToken.getTokenAtual().str + " Vai adicionar esse não terminal");
				arquivoDescricao.getConteudoSintatico().getGramatica().getNaoTerminais()
						.add(new NaoTerminal(formatarNaoTerminal(leitorToken.getTokenAtual().str)));
				producao.getSimbolos().add(new NaoTerminal(formatarNaoTerminal(leitorToken.getTokenAtual().str)));
				break;
			case ID:
				arquivoDescricao.getConteudoSintatico().getGramatica().getTerminais()
						.add(new Terminal(leitorToken.getTokenAtual().str));
				producao.getSimbolos().add(new Terminal(leitorToken.getTokenAtual().str));
				break;
			case SIMBOLO_EPSILON:
				producao.getSimbolos().add(new Epsilon());
				break;
			default:
				throw new ArquivoDescricaoParserException(
						"Não é possível utilizar o token " + leitorToken.getTokenAtual().type
								+ " como elemento de uma produção",
						leitorToken.getTokenAtual().line, leitorToken.getTokenAtual().start,
						leitorToken.getTokenAtual().end);
			}

		}

		leitorToken.voltar();
		return producao;
	}

	private String formatarNaoTerminal(String naoTerminal) {
		return naoTerminal.replaceAll("<|>", "");
	}
}
