package io.github.paulorg5.arquivodescricao.domain;

public class Configuracao {
	private TipoToken nome;

	private String valor;

	public Configuracao(TipoToken nome) {
		super();
		this.nome = nome;
	}

	public Configuracao(TipoToken nome, String valor) {
		super();
		this.nome = nome;
		this.valor = valor;
	}

	public TipoToken getNome() {
		return nome;
	}

	public String getValor() {
		return valor;
	}

	public void setNome(TipoToken nome) {
		this.nome = nome;
	}

	public void setValor(String valor) {
		this.valor = valor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Configuracao other = (Configuracao) obj;
		if (nome != other.nome)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Configuracao [nome=" + nome + ", valor=" + valor + "]";
	}
}