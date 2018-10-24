package io.github.paulorg5.gerador;

import java.io.InputStream;

public class ArquivoJarProjeto implements Arquivo {

	private static final String CAMINHO_NO_ZIP = "src/main/resources";

	private static final String NOME_ARQUIVO = "upg-core-1.0.0.jar";
	
	@Override
	public String getCaminhoNoZip() {
		return CAMINHO_NO_ZIP + "/" + NOME_ARQUIVO;
	}

	@Override
	public InputStream getInputStream() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			return classLoader.getResourceAsStream("jar_projeto/" + NOME_ARQUIVO);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			return null;
		}
	}
}
