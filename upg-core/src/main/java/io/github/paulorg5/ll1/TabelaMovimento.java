package io.github.paulorg5.ll1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.exceptions.GeracaoTabelaMovimentoException;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;

public class TabelaMovimento implements Serializable {
	private static final long serialVersionUID = -8925013668811761743L;
	private static Map<ChaveTabela, Producao> tabelaMovimentos = new HashMap<ChaveTabela, Producao>();
	
	public static Map<ChaveTabela, Producao> gerar(Map<NaoTerminal, Set<Simbolo>> first,
			Map<NaoTerminal, Set<Simbolo>> follow, Gramatica g) {
		
		tabelaMovimentos = new HashMap<ChaveTabela, Producao>();
		
		for (Producao p : g.getProducoes()) {
			Set<Simbolo> firstDaProducao = Conjuntos.getInstance().firstDosSimbolos(p.getSimbolos(), first);

			for (Simbolo s : firstDaProducao) {
				if (s instanceof Epsilon) {
					continue;
				}

				if (tabelaMovimentos.put(new ChaveTabela(p.getNt(), s), p) != null) {
					throw new GeracaoTabelaMovimentoException("Conflito para: " + p.getNt() + ", " + s);
				}
			}

			if (firstDaProducao.contains(new Epsilon())) {
				for (Simbolo s : follow.get(p.getNt())) {
					if (tabelaMovimentos.put(new ChaveTabela(p.getNt(), s), p) != null) {
						throw new GeracaoTabelaMovimentoException("Conflito para: " + p.getNt() + ", " + s);
					}
				}
			}
		}

		return tabelaMovimentos;
	}

	public Map<ChaveTabela, Producao> getTabelaMovimentos() {
		return tabelaMovimentos;
	}
	
	public String toString(Map<ChaveTabela, Producao> tabelaMovimento) {
		StringBuilder sb = new StringBuilder();
		sb.append("--- Tabela Movimento ---");

		for (Map.Entry<ChaveTabela, Producao> entry : tabelaMovimento.entrySet()) {
			sb.append(entry.getKey().toString() + " " + entry.getValue().toString());
		}
		return sb.toString();
	}
}
