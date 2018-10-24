package <pacote>;

import java.util.List;

import <pacote>.lexico.AnalisadorLexico;
import <pacote>.sintatico.AnalisadorSintatico;
import <pacote>.utils.ArvoreUtils;
import io.github.paulorg5.ast.No;
import io.github.paulorg5.ast.Token;

public class Main {
	public static void main(String[] args) {
		new Main("");
	}
	
	public Main(String entrada) {
		try {
			AnalisadorLexico scanner = new AnalisadorLexico();
			AnalisadorSintatico parser = new AnalisadorSintatico();
			
			List<Token> tokens = scanner.classificar(entrada);
			
			for (Token token : tokens) {
				System.out.println(token.toString());
			}
			
			No arvore = parser.parse(tokens);
			System.out.println("Vai visitar toda a árvore");
			ArvoreUtils.visitar(arvore);
			System.out.println("Vai visitar somente as produções");
			ArvoreUtils.visitarProducao(arvore);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
