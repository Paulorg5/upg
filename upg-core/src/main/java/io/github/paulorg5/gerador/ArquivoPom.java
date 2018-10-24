
package io.github.paulorg5.gerador;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.github.paulorg5.arquivodescricao.domain.ConteudoHeader;
import io.github.paulorg5.arquivodescricao.domain.TipoToken;

public class ArquivoPom extends AbstractArquivo implements Arquivo {

	private static final String CAMINHO_TEMPLATE = "templates_arquivos/pom_xml.st";

	private static final String CAMINHO_NO_ZIP = "";

	private static final String NOME_ARQUIVO = "pom.xml";

	private ConteudoHeader conteudoHeader;

	public ArquivoPom(ConteudoHeader conteudoHeader) {
		super(CAMINHO_TEMPLATE);
		this.conteudoHeader = conteudoHeader;
	}

	@Override
	public String getCaminhoNoZip() {
		return CAMINHO_NO_ZIP + NOME_ARQUIVO;
	}

	@Override
	public InputStream getInputStream() {
		return new ByteArrayInputStream(gerar().getBytes());
	}

	@Override
	protected List<Parametro> getParametros() {
		List<Parametro> parametros = new ArrayList<Parametro>();
		parametros.add(new Parametro("<pacote>",
				conteudoHeader.getConfiguracaoAPartir(TipoToken.KEYWORD_PACOTE).getValor()));
		parametros.add(new Parametro("<nomeProjeto>",
				conteudoHeader.getConfiguracaoAPartir(TipoToken.KEYWORD_NOME_PROJETO).getValor()));
		parametros.add(new Parametro("<versao>",
				conteudoHeader.getConfiguracaoAPartir(TipoToken.KEYWORD_VERSAO).getValor()));
		return parametros;
	}
}
