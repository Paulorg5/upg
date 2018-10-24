package io.github.paulorg5.exceptions;

public class ArquivoDescricaoParserException extends RuntimeException {
	private static final long serialVersionUID = -184347670384826947L;

	private Integer colunaInicial;

	private Integer colunaFinal;

	private Integer linha;

	public ArquivoDescricaoParserException(String mensagem) {
		super(mensagem);
	}

	public ArquivoDescricaoParserException(String mensagem, Integer linha, Integer colunaInicial, Integer colunaFinal) {
		super(montarStringComLinhaEColunas(mensagem, linha, colunaInicial, colunaFinal));
		this.linha = linha;
		this.colunaInicial = colunaInicial;
		this.colunaFinal = colunaFinal;
	}

	private static String montarStringComLinhaEColunas(String mensagem, Integer linha, Integer colunaInicial,
			Integer colunaFinal) {
		StringBuilder sb = new StringBuilder();
		sb.append(mensagem);
		sb.append(" Em ");
		sb.append("Linha: " + linha);
		sb.append(" Coluna: " + colunaInicial + " - " + colunaFinal);
		return sb.toString();
	}

	public Integer getColunaInicial() {
		return colunaInicial;
	}

	public Integer getColunaFinal() {
		return colunaFinal;
	}

	public Integer getLinha() {
		return linha;
	}
}
