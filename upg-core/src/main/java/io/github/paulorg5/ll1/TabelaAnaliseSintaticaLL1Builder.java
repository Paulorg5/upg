package io.github.paulorg5.ll1;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.exceptions.GeracaoTabelaMovimentoException;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;

public class TabelaAnaliseSintaticaLL1Builder {

	private Map<NaoTerminal, Set<Simbolo>> first;
	
	private Map<NaoTerminal, Set<Simbolo>> follow;
	
	private Gramatica gramatica;
	
	private Map<ChaveTabela, Producao> tabela = new HashMap<>();
	
	public TabelaAnaliseSintaticaLL1Builder comParametros(Map<NaoTerminal, Set<Simbolo>> first, Map<NaoTerminal, Set<Simbolo>> follow, 
			Gramatica gramatica) {
		this.first = first;
		this.follow = follow;
		this.gramatica = gramatica;
		return this;
	}
	
	public Map<ChaveTabela, Producao> getTabela() throws GeracaoTabelaMovimentoException {
		for (Producao p : gramatica.getProducoes()) {
			Set<Simbolo> firstA = Conjuntos.getInstance().firstDosSimbolos(p.getSimbolos(), first);
			for (Simbolo simbolo : firstA) {
				
				if (simbolo.equals(new Epsilon())) {
					Set<Simbolo> followA = follow.get(p.getNt());
					for (Simbolo simboloFollow : followA) {
						addRegraNaTabela(p.getNt(), simboloFollow, p);
					}
				} else {
					addRegraNaTabela(p.getNt(), simbolo, p);
				}
			}
		}
		
		return tabela;
	}
	
	private void addRegraNaTabela(NaoTerminal nt, Simbolo s, Producao p) throws GeracaoTabelaMovimentoException {
		Producao producaoAdicionada = tabela.put(new ChaveTabela(nt, s), p);
		if (producaoAdicionada != null) {
			StringBuilder sb = new StringBuilder();
			sb.append("A gramática não é LL1!");
			sb.append("\n");
			sb.append("Foi identificado conflito para a chave {");
			sb.append(nt.getValor());
			sb.append(", ");
			sb.append(s.getValor());
			sb.append(" Onde seria adicionado a produção " + p.getNt().getValor() + " ::= " + p.getSimbolos().toString());
			sb.append(" mas já exisita a produção: " + producaoAdicionada.getNt() + " ::= " + p.getSimbolos().toString());
			throw new GeracaoTabelaMovimentoException(sb.toString());
		}
	}
}
