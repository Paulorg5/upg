package io.github.paulorg5.glc;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Gramatica {

	private NaoTerminal ntInicial;

	private List<Producao> producoes = new ArrayList<>();

	private Set<NaoTerminal> naoTerminais = new LinkedHashSet<>();

	private Set<Terminal> terminais = new HashSet<>();

	public Gramatica() {}
	
	public Gramatica(Gramatica gramatica) {
		this.ntInicial = gramatica.ntInicial;
		this.naoTerminais = gramatica.naoTerminais;
		this.terminais = gramatica.terminais;
		this.producoes = gramatica.producoes;
	}
	
	public NaoTerminal getNtInicial() {
		return ntInicial;
	}

	public void setNtInicial(NaoTerminal ntInicial) {
		this.ntInicial = ntInicial;
	}

	public List<Producao> getProducoes() {
		return producoes;
	}

	public void setProducoes(List<Producao> producoes) {
		this.producoes = producoes;
	}

	public Set<NaoTerminal> getNaoTerminais() {
		return naoTerminais;
	}

	public void setNaoTerminais(Set<NaoTerminal> naoTerminais) {
		this.naoTerminais = naoTerminais;
	}

	public Set<Terminal> getTerminais() {
		return terminais;
	}

	public void setTerminais(Set<Terminal> terminais) {
		this.terminais = terminais;
	}

	public String toStringView() {
		StringBuilder sb = new StringBuilder();
		for (NaoTerminal nt : getNtsDasProducoesOrdenados()) {
			sb.append(nt.toString());
			sb.append(" ::=");
			List<Producao> producoesDoNt = getProducoesDoNt(nt);
			if (!producoesDoNt.isEmpty()) {
				if (producoesDoNt.size() == 1) {
					sb.append("\n");
					sb.append("\t");
					sb.append(producoesDoNt.get(0).toStringSomenteSimbolos());
					sb.append(";");
					sb.append("\n");
				} else {
					for (Producao p : producoesDoNt) {
						if (producoesDoNt.indexOf(p) == 0) {
							sb.append("\n");
						}

						sb.append("\t");
						if (producoesDoNt.indexOf(p) != 0) {
							sb.append("| ");
						}
						sb.append(p.toStringSomenteSimbolos());
						if (producoesDoNt.indexOf(p) == producoesDoNt.size() - 1) {
							sb.append(";");
						} else {
							sb.append("\n");
						}
					}
				}
			}

			sb.append("\n\n");
		}
		return sb.toString();
	}

	public List<Producao> getProducoesDoNt(NaoTerminal nt) {
		List<Producao> producoesDoNt = new ArrayList<>();
		for (Producao p : producoes) {
			if (p.getNt().equals(nt)) {
				producoesDoNt.add(p);
			}
		}

		return producoesDoNt;
	}

	public List<NaoTerminal> getNtsDasProducoesOrdenados() {
		List<NaoTerminal> nts = new ArrayList<>();
		for (Producao p : producoes) {
			if (!nts.contains(p.getNt())) {
				nts.add(p.getNt());
			}
		}

		return nts;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("#### GLC com Símbolo Inicial ");
		sb.append(ntInicial.toString() + " ####");

		for (Producao p : producoes) {
			sb.append(p.getNt() + " ::= ");
			sb.append(p.toString());
		}

		return sb.toString();
	}
}
