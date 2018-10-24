package io.github.paulorg5.ll1;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;

public class TabelaMovimentoLL1 implements Serializable {
	private static final long serialVersionUID = -9103047796721741512L;

	private NaoTerminal ntInicial;

	private Map<ChaveTabela, Producao> tabelaMovimentos = new HashMap<ChaveTabela, Producao>();

	public NaoTerminal getNtInicial() {
		return ntInicial;
	}

	public void setNtInicial(NaoTerminal ntInicial) {
		this.ntInicial = ntInicial;
	}

	public Map<ChaveTabela, Producao> getTabelaMovimentos() {
		return tabelaMovimentos;
	}

	public void setTabelaMovimentos(Map<ChaveTabela, Producao> tabelaMovimentos) {
		this.tabelaMovimentos = tabelaMovimentos;
	}
}
