package io.github.paulorg5.gerador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Simbolo;

public class ArquivoSerializadoConjuntoFirst implements Arquivo {

	private static final String CAMINHO_NO_ZIP = "src/main/resources";

	private static final String NOME_ARQUIVO = "First.ser";

	private Map<NaoTerminal, Set<Simbolo>> first;

	public ArquivoSerializadoConjuntoFirst(Map<NaoTerminal, Set<Simbolo>> first) {
		this.first = first;
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

			oos.writeObject(this.first);
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
