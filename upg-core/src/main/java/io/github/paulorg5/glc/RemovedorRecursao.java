package io.github.paulorg5.glc;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class RemovedorRecursao {

	public static Gramatica remover(Gramatica gramatica) {
		List<NaoTerminal> ntsOrdenados = new ArrayList<>(gramatica.getNaoTerminais());

		for (int i = 0; i < ntsOrdenados.size(); i++) {
			NaoTerminal ntI = ntsOrdenados.get(i);
			for (int j = 0; j < i; j++) {
				NaoTerminal ntJ = ntsOrdenados.get(j);

				System.out.println("Situação do for: i = " + i + " j = " + j);

				System.out.println("(1) Vai analisar se tem produção na forma " + ntI + " -> " + ntJ + "y");

				List<Producao> producoesNaFormaAiAj = getProducoesNaFormaAiAj(ntI, ntJ, gramatica);
				if (producoesNaFormaAiAj.isEmpty()) {
					System.out.println("(1) Não tem produções na forma " + ntI + " -> " + ntJ + "y");
				} else {
					System.out.println("(1) Tem produções na forma " + ntI + " -> " + ntJ + "y");

					for (Producao p : producoesNaFormaAiAj) {
						System.out.println(p);
					}
				}

				if (!producoesNaFormaAiAj.isEmpty()) {
					List<Producao> producoesDeAj = gramatica.getProducoesDoNt(ntJ);
					System.out.println("Vai substituir pelas produções de Aj " + ntJ + " onde :\n");
					for (Producao producao : producoesDeAj) {
						System.out.println(producao);
					}

					for (Producao producaoFormaAiAj : producoesNaFormaAiAj) {
						System.out.print("Produção " + producaoFormaAiAj + " vai ser substituída por ");

						List<Producao> producaoReplicada = replicarProducoesComBaseTamanho(producaoFormaAiAj,
								producoesDeAj.size());
						List<Producao> producoesFormaAiAjSubstituidas = new ArrayList<>();

						Iterator<Producao> itrProducaoReplicada = producaoReplicada.iterator();
						for (Producao producaoAj : producoesDeAj) {
							Producao producaoAtual = itrProducaoReplicada.next();
							System.out.print(" " + producaoAj);
							producoesFormaAiAjSubstituidas.add(colocarProducaoAjEmAi(producaoAj, producaoAtual));

						}

						gramatica.getProducoes().remove(producaoFormaAiAj);
						gramatica.getProducoes().addAll(new ArrayList<>(producoesFormaAiAjSubstituidas));
						System.out.println("Como ficou as produções substituidas? \n");
						for (Producao p : producoesFormaAiAjSubstituidas) {
							System.out.println(p);
						}

						System.out.println("Agora o ntI tem que produções?\n");
						for (Producao p : gramatica.getProducoesDoNt(ntI)) {
							System.out.println(p);
						}

					}

					System.out.println("\n");
				}

			}

			System.out.println("(2) Vai verificar se tem recursão direta para " + ntI);

			List<Producao> producoesAi = gramatica.getProducoesDoNt(ntI);

			List<Producao> producoesAiSemRecursao = getProducoesAiSemRecursao(producoesAi);
			List<Producao> producoesAiComRecursao = getProducoesAiComRecursao(producoesAi);

			if (producoesAiComRecursao.isEmpty()) {
				System.out.println("(2) Não tem produções com recursão direta para " + ntI);
			} else {
				System.out.println("(2) Tem recursão direta para " + ntI);

				NaoTerminal ntNovo = new NaoTerminal(ntI.getValor() + "_NOVO");
				for (int index = 0; index < producoesAiSemRecursao.size(); index++) {
					Producao producao = producoesAiSemRecursao.get(index);
					producao.getSimbolos().add(ntNovo);

					System.out.println(" A produção " + producao + " foi alterada");
				}

				for (int index = 0; index < producoesAiComRecursao.size(); index++) {
					Producao producao = producoesAiComRecursao.get(index);
					producao.setNt(ntNovo);
					producao.getSimbolos().add(ntNovo);
					producao.setSimbolos(
							new ArrayList<>(producao.getSimbolos().subList(1, producao.getSimbolos().size())));

					System.out.println(" A produção " + producao + " foi alterada");
				}

				Producao producaoVazia = new Producao();
				producaoVazia.setNt(ntNovo);
				producaoVazia.setSimbolos(Arrays.asList(new Epsilon()));

				gramatica.getProducoes().add(producaoVazia);
				gramatica.getNaoTerminais().add(ntNovo);

				System.out.println(" A produção " + producaoVazia + " foi adicionada a gramática");
			}

			System.out.println("Como ficou a gramática com i: " + i + "\n");
			System.out.println(gramatica.toStringView());

		}

		return gramatica;
	}

	private static List<Producao> getProducoesAiComRecursao(List<Producao> producoes) {
		List<Producao> resultado = new ArrayList<>();
		for (Producao producao : producoes) {
			if (isComRecursaoDiretaEsquerda(producao)) {
				resultado.add(producao);
			}
		}
		return resultado;
	}

	private static List<Producao> getProducoesAiSemRecursao(List<Producao> producoes) {
		List<Producao> resultado = new ArrayList<>();
		for (Producao producao : producoes) {
			if (!isComRecursaoDiretaEsquerda(producao)) {
				resultado.add(producao);
			}
		}
		return resultado;
	}

	private static boolean isComRecursaoDiretaEsquerda(Producao producao) {
		return producao.getNt().equals(producao.getSimbolos().get(0));
	}

	private static Producao colocarProducaoAjEmAi(Producao producaoAj, Producao producaoAi) {
		Producao resultado = new Producao();
		resultado.setNt(new NaoTerminal(producaoAi.getNt().getValor()));
		resultado.setSimbolos(new ArrayList<>(producaoAj.getSimbolos()));
		resultado.getSimbolos()
				.addAll(new ArrayList<>(producaoAi.getSimbolos().subList(1, producaoAi.getSimbolos().size())));

		return resultado;
	}

	private static List<Producao> replicarProducoesComBaseTamanho(Producao p, Integer tamanho) {
		List<Producao> resultado = new ArrayList<>();
		for (int i = 0; i < tamanho; i++) {
			resultado.add(p);
		}

		return resultado;
	}

	private static List<Producao> getProducoesNaFormaAiAj(NaoTerminal ntI, NaoTerminal ntJ, Gramatica gramatica) {
		List<Producao> resultado = new ArrayList<>();

		for (Producao producao : gramatica.getProducoes()) {
			if (producao.getNt().equals(ntI)) {
				if (producao.getSimbolos().contains(new Epsilon())
						|| producao.getSimbolos().get(0) instanceof Terminal) {
					continue;
				} else if (producao.getSimbolos().get(0).equals(ntJ)) {
					resultado.add(producao);
				}
			}
		}

		return resultado;
	}
}
