package io.github.paulorg5.gerador;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.List;

import org.apache.commons.io.IOUtils;

public abstract class AbstractArquivo {

	private String caminhoTemplate;

	public AbstractArquivo(String caminhoTemplate) {
		super();
		this.caminhoTemplate = caminhoTemplate;
	}

	public String gerar() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			InputStream is = classLoader.getResourceAsStream(caminhoTemplate);

			StringWriter writer = new StringWriter();
			IOUtils.copy(is, writer, Charset.forName("UTF-8"));
			String arquivo = writer.toString();

			for (Parametro parametro : getParametros()) {
				arquivo = arquivo.replaceAll(parametro.getChave(), parametro.getValor());
			}
			return arquivo;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}

	}

	public String gerar(String conteudoArquivo) {
		for (Parametro parametro : getParametros()) {
			conteudoArquivo = conteudoArquivo.replaceAll(parametro.getChave(), parametro.getValor());
		}
		return conteudoArquivo;
	}
	
	protected String getNomePacoteComBarra(String pacote) {
		return pacote.replace(".", "/");
	}

	protected abstract List<Parametro> getParametros();
}
