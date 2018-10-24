package io.github.paulorg5.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.lexico.TokenCompleto;
import io.github.paulorg5.ll1.ChaveTabela;
import io.github.paulorg5.ll1.PilhaParsingLog;

public class RetornoVO {

	private List<RegistroFirstFollowVO> firstFollow = new ArrayList<>();

	private TabelaMovimentoVO tabelaMovimento;

	private String gramaticaInformada;

	private String gramaticaSemRecursao;

	private String gramaticaFatorada;

	private String gramaticaFinal;

	private List<TokenCompleto> tokensClassificados;

	private List<RegraAnalisadorLexicoVO> regrasAnalisadorLexico = new ArrayList<>();

	private ArvoreAnaliseVO arvoreAnalise;

	private List<PilhaParsingLog> pilhaParsing = new ArrayList<>();

	public void setFirstFollow(Map<NaoTerminal, Set<Simbolo>> first, Map<NaoTerminal, Set<Simbolo>> follow) {
		criarRegistrosFirstFollow(first, follow);
	}

	public void setTabelaMovimento(Map<ChaveTabela, Producao> tabelaMovimento) {
		this.tabelaMovimento = new TabelaMovimentoVO(tabelaMovimento);
	}

	public RetornoVO() {
	}

	private void criarRegistrosFirstFollow(Map<NaoTerminal, Set<Simbolo>> first,
			Map<NaoTerminal, Set<Simbolo>> follow) {
		for (NaoTerminal nt : first.keySet()) {
			firstFollow.add(new RegistroFirstFollowVO(nt.getValor(), getFirstFollowString(first.get(nt)),
					getFirstFollowString(follow.get(nt))));
		}
	}

	private String getFirstFollowString(Set<Simbolo> simbolos) {
		StringBuilder sb = new StringBuilder();
		List<Simbolo> listaSimbolos = new ArrayList<>(simbolos);
		sb.append("{");
		for (Simbolo s : listaSimbolos) {
			sb.append(s.toString());
			if (listaSimbolos.indexOf(s) != listaSimbolos.size() - 1) {
				sb.append(", ");
			}
		}
		sb.append("}");
		return sb.toString();
	}

	public List<RegistroFirstFollowVO> getFirstFollow() {
		return firstFollow;
	}

	public void setFirstFollow(List<RegistroFirstFollowVO> firstFollow) {
		this.firstFollow = firstFollow;
	}

	public TabelaMovimentoVO getTabelaMovimento() {
		return tabelaMovimento;
	}

	public void setTabelaMovimento(TabelaMovimentoVO tabelaMovimento) {
		this.tabelaMovimento = tabelaMovimento;
	}

	public String getGramaticaInformada() {
		return gramaticaInformada;
	}

	public void setGramaticaInformada(String gramaticaInformada) {
		this.gramaticaInformada = gramaticaInformada;
	}

	public String getGramaticaSemRecursao() {
		return gramaticaSemRecursao;
	}

	public void setGramaticaSemRecursao(String gramaticaSemRecursao) {
		this.gramaticaSemRecursao = gramaticaSemRecursao;
	}

	public String getGramaticaFatorada() {
		return gramaticaFatorada;
	}

	public void setGramaticaFatorada(String gramaticaFatorada) {
		this.gramaticaFatorada = gramaticaFatorada;
	}

	public String getGramaticaFinal() {
		return gramaticaFinal;
	}

	public void setGramaticaFinal(String gramaticaFinal) {
		this.gramaticaFinal = gramaticaFinal;
	}

	public List<TokenCompleto> getTokensClassificados() {
		return tokensClassificados;
	}

	public void setTokensClassificados(List<TokenCompleto> tokensClassificados) {
		this.tokensClassificados = tokensClassificados;
	}

	public List<RegraAnalisadorLexicoVO> getRegrasAnalisadorLexico() {
		return regrasAnalisadorLexico;
	}

	public void setRegrasAnalisadorLexico(List<RegraAnalisadorLexicoVO> regrasAnalisadorLexico) {
		this.regrasAnalisadorLexico = regrasAnalisadorLexico;
	}

	public ArvoreAnaliseVO getArvoreAnalise() {
		return arvoreAnalise;
	}

	public void setArvoreAnalise(ArvoreAnaliseVO arvoreAnalise) {
		this.arvoreAnalise = arvoreAnalise;
	}

	public List<PilhaParsingLog> getPilhaParsing() {
		return pilhaParsing;
	}

	public void setPilhaParsing(List<PilhaParsingLog> pilhaParsing) {
		this.pilhaParsing = pilhaParsing;
	}

}
