package io.github.paulorg5.glc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidadorGramatica {

	public static boolean validar(final Gramatica gramatica) {
		return !isComCiclos(gramatica) && !isComProducoesEpsilon(gramatica);
	}
	
	private static boolean isComCiclos(final Gramatica gramatica) {
		for (NaoTerminal nt : gramatica.getNaoTerminais()) {
			Set<NaoTerminal> ntsVisitados = new HashSet<>();
			List<NaoTerminal> derivacoesNts = new ArrayList<>();
			derivacoesNts.add(nt);
			
			derivacoesNts = visitarNts
					(nt, ntsVisitados, derivacoesNts, 
							gramatica.getProducoes());
			
			if (derivacoesNts.size() > 1 
					&& derivacoesNts.get(0)
					.equals(derivacoesNts.get(derivacoesNts.size() - 1))) {
				return true;
			}
		}
		
		return false;
	}
	
	private static List<NaoTerminal> visitarNts(NaoTerminal ntSendoAnalisado, 
			Set<NaoTerminal> ntsVisitados, List<NaoTerminal> derivacoesNts, List<Producao> producoes) {
		ntsVisitados.add(ntSendoAnalisado);
		
		System.out.println("Está buscando o não terminal " + ntSendoAnalisado);
		
		for (Producao producao : producoes) {
			if (!producao.getNt().equals(ntSendoAnalisado)) {
				continue;
			}
			
			if (producao.getSimbolos().size() != 1) {
				continue;
			}
			
			if (!(producao.getSimbolos().get(0) instanceof NaoTerminal)) {
				continue;
			}
			
			NaoTerminal proximo = (NaoTerminal) producao.getSimbolos().get(0);
			
			derivacoesNts.add(proximo);
			
			if (ntsVisitados.contains(proximo)) {
				return derivacoesNts;
			}
			
			return visitarNts(proximo, ntsVisitados, derivacoesNts, producoes);
		}
		
		return derivacoesNts;
	}
	
	private static boolean isComProducoesEpsilon(final Gramatica gramatica) {
		List<Producao> producoesComEpsilon = new ArrayList<>();
		for (Producao producao : gramatica.getProducoes()) {
			if (producao.getSimbolos().contains(new Epsilon())) {
				producoesComEpsilon.add(producao);
			}
		}
		
		return !producoesComEpsilon.isEmpty();
	}
}
