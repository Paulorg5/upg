package io.github.paulorg5.ll1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;

import io.github.paulorg5.ast.No;
import io.github.paulorg5.ast.NoProducao;
import io.github.paulorg5.ast.NoToken;
import io.github.paulorg5.ast.Token;
import io.github.paulorg5.exceptions.AnalisadorSintaticoException;
import io.github.paulorg5.glc.EOF;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.glc.Terminal;

public class ParserLL1 {

	private Gramatica gramatica;
	
	private Token proximaMarca;
	
	private Iterator<Token> itrTokens;
	
	private Stack<Simbolo> pilha;
	
	private Stack<Elemento> elementos = new Stack<>();
	
	private Map<ChaveTabela, Producao> tabela = new HashMap<ChaveTabela, Producao>();
	
	private Map<NaoTerminal, Set<Simbolo>> first;
	
	private List<PilhaParsingLog> logger = new ArrayList<>();
	
	private Queue<String> entradaParcial = new LinkedList<>();
	
	public ParserLL1(Gramatica gramatica, Map<ChaveTabela, Producao> tabela, Map<NaoTerminal, Set<Simbolo>> first) {
		this.gramatica = gramatica;
		this.tabela = tabela;
		this.first = first;
	}
	
	public No parse(List<Token> tokens) throws AnalisadorSintaticoException {
		if (tokens.isEmpty()) {
			throw new AnalisadorSintaticoException("A entrada está vazia, parse finalizado!");
		}
		
		inicializarEntradaParcial(tokens);
		adicionarEOFNoFimDaEntrada(tokens);
		this.pilha = inicializarPilha();
		
		this.itrTokens = tokens.iterator();
		this.proximaMarca = itrTokens.next();
		
		while (!isEntradaAceita()) {
			
			PilhaParsingLog log = new PilhaParsingLog();
			
			System.out.println("Pilha vai iniciar como " + pilha);
			log.setValorPilha(pilha.toString());
			
			System.out.println("Simbolo que está sendo analiado " + proximaMarca.getValor());
			log.setValorEntrada(proximaMarca.getValor());
			log.setPosicao(proximaMarca.getColunaInicio(), proximaMarca.getColunaFim(), proximaMarca.getLinha());
			
			System.out.println("Entrada completa " + entradaParcial);
			log.setEntradaParcial(entradaParcial.toString());
			
			if (pilha.peek() instanceof Terminal) {
				if (pilha.peek().equals(proximaMarca.getSimbolo())) {
					System.out.println("Casamento entre " + pilha.peek() + " " + proximaMarca.getSimbolo());
					elementos.push(new Elemento(proximaMarca));
					casamento();
					entradaParcial.remove();
					log.setAcao(AcaoSintatica.CASAMENTO);
					System.out.println("Vai adicionar o log 1" + log);
					logger.add(log);
				} else {
					criarMensagemErroCasamento();
				}
			} else if (pilha.peek() instanceof NaoTerminal) {
				Producao p = encontrarProducao();
								
				if (p == null) {
					criarMensagemErroEncontrarProducao();
				}
				
				System.out.println("Encontrou producao " + p);
				log.setProducao(p.getNt() + " ::= " + p.getSimbolos());
				log.setAcao(AcaoSintatica.GERA);
				System.out.println(pilha.peek() + " e " + proximaMarca.getSimbolo());
				
				elementos.push(new Elemento(p));
				
				if (isProducaoComEpsilon(p)) {
					Simbolo simboloRemovido = pilha.pop();
					System.out.println("Simbolo removido pois derivou para o Epsilon " + simboloRemovido);
					System.out.println("Como ficou a pilha " + pilha);
					System.out.println("Como ficou a entrada " + tokens);
				} else {
					gerar(p);
				}
				
				System.out.println("Vai adicionar o log 2 " + log);
				logger.add(log);
			}
			
			if (isEntradaAceita()) {
				PilhaParsingLog logAceita = new PilhaParsingLog();
				logAceita.setAcao(AcaoSintatica.ACEITA);
				logAceita.setValorPilha(pilha.toString());
				logAceita.setValorEntrada(proximaMarca.getValor());
				logAceita.setEntradaParcial(entradaParcial.toString());
				logger.add(logAceita);
				break;
			}
		}
		
		System.out.println("Como ficou o log");
		
		for (PilhaParsingLog log : logger) {
			System.out.println(log);
		}
		
		return criarArvore();
	}
	
