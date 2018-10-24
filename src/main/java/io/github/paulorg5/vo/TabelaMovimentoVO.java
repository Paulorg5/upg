package io.github.paulorg5.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Terminal;
import io.github.paulorg5.ll1.ChaveTabela;

public class TabelaMovimentoVO {

	private static final String LABEL_ERRO = "Erro";

	private static final String LABEL_NT = "Não Terminal";

	private List<String> header = new ArrayList<>();

	private List<List<String>> conteudo = new ArrayList<>();

	public TabelaMovimentoVO(Map<ChaveTabela, Producao> tabelaMovimento) {
		gerarHeader(tabelaMovimento);
		gerarConteudo(tabelaMovimento);
	}

	private void gerarHeader(Map<ChaveTabela, Producao> tabelaMovimento) {
		header.add(LABEL_NT);
		for (Map.Entry<ChaveTabela, Producao> entry : tabelaMovimento.entrySet()) {
			if (!header.contains(entry.getKey().getTerminal().toString())) {
				header.add(entry.getKey().getTerminal().toString());
			}
		}
	}

	private void gerarConteudo(Map<ChaveTabela, Producao> tabelaMovimento) {
		for (NaoTerminal nt : getNtsDaTabelaMovimento(tabelaMovimento)) {
			List<String> registro = new ArrayList<>();
			registro.add(nt.getValor());
			for (int i = 1; i < header.size(); i++) {
				Producao p = tabelaMovimento.get(new ChaveTabela(nt, new Terminal(header.get(i))));
				if (p == null) {
					registro.add(LABEL_ERRO);
				} else {
					registro.add(p.toString());
				}
			}
			conteudo.add(registro);
		}
	}

	private List<NaoTerminal> getNtsDaTabelaMovimento(Map<ChaveTabela, Producao> tabelaMovimento) {
		List<NaoTerminal> nts = new ArrayList<>();
		for (Map.Entry<ChaveTabela, Producao> entry : tabelaMovimento.entrySet()) {
			if (!nts.contains(entry.getKey().getNaoTerminal())) {
				nts.add(entry.getKey().getNaoTerminal());
			}
		}

		return nts;
	}

	public List<String> getHeader() {
		return header;
	}

	public void setHeader(List<String> header) {
		this.header = header;
	}

	public List<List<String>> getConteudo() {
		return conteudo;
	}

	public void setConteudo(List<List<String>> conteudo) {
		this.conteudo = conteudo;
	}

}
