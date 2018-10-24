package io.github.paulorg5.lexico;

public enum TipoTokenPredefinido {

	WHITESPACE("[ \n\t\r]+", true);

	private String regex;

	private boolean ignorar;

	private TipoTokenPredefinido(String regex, boolean ignorar) {
		this.regex = regex;
		this.ignorar = ignorar;
	}

	public String getRegex() {
		return regex;
	}

	public boolean isIgnorar() {
		return ignorar;
	}

}
