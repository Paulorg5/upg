package io.github.paulorg5.gerador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.paulorg5.arquivodescricao.domain.ConteudoHeader;
import io.github.paulorg5.arquivodescricao.domain.TipoToken;

public class ArquivoLeitorResourceSerializado extends AbstractArquivo implements Arquivo {

	private static final String CAMINHO_TEMPLATE = "templates_arquivos/leitor_resource_serializado_java.st";

	private static final String CAMINHO_NO_ZIP = "src/main/java/";

	private static final String PACOTE = ".utils";

	private static final String NOME_ARQUIVO = "LeitorResourceSerializado.java";

	private ConteudoHeader conteudoHeader;
	
	public ArquivoLeitorResourceSerializado(ConteudoHeader conteudoHeader) {
		super(CAMINHO_TEMPLATE);
		this.conteudoHeader = conteudoHeader;
	}

	@Override
	public String getCaminhoNoZip() {
		return CAMINHO_NO_ZIP + getNomePacoteComBarra(getPacote() + PACOTE) + "/" + NOME_ARQUIVO;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(gerar().getBytes());
	}

	@Override
	protected List<Parametro> getParametros() {
		List<Parametro> parametros = new ArrayList<>();
		parametros.add(new Parametro("<pacote>", getPacote()));
		return parametros;
	}

	private String getPacote() {
		return conteudoHeader.getConfiguracaoAPartir(TipoToken.KEYWORD_PACOTE).getValor();
	}
}
