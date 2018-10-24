package io.github.paulorg5.vo;

public class RegraAnalisadorLexicoVO {

	private String id;

	private String regex;

	private boolean ignorar;

	public RegraAnalisadorLexicoVO(String id, String regex, boolean ignorar) {
		super();
		this.id = id;
		this.regex = regex;
		this.ignorar = ignorar;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}

	public boolean isIgnorar() {
		return ignorar;
	}

	public void setIgnorar(boolean ignorar) {
		this.ignorar = ignorar;
	}
}