	private void inicializarEntradaParcial(List<Token> tokens) {
		for (Token token : tokens) {
			entradaParcial.add(token.getValor());
		}
		entradaParcial.add("EOF");
	}
	
	private void criarMensagemErroEncontrarProducao() throws AnalisadorSintaticoException {
		StringBuilder sb = new StringBuilder();
		sb.append("Falha ao realizar o parser do símbolo ");
		sb.append(proximaMarca.getValor());
		sb.append(" em ");
		sb.append(" Linha: " + proximaMarca.getLinha());
		sb.append(" Coluna: " + proximaMarca.getColunaInicio() + " - " + proximaMarca.getColunaFim());
		sb.append(" Com o topo da pilha em " + pilha.peek().getValor());
		sb.append(" As escolhas possíveis com o topo da pilha são: \n");
		sb.append(getEscolhasPossivesNt(pilha.peek()));
		sb.append("Obs: Possivelmente está faltando um símbolo antes do símbolo informado na mensagem de erro, e que está nas escolhas possíveis de ");
		sb.append(pilha.peek().getValor());
		throw new AnalisadorSintaticoException(sb.toString());
	}
	
	private void criarMensagemErroCasamento() throws AnalisadorSintaticoException {
		StringBuilder sb = new StringBuilder();
		sb.append("Era esperado o símbolo ");
		sb.append(pilha.peek());
		sb.append(", mas foi encontrado ");
		
		if (proximaMarca.getSimbolo().equals(new EOF())) {
			sb.append(proximaMarca.getSimbolo().getValor());
			sb.append(" ao final da entrada");
		} else {
			sb.append(proximaMarca.getSimbolo().getValor());
			sb.append(" em ");
			sb.append(" Linha: " + proximaMarca.getLinha());
			sb.append(" Coluna: " + proximaMarca.getColunaInicio() + " - " + proximaMarca.getColunaFim());
		}
		
		throw new AnalisadorSintaticoException(sb.toString());
	}
	
	private String getEscolhasPossivesNt(Simbolo simbolo) {
		StringBuilder sb = new StringBuilder();
		Set<Simbolo> escolhas = first.get((NaoTerminal) simbolo);
		
		for (Simbolo s : escolhas) {
			sb.append(s + "\n");
		}
		
		return sb.toString();
	}
	
	private void adicionarEOFNoFimDaEntrada(List<Token> tokens) {
		Token eof = new Token(new EOF());
		eof.setValor("EoF ($)");
		tokens.add(eof);
	}
	
	private boolean isProducaoComEpsilon(Producao p) {
		return p.getSimbolos().contains(new Epsilon());
	}
	
	private void gerar(Producao p) {
		pilha.pop();
		colocaProducaoInvertidaNaPilha(p);
	}
	
	private No criarArvore() {
		System.out.println(elementos);
		Stack<No> nos = new Stack<No>();
		
		do {
			Elemento elemento = elementos.pop();
			
			if (elemento.getProducao() != null) {
				NoProducao noProducao = new NoProducao(elemento.getProducao());
				if (!noProducao.getProducao().getSimbolos().contains(new Epsilon())) {
					while (!noProducao.isCheio()) {
						noProducao.addFilho(nos.pop());
					}
				} else {
					noProducao.addFilho(new NoToken(new Token(new Epsilon()), true));
				}
				
				nos.push(noProducao);
			} else {
				nos.push(new NoToken(elemento.getToken(), false));
			}
			
		} while (!elementos.isEmpty());
		
		return nos.pop();
	}
	
	private void colocaProducaoInvertidaNaPilha(Producao p) {
		for (int i = p.getSimbolos().size() - 1; i >= 0; --i) {
			pilha.push(p.getSimbolos().get(i));
		}
	}
	
	private Producao encontrarProducao() {
		return tabela.get(new ChaveTabela((NaoTerminal) pilha.peek(), (Terminal) proximaMarca.getSimbolo()));
	}
	
	private void casamento() {
		pilha.pop();
		avancaEntrada();
	}
	
	private void avancaEntrada() {
		this.proximaMarca = this.itrTokens.next();
	}
	
	private boolean isEntradaAceita() {
		return pilha.peek().equals(new EOF()) && proximaMarca.getSimbolo().equals(new EOF());
	}
	
	private Stack<Simbolo> inicializarPilha() {
		Stack<Simbolo> pilha = new Stack<>();
		pilha.push(new EOF());
		pilha.push(gramatica.getNtInicial());
		return pilha;
	}

	public List<PilhaParsingLog> getLogger() {
		return logger;
	}
}
