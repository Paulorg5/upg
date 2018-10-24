package com.fulmicoton.multiregexp;

/* https://github.com/fulmicoton/multiregexp */
public class ScanException extends Exception {
	private static final long serialVersionUID = -8218567577514194987L;

	private String context;

	private int start;

	private int end;

	private int line;
	
	private String value;
	
	public ScanException(String context, int startColumnToken, int endColumnToken, int line, String value) {
		super(createMsg(context, startColumnToken, endColumnToken, line, value));
		this.context = context;
		this.start = startColumnToken;
		this.end = endColumnToken;
		this.line = line;
		this.value = value;
	}

	private static String createMsg(String context, int start, int end, int line, String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("Não foi encontrado nenhum tipo de Token em (");
		sb.append("Linha: " + line);
		sb.append(" Coluna: " + start + " - " + end + ")");
		sb.append(" Contexto: " + context);
		sb.append(" Valor: " + value);
		return sb.toString();
	}

	public String getContext() {
		return context;
	}

	public int getStart() {
		return start;
	}

	public int getEnd() {
		return end;
	}

	public int getLine() {
		return line;
	}

	public String getValue() {
		return value;
	}
}
