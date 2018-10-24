package io.github.paulorg5.domain;

import java.io.Serializable;

public class TesteRegExp implements Serializable {
	private static final long serialVersionUID = -5790533842526850162L;

	private String regexpTeste;

	private String stringTesteRegexp;

	public String getRegexpTeste() {
		return regexpTeste;
	}

	public void setRegexpTeste(String regexpTeste) {
		this.regexpTeste = regexpTeste;
	}

	public String getStringTesteRegexp() {
		return stringTesteRegexp;
	}

	public void setStringTesteRegexp(String stringTesteRegexp) {
		this.stringTesteRegexp = stringTesteRegexp;
	}

}
