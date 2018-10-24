package io.github.paulorg5.domain;

public class EntradaUsuario {

	private String codigoArquivoDescricao;

	private String codigoTeste;

	public EntradaUsuario() {
		super();
	}

	public EntradaUsuario(String codigoArquivoDescricao) {
		super();
		this.codigoArquivoDescricao = codigoArquivoDescricao;
	}

	public String getCodigoArquivoDescricao() {
		return codigoArquivoDescricao;
	}

	public void setCodigoArquivoDescricao(String codigoArquivoDescricao) {
		this.codigoArquivoDescricao = codigoArquivoDescricao;
	}

	public String getCodigoTeste() {
		return codigoTeste;
	}

	public void setCodigoTeste(String codigoTeste) {
		this.codigoTeste = codigoTeste;
	}
}
