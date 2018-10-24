package io.github.paulorg5.ll1;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.github.paulorg5.glc.EOF;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.glc.Terminal;

public class Conjuntos {

	private Conjuntos() { }

	private static class ConjuntosInstance {
		private static final Conjuntos INSTANCE = new Conjuntos();
	}
	
	public static Conjuntos getInstance() {
		return ConjuntosInstance.INSTANCE;
	}
	
	private Map<NaoTerminal, Set<Simbolo>> inicializarConjunto(Gramatica g) {
		Map<NaoTerminal, Set<Simbolo>> conjunto = new HashMap<>();
		for (NaoTerminal nt : g.getNaoTerminais()) {
			conjunto.put(nt, new HashSet<Simbolo>());
		}
		
		return conjunto;
	}
	
	public Map<NaoTerminal, Set<Simbolo>> first(Gramatica g) {
		Map<NaoTerminal, Set<Simbolo>> first = inicializarConjunto(g);
		
		boolean deveContinuar;
		do {
			deveContinuar = false;
			for (Producao p : g.getProducoes()) {
				Set<Simbolo> simbolos = firstDosSimbolos(p.getSimbolos(), first);
				
				if (first.get(p.getNt()).addAll(simbolos)) {
					deveContinuar = true;
				}
			}
		} while (deveContinuar);
		
		return first;
	}
	
	public Set<Simbolo> firstDosSimbolos(List<Simbolo> simbolos, Map<NaoTerminal, Set<Simbolo>> conjuntoFirst) {
		Set<Simbolo> first = new HashSet<Simbolo>();
		for (Simbolo s : simbolos) {
			if (s instanceof Terminal || s instanceof Epsilon) {
				first.add(s);
				return first;
			}
			
			NaoTerminal nt = (NaoTerminal) s;
			
			if (!conjuntoFirst.get(nt).contains(new Epsilon())) {
				first.addAll(conjuntoFirst.get(nt));
				return first;
			}
			
			first.addAll(conjuntoFirst.get(nt));
		}
		
		first.add(new Epsilon());
		return first;
	}
	
	public Map<NaoTerminal, Set<Simbolo>> follow(Gramatica g, Map<NaoTerminal, Set<Simbolo>> first) {
		Map<NaoTerminal, Set<Simbolo>> follow = inicializarConjunto(g);
		
		follow.get(g.getNtInicial()).add(new EOF());
		
		boolean deveContinuar;
		
		do {
			deveContinuar = false;
			
			for (Producao p : g.getProducoes()) {
				for (int i = 0; i < p.getSimbolos().size(); ++i) {
					if (p.getSimbolos().get(i) instanceof Terminal || p.getSimbolos().get(i) instanceof Epsilon) {
						continue;
					}
					
					NaoTerminal nt = (NaoTerminal) p.getSimbolos().get(i);
					
					Set<Simbolo> restoDaProducao =  firstDosSimbolos(p.getSimbolos().subList(i + 1, p.getSimbolos().size()), first);
					
					for (Simbolo s:  restoDaProducao) {
						if (s instanceof Epsilon) {
							continue;
						}
						
						if (follow.get(nt).add(s)) {
							deveContinuar = true;
						}
					}
					
					if (restoDaProducao.contains(new Epsilon())) {
						if (follow.get(nt).addAll(follow.get(p.getNt()))) {
							deveContinuar = true;
						}
					}
				}
			}
			
		} while (deveContinuar);
		
		return follow;
	}
}
