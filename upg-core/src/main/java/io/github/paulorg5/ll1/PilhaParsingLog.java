package io.github.paulorg5.ll1;

public class PilhaParsingLog {

	private String valorPilha;

	private String valorEntrada;

	private String producao;

	private AcaoSintatica acao;

	private String entradaParcial;

	private Integer colunaInicial;

	private Integer colunaFinal;

	private Integer linha;

	public void setPosicao(Integer colunaInicial, Integer colunaFinal, Integer linha) {
		this.colunaInicial = colunaInicial;
		this.colunaFinal = colunaFinal;
		this.linha = linha;
	}

	public PilhaParsingLog() {
		super();
	}

	public PilhaParsingLog(String valorPilha, String valorEntrada, String producao, AcaoSintatica acao) {
		super();
		this.valorPilha = valorPilha;
		this.valorEntrada = valorEntrada;
		this.producao = producao;
		this.acao = acao;
	}

	public String getValorPilha() {
		return valorPilha;
	}

	public void setValorPilha(String valorPilha) {
		this.valorPilha = valorPilha;
	}

	public String getValorEntrada() {
		return valorEntrada;
	}

	public void setValorEntrada(String valorEntrada) {
		this.valorEntrada = valorEntrada;
	}

	public String getProducao() {
		return producao;
	}

	public void setProducao(String producao) {
		this.producao = producao;
	}

	public AcaoSintatica getAcao() {
		return acao;
	}

	public void setAcao(AcaoSintatica acao) {
		this.acao = acao;
	}

	public String getEntradaParcial() {
		return entradaParcial;
	}

	public void setEntradaParcial(String entradaParcial) {
		this.entradaParcial = entradaParcial;
	}

	public Integer getColunaInicial() {
		return colunaInicial;
	}

	public void setColunaInicial(Integer colunaInicial) {
		this.colunaInicial = colunaInicial;
	}

	public Integer getColunaFinal() {
		return colunaFinal;
	}

	public void setColunaFinal(Integer colunaFinal) {
		this.colunaFinal = colunaFinal;
	}

	public Integer getLinha() {
		return linha;
	}

	public void setLinha(Integer linha) {
		this.linha = linha;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Valor Pilha:" + valorPilha);
		sb.append("\n");
		sb.append("Entrada Parcial:" + entradaParcial);
		sb.append("\n");
		sb.append("Valor Entrada:" + valorEntrada);
		sb.append("\n");
		sb.append("Produção escolhida:" + producao);
		sb.append("\n");
		sb.append("Ação sintática: " + acao.getDescricao());
		sb.append("\n");
		return sb.toString();
	}

}
