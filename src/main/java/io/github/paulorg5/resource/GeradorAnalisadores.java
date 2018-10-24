package io.github.paulorg5.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.arquivodescricao.domain.ArquivoDescricao;
import io.github.paulorg5.arquivodescricao.domain.RegraLexico;
import io.github.paulorg5.arquivodescricao.parser.ArquivoDescricaoParser;
import io.github.paulorg5.ast.No;
import io.github.paulorg5.ast.Token;
import io.github.paulorg5.converters.ConversorCST;
import io.github.paulorg5.domain.EntradaUsuario;
import io.github.paulorg5.exceptions.AnalisadorLexicoException;
import io.github.paulorg5.exceptions.AnalisadorSintaticoException;
import io.github.paulorg5.exceptions.ArquivoDescricaoParserException;
import io.github.paulorg5.exceptions.GeracaoTabelaMovimentoException;
import io.github.paulorg5.glc.FatoradorGramatica;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.RemovedorRecursao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.glc.Terminal;
import io.github.paulorg5.lexico.GeradorAnalisadorLexico;
import io.github.paulorg5.lexico.TokenCompleto;
import io.github.paulorg5.ll1.ChaveTabela;
import io.github.paulorg5.ll1.Conjuntos;
import io.github.paulorg5.ll1.ParserLL1;
import io.github.paulorg5.ll1.TabelaAnaliseSintaticaLL1Builder;
import io.github.paulorg5.vo.RegraAnalisadorLexicoVO;
import io.github.paulorg5.vo.RetornoVO;

public class GeradorAnalisadores {
	
	public RetornoVO analisar(EntradaUsuario entrada) throws Exception {
		if (entrada.getCodigoArquivoDescricao().isEmpty()) {
			throw new Exception("O código do arquivo de descrição está vazio! Para continuar informa as regras.");
		}
		
		try {
			RetornoVO retorno = new RetornoVO();
			ArquivoDescricao arquivoDescricao = ArquivoDescricaoParser.getInstance().parse(entrada.getCodigoArquivoDescricao());
			Gramatica gramatica = arquivoDescricao.getConteudoSintatico().getGramatica();
			retorno.setGramaticaInformada(gramatica.toStringView());
			
			if (arquivoDescricao.getConteudoHeader().deveTransformarGramatica()) {
				retorno.setGramaticaSemRecursao(RemovedorRecursao.remover(gramatica).toStringView());
				retorno.setGramaticaFatorada(FatoradorGramatica.fatorar(gramatica).toStringView());
				retorno.setGramaticaFinal(gramatica.toStringView());
			}
			
			Map<NaoTerminal, Set<Simbolo>> first = Conjuntos.getInstance().first(gramatica);
			Map<NaoTerminal, Set<Simbolo>> follow = Conjuntos.getInstance().follow(gramatica, first);
			
			TabelaAnaliseSintaticaLL1Builder tabelaBuilder = new TabelaAnaliseSintaticaLL1Builder();
			Map<ChaveTabela, Producao> tabela = tabelaBuilder.comParametros(first, follow, gramatica).getTabela();
			
			retorno.setRegrasAnalisadorLexico(converterRegras(arquivoDescricao.getConteudoLexico().getRegras()));
			
			retorno.setFirstFollow(first, follow);
			retorno.setTabelaMovimento(tabela);
			
			if (!entrada.getCodigoTeste().isEmpty()) {
				List<RegraLexico> regras = arquivoDescricao.getConteudoLexico().getRegras();
				
				List<TokenCompleto> tokens = GeradorAnalisadorLexico.classificar(regras, entrada.getCodigoTeste());
				retorno.setTokensClassificados(tokens);
				
				ParserLL1 parserLL1 = new ParserLL1(gramatica, tabela, first);
				No no = parserLL1.parse(convertTokenCompletoToToken(tokens));
				retorno.setPilhaParsing(parserLL1.getLogger());
				
				retorno.setArvoreAnalise(new ConversorCST().converter(no));		
			}

			return retorno;
		} catch (ArquivoDescricaoParserException e) {
			throw new Exception("Erro ao analisar código do arquivo de descrição: \n" + e.getMessage());
		} catch (GeracaoTabelaMovimentoException e) {
			throw new Exception("Erro ao gerar a tabela de análise sintática: \n" + e.getMessage());
		} catch (AnalisadorLexicoException e) {
			throw new Exception("Erro ao classificar o código de teste: \n" + e.getMessage());
		} catch (AnalisadorSintaticoException e) {
			throw new Exception("Erro durante o parsing do código de teste: \n" + e.getMessage());
		}
	}
	
	private List<Token> convertTokenCompletoToToken(List<TokenCompleto> tokensCompleto) {
		List<Token> tokens = new ArrayList<>();
		for (TokenCompleto tokenCompleto : tokensCompleto) {
			Token t = new Token();
			t.setSimbolo(new Terminal(tokenCompleto.getTipoString()));
			t.setValor(tokenCompleto.getValor());
			t.setLinha(tokenCompleto.getLinha());
			t.setColunaInicio(tokenCompleto.getInicio());
			t.setColunaFim(tokenCompleto.getFim());
			tokens.add(t);
		}
		
		return tokens;
	}
	
	public List<Token> converterParaTerminal(List<TokenCompleto> tokensCompleto) {
		List<Token> tokens = new ArrayList<>();
		for (TokenCompleto tokenCompleto : tokensCompleto) {
			Token token = new Token();
			token.setSimbolo(new Terminal(tokenCompleto.getTipoString()));
			token.setColunaInicio(tokenCompleto.getInicio());
			token.setColunaFim(tokenCompleto.getFim());
			token.setLinha(tokenCompleto.getLinha());
			token.setValor(tokenCompleto.getValor());
			tokens.add(token);
		}
		
		return tokens;
	}
	
	private List<RegraAnalisadorLexicoVO> converterRegras(List<RegraLexico> regras) {
		List<RegraAnalisadorLexicoVO> resultado = new ArrayList<>();
		for (RegraLexico r : regras) {
			resultado.add(new RegraAnalisadorLexicoVO(r.getId(), r.getRegex(), r.contemIgnorar()));
		}
		
		return resultado;
	}
	
}
