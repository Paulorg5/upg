
package io.github.paulorg5.gerador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Map;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.ll1.ChaveTabela;
import io.github.paulorg5.ll1.TabelaMovimentoLL1;

public class ArquivoTabelaMovimento implements Arquivo {

	private static final String CAMINHO_NO_ZIP = "src/main/resources";

	private static final String NOME_ARQUIVO = "TabelaMovimento.ser";

	private Map<ChaveTabela, Producao> tabelaMovimento;

	private TabelaMovimentoLL1 tabelaMovimentoLL1 = new TabelaMovimentoLL1();

	private NaoTerminal ntInicial;

	public ArquivoTabelaMovimento(Map<ChaveTabela, Producao> tabelaMovimento, NaoTerminal ntInicial) {
		this.tabelaMovimento = tabelaMovimento;
		this.ntInicial = ntInicial;

		tabelaMovimentoLL1.setNtInicial(this.ntInicial);
		tabelaMovimentoLL1.setTabelaMovimentos(this.tabelaMovimento);
	}

	@Override
	public String getCaminhoNoZip() {
		return CAMINHO_NO_ZIP + "/" + NOME_ARQUIVO;
	}

	@Override
	public InputStream getInputStream() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(this.tabelaMovimentoLL1);
			oos.flush();
			oos.close();

			InputStream is = new ByteArrayInputStream(baos.toByteArray());

			return is;
		} catch (IOException e) {
			System.out.println(e.getMessage());
			return null;
		}
	}

}
