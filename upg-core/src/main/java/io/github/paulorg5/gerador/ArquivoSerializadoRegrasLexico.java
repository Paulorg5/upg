package io.github.paulorg5.gerador;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectOutputStream;

import com.fulmicoton.multiregexp.Lexer;

import io.github.paulorg5.arquivodescricao.domain.ArquivoDescricao;
import io.github.paulorg5.arquivodescricao.domain.RegraLexico;
import io.github.paulorg5.lexico.TipoTokenPredefinido;
import io.github.paulorg5.lexico.TokenCompleto;

public class ArquivoSerializadoRegrasLexico implements Arquivo {

	private static final String CAMINHO_NO_ZIP = "src/main/resources";

	private static final String NOME_ARQUIVO = "Lexer.ser";

	private Lexer<TokenCompleto> lexer = new Lexer<>();
	
	public ArquivoSerializadoRegrasLexico(ArquivoDescricao arquivoDescricao) {
		criarLexer(arquivoDescricao);
	}
	
	private void criarLexer(ArquivoDescricao arquivoDescricao) {
		lexer.addRule(new TokenCompleto(TipoTokenPredefinido.WHITESPACE.name(), 
				TipoTokenPredefinido.WHITESPACE.isIgnorar()), TipoTokenPredefinido.WHITESPACE.getRegex());
		
		for (RegraLexico regra : arquivoDescricao.getConteudoLexico().getRegras()) {
			lexer.addRule(new TokenCompleto(regra.getId(), regra.contemIgnorar()), regra.getRegex());
		}
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
			
			oos.writeObject(this.lexer);
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
