package <pacote>.lexico;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fulmicoton.multiregexp.Lexer;
import com.fulmicoton.multiregexp.ScanException;
import com.fulmicoton.multiregexp.Scanner;

import <pacote>.utils.LeitorResourceSerializado;
import io.github.paulorg5.ast.Token;
import io.github.paulorg5.glc.Terminal;
import io.github.paulorg5.lexico.TokenCompleto;

public class AnalisadorLexico {

	private static final String NOME_ARQUIVO_SER_LEXER = "Lexer.ser";
	
	private Lexer<TokenCompleto> lexer;
	
	@SuppressWarnings("unchecked")
	public AnalisadorLexico() {
		this.lexer = (Lexer<TokenCompleto>) LeitorResourceSerializado.getInstance().ler(NOME_ARQUIVO_SER_LEXER);
	}
	
	public List<Token> classificar(String conteudo) throws IllegalArgumentException, ScanException, IOException {
		if (conteudo.isEmpty()) {
			throw new IllegalArgumentException("Entrada vazia, não será realizado a classificação dos Tokens!");
		}
		
		List<Token> tokens = new ArrayList<>();
		Scanner<TokenCompleto> scanner = null;
		try {
			scanner = lexer.scannerFor(conteudo);
		} catch (IllegalArgumentException e) {
			throw e;
		}
		
		try {
			while(scanner.next()) {
				if (!scanner.type.isIgnorar()) {
					
					Token t = new Token();
					t.setLinha(scanner.line);
					t.setColunaInicio(scanner.startColumnToken);
					t.setColunaFim(scanner.endColumnToken);
					t.setValor(scanner.tokenString().toString());
					t.setSimbolo(new Terminal(scanner.type.getTipoString()));
					
					tokens.add(t);
				}
			}
		} catch (ScanException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		}
		
		return tokens;
	}
}
