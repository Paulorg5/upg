
package io.github.paulorg5.gerador;

public class Parametro {

	private String chave;

	private String valor;

	public Parametro(String chave, String valor) {
		super();
		this.chave = chave;
		this.valor = valor;
	}

	public String getChave() {
		return chave;
	}

	public void setChave(String chave) {
		this.chave = chave;
	}

	public String getValor() {
		return valor;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}
}
