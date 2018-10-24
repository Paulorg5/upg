package io.github.paulorg5.regexp;

import dk.brics.automaton.Automaton;
import dk.brics.automaton.RegExp;

public class TestadorRegExp {
	public static boolean testarRegExp(String regExp, String entrada) {
		try {
			RegExp r = new RegExp(regExp);
			Automaton a = r.toAutomaton();
			return a.run(entrada);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Erro na RegExp! A engine de RegExp lança o erro: " + e.getMessage());
		}
	}
}
