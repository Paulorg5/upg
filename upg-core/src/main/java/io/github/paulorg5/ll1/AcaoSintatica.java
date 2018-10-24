package io.github.paulorg5.ll1;

public enum AcaoSintatica {

	CASAMENTO("Casamento"), ACEITA("Aceita"), GERA("Gera"), ERRO("Erro"), NENHUM("");

	private String descricao;

	private AcaoSintatica(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
