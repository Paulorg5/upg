package io.github.paulorg5.glc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FatoradorGramatica {

	public static Gramatica fatorar(Gramatica gramatica) {
		List<NaoTerminal> naoTerminais = new ArrayList<>(gramatica.getNaoTerminais());
		
		for (int i = 0; i < naoTerminais.size(); i++) {
			NaoTerminal nt = naoTerminais.get(i);
			System.out.println("Vai analisar o não terminal " + nt);
			
			Map<Simbolo, List<Producao>> producoesComPrefixoComum = 
					getProducoesComPrefixoComum(gramatica.getProducoesDoNt(nt));
			
			System.out.println("Para o não terminal" + nt + " encontrou: ");
			for (Map.Entry<Simbolo, List<Producao>> entry : producoesComPrefixoComum.entrySet()) {
				System.out.println(entry.getKey());
				for (Producao p : entry.getValue()) {
					System.out.println(p);
				}
			}
			
			if (!producoesComPrefixoComum.isEmpty()) {
				for (Map.Entry<Simbolo, List<Producao>> entry : producoesComPrefixoComum.entrySet()) {
					NaoTerminal ntNovo = new NaoTerminal(nt.getValor() + "_NOVO_" + entry.getKey().getValor());
					
					List<Producao> producoesComecandoComSimboloX = 
							getProducoesComecandoComSibolo(entry.getKey(), gramatica.getProducoesDoNt(nt));
					for (Producao p : producoesComecandoComSimboloX) {
						Producao pNova = new Producao();
						pNova.setNt(ntNovo);
						pNova.setSimbolos(new ArrayList<>(p.getSimbolos().subList(1, p.getSimbolos().size())));
						gramatica.getProducoes().add(pNova);
						gramatica.getNaoTerminais().add(ntNovo);
						
						
						gramatica.getProducoes().remove(p);
					}
					
					Producao producaoFatorada = new Producao();
					producaoFatorada.setNt(nt);
					producaoFatorada.setSimbolos(Arrays.asList(entry.getKey(), ntNovo));
					gramatica.getProducoes().add(producaoFatorada);
					
					Map<Simbolo, List<Producao>> producoesDeNtNovoComPrefixoComum = new HashMap<>();
					
					do {
						producoesDeNtNovoComPrefixoComum = 
								getProducoesComPrefixoComum(gramatica.getProducoesDoNt(ntNovo));
						
						if (!producoesDeNtNovoComPrefixoComum.isEmpty()) {
							System.out.println("Ainda ficou fatorada");
							for (Map.Entry<Simbolo, List<Producao>> e : producoesDeNtNovoComPrefixoComum.entrySet()) {
								System.out.println(e.getKey());
								for (Producao p : e.getValue()) {
									System.out.println(p);
								}
							}
							
							for (Map.Entry<Simbolo, List<Producao>> e : producoesDeNtNovoComPrefixoComum.entrySet()) {
								if (!e.getValue().isEmpty()) {
									Simbolo simboloDuplicado = e.getValue().get(0).getSimbolos().get(0);
									
									for (Producao producao : e.getValue()) {
										producao.getSimbolos().remove(0);
										
										if (producao.getSimbolos().isEmpty()) {
											gramatica.getProducoes().remove(producao);
											adicionaProducaoEpsilonSeNaoTiverPara(ntNovo, gramatica);
										}
									}
									
									adicionaSimboloRemovidoEmProducaoNtEComecaComSimbolo(nt, entry.getKey(), 
											simboloDuplicado, gramatica);
								}
							}
							
						}
					} while(!producoesDeNtNovoComPrefixoComum.isEmpty());
					
				}
			}
			
		}
		
		
		for (Producao p : gramatica.getProducoes()) {
			if (p.getSimbolos().isEmpty()) {
				p.setSimbolos(Arrays.asList(new Epsilon()));
			}
		}
		
		return gramatica;
	}
	
	private static void adicionaSimboloRemovidoEmProducaoNtEComecaComSimbolo(NaoTerminal nt, Simbolo simbolo, 
			Simbolo simboloRemovido, Gramatica gramatica) {
		
		System.out.println("Chegou nt = " + nt + " simbolo = " + simbolo + " simboloRemovido = " + simboloRemovido);
		
		for (Producao p : gramatica.getProducoesDoNt(nt)) {
			if (p.getSimbolos().get(0).equals(simbolo)) {
				System.out.println("Entrou aqui alguma vez?");
				List<Simbolo> menosUltimoSimbolo = new ArrayList<>(p.getSimbolos().subList(0, p.getSimbolos().size() - 1));
				menosUltimoSimbolo.add(simboloRemovido);
				menosUltimoSimbolo.add(p.getSimbolos().get(p.getSimbolos().size() - 1));
				p.setSimbolos(menosUltimoSimbolo);
			}
		}
	}
	
	private static void adicionaProducaoEpsilonSeNaoTiverPara(NaoTerminal ntNovo, Gramatica gramatica) {
		boolean temProducaoEpsilon = false;
		for (Producao p : gramatica.getProducoesDoNt(ntNovo)) {
			if (p.getSimbolos().contains(new Epsilon())) {
				temProducaoEpsilon = true;
			}
		}
		
		if (!temProducaoEpsilon) {
			Producao p = new Producao();
			p.setNt(ntNovo);
			p.setSimbolos(Arrays.asList(new Epsilon()));
			gramatica.getProducoes().add(p);
		}
	}
	
	private static List<Producao> getProducoesComecandoComSibolo(Simbolo s, List<Producao> producoes) {
		List<Producao> resultado = new ArrayList<>();
		
		for (Producao p : producoes) {
			if (p.getSimbolos().get(0).equals(s)) {
				resultado.add(p);
			}
		}
		
		return resultado;
	}
	
	private static Map<Simbolo, List<Producao>> getProducoesComPrefixoComum(List<Producao> producoes) {
		Map<Simbolo, List<Producao>> simboloXProducaoPrefixoComum = new HashMap<>();
		
		for (Producao producao : producoes) {
			if (producao.getSimbolos().contains(new Epsilon())) {
				continue;
			}
			
			if (producao.getSimbolos().isEmpty()) {
				continue;
			}
			
			Simbolo simbolo = producao.getSimbolos().get(0);
			
			if (simboloXProducaoPrefixoComum.containsKey(simbolo)) {
				continue;
			}
			
			List<Producao> producoesParaOSimbolo = new ArrayList<>();
			for (Producao p : producoes) {
				
				if (p.getSimbolos().isEmpty()) {
					continue;
				}
				
				if (p.getSimbolos().get(0).equals(simbolo)) {
					producoesParaOSimbolo.add(p);
				}
			}
			
			if (producoesParaOSimbolo.size() > 1) {
				simboloXProducaoPrefixoComum.put(simbolo, producoesParaOSimbolo);
			}
		}
		
		return simboloXProducaoPrefixoComum;
	}
}
