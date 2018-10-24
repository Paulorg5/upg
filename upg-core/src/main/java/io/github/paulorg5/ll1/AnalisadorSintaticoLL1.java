package io.github.paulorg5.ll1;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import io.github.paulorg5.ast.ArvoreAnalise;
import io.github.paulorg5.ast.Token;
import io.github.paulorg5.exceptions.AnalisadorSintaticoException;
import io.github.paulorg5.glc.EOF;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.glc.Terminal;

public final class AnalisadorSintaticoLL1 {

	private Map<ChaveTabela, Producao> tabelaMovimentos = new HashMap<ChaveTabela, Producao>();
	
	private Deque<PilhaDerivacaoSimbolo> pilhaParsing = new ArrayDeque<PilhaDerivacaoSimbolo>();
	
	private ArvoreAnalise arvoreAnalise;
	
	private List<PilhaParsingLog> pilhaParsingLog = new ArrayList<>();
	
	public AnalisadorSintaticoLL1(Map<ChaveTabela, Producao> tabelaMovimentos, NaoTerminal ntInicial) {
		this.tabelaMovimentos = tabelaMovimentos;
		arvoreAnalise = new ArvoreAnalise(new Token(ntInicial));
		
		pilhaParsing.offerFirst(new PilhaDerivacaoSimbolo(new EOF(), null));
		pilhaParsing.offerFirst(new PilhaDerivacaoSimbolo(ntInicial, arvoreAnalise));
	}
	
	private ArvoreAnalise processarSimbolo(Token token) throws AnalisadorSintaticoException {
		if (pilhaParsing.isEmpty()) {
			throw new AnalisadorSintaticoException("O parsing já está completo");
		}
		
		while (true) {
			PilhaDerivacaoSimbolo topo = pilhaParsing.pollFirst();
			PilhaParsingLog logPilha = new PilhaParsingLog();
			
			System.out.println("Simbolo sendo analisado " + token.getSimbolo());
			logPilha.setValorEntrada(token.getSimbolo().toString());
			
			System.out.println("Valor do topo: " + getValorPilha(pilhaParsing));
			
			if (topo.getSimbolo().equals(token.getSimbolo())) {
				if (!topo.getSimbolo().equals(new EOF())) {
					topo.getArvore().setPai(token);
				}
				
				System.out.println("Casamento entre " + topo.getSimbolo().getValor() + " -" + token.getSimbolo());
				logPilha.setValorPilha(getValorPilha(pilhaParsing));
				logPilha.setAcao(AcaoSintatica.CASAMENTO);
				
				if (token.getSimbolo().equals(new EOF())) {
					logPilha.setAcao(AcaoSintatica.ACEITA);
				}
				
				pilhaParsingLog.add(logPilha);
				return token.getSimbolo().equals(new EOF()) ? arvoreAnalise : null;
			}
			
			if (topo.getSimbolo() instanceof Terminal) {
				throw new AnalisadorSintaticoException("Era esperado " + topo.getSimbolo().getValor() + " mas foi encontrado " + token.getSimbolo());
			}
			
			NaoTerminal nt = (NaoTerminal) topo.getSimbolo();
			Producao p = tabelaMovimentos.get(new ChaveTabela(nt, token.getSimbolo()));
			
			if (p == null) {
				throw new AnalisadorSintaticoException("Não foi encontrado produção para " + token.getValor() + " com o topo da pilha igual " + topo.getSimbolo());
			}
			
			System.out.println("Não Terminal: " + nt.getValor().replaceAll("<|>", ""));
			System.out.println("Produção: " + producaoFormatada(p));
			
			logPilha.setProducao(producaoFormatada(p));
			
			boolean isEpsilon = false;
			for (int i = p.getSimbolos().size() - 1; i >= 0; --i) {
				Simbolo simbolo = p.getSimbolos().get(i);
				if (!(simbolo instanceof Epsilon)) {	
					ArvoreAnalise arvore = new ArvoreAnalise(new Token(simbolo));
					pilhaParsing.offerFirst(new PilhaDerivacaoSimbolo(simbolo, arvore));
				} else {
					isEpsilon = true;
				}
			}
			
			System.out.println("Valor do topo: " + getValorPilha(pilhaParsing));
			
			logPilha.setAcao(AcaoSintatica.GERA);
			logPilha.setValorPilha(getValorPilha(pilhaParsing));
			pilhaParsingLog.add(logPilha);
			
			if (!isEpsilon) {
				Iterator<PilhaDerivacaoSimbolo> iter = pilhaParsing.iterator();
				for (int i = 0; i < p.getSimbolos().size(); ++i) {
					topo.getArvore().getFilhos().add(iter.next().getArvore());
				}
			} else {
				topo.getArvore().getFilhos().add(null);
			}
			
		}
	}
	
	public ArvoreAnalise parse(List<Token> tokens) throws AnalisadorSintaticoException {
		for (Token token : tokens) {
			proximoTerminal(token);
		}
		return parseCompleto();
	}
	
	public ArvoreAnalise parseCompleto() throws AnalisadorSintaticoException {
		Token eof = new Token();
		eof.setSimbolo(new EOF());
		ArvoreAnalise arvore = processarSimbolo(eof);
		assert arvore != null;
		return arvore;
	}
	
	public void proximoTerminal(Token t) throws AnalisadorSintaticoException {
		processarSimbolo(t);
	}
	
	private String getValorPilha(Deque<PilhaDerivacaoSimbolo> pilha) {
		StringBuilder sb = new StringBuilder();
		List<PilhaDerivacaoSimbolo> reverse = new ArrayList<>(pilha);
		Collections.reverse(reverse);
		for (PilhaDerivacaoSimbolo pds : reverse) {
			sb.append(" " + pds.getSimbolo().toString() + " ");
		}
		return sb.toString().replaceAll("<|>", "");
	}

	public List<PilhaParsingLog> getPilhaParsingLog() {
		return pilhaParsingLog;
	}
	
	private String producaoFormatada(Producao p) {
		StringBuilder sb = new StringBuilder();
		sb.append(p.getNt().getValor().replaceAll("<|>", ""));
		sb.append(" ::= ");
		for (Simbolo s : p.getSimbolos()) {
			sb.append(" " + s.getValor() + " ");
		}
		
		return sb.toString().replaceAll("<|>", "");
	}
}
