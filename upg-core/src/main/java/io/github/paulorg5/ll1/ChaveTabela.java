package io.github.paulorg5.ll1;

import java.io.Serializable;

import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Simbolo;

public final class ChaveTabela implements Serializable {
	private static final long serialVersionUID = -7090295133978389154L;

	private NaoTerminal naoTerminal;

	private Simbolo terminal;

	public ChaveTabela(NaoTerminal naoTerminal, Simbolo terminal) {
		super();
		this.naoTerminal = naoTerminal;
		this.terminal = terminal;
	}

	public NaoTerminal getNaoTerminal() {
		return naoTerminal;
	}

	public void setNaoTerminal(NaoTerminal naoTerminal) {
		this.naoTerminal = naoTerminal;
	}

	public Simbolo getTerminal() {
		return terminal;
	}

	public void setTerminal(Simbolo terminal) {
		this.terminal = terminal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((naoTerminal == null) ? 0 : naoTerminal.hashCode());
		result = prime * result + ((terminal == null) ? 0 : terminal.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ChaveTabela other = (ChaveTabela) obj;
		if (naoTerminal == null) {
			if (other.naoTerminal != null)
				return false;
		} else if (!naoTerminal.equals(other.naoTerminal))
			return false;
		if (terminal == null) {
			if (other.terminal != null)
				return false;
		} else if (!terminal.equals(other.terminal))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "ChaveTabela [naoTerminal=" + naoTerminal + ", terminal=" + terminal + "]";
	}

}