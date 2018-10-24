package io.github.paulorg5.lexico;

import java.io.IOException;

import java.util.ArrayList;
import java.util.List;

import com.fulmicoton.multiregexp.Lexer;
import com.fulmicoton.multiregexp.ScanException;
import com.fulmicoton.multiregexp.Scanner;

import io.github.paulorg5.arquivodescricao.domain.RegraLexico;
import io.github.paulorg5.exceptions.AnalisadorLexicoException;
public class GeradorAnalisadorLexico {

	private static Lexer<TokenCompleto> lexer = new Lexer<>();
	
	private static List<TokenCompleto> tokens = new ArrayList<>();
	
	private GeradorAnalisadorLexico() { }
	
	public static List<TokenCompleto> classificar(List<RegraLexico> regras, String conteudo) 
			throws AnalisadorLexicoException {
		
		if (conteudo.isEmpty()) {
			throw new AnalisadorLexicoException
			("Entrada está vazia, não será realizado parsing!");
		}
		
		lexer = new Lexer<>();
		tokens = new ArrayList<>();
		
		lexer.addRule(new TokenCompleto(TipoTokenPredefinido.WHITESPACE.name(), 
				TipoTokenPredefinido.WHITESPACE.isIgnorar()), 
				TipoTokenPredefinido.WHITESPACE.getRegex());
		
		for (RegraLexico regra : regras) {
			lexer.addRule(new TokenCompleto(regra.getId(), 
					regra.contemIgnorar()), regra.getRegex());
		}
		
		Scanner<TokenCompleto> scanner = null;
		try {
			scanner = lexer.scannerFor(conteudo);
		} catch (IllegalArgumentException e) {
			throw new AnalisadorLexicoException(e.getMessage());
		}
		
		try {
			while(scanner.next()) {
				if (!scanner.type.isIgnorar()) {
					tokens.add(new TokenCompleto(
							scanner.type.getTipoString(), 
							scanner.type.isIgnorar(), 
							scanner.tokenString().toString(), 
							scanner.startColumnToken, 
							scanner.endColumnToken, 
							scanner.line));
				}
			}
		} catch (ScanException e) {
			throw new AnalisadorLexicoException(e.getMessage());
		} catch (IOException e) {
			throw new AnalisadorLexicoException(e.getMessage());
		}

		return tokens;
	}

}
