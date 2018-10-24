package io.github.paulorg5.arquivodescricao.domain;

public class ArquivoDescricao {
	private ConteudoHeader conteudoHeader = new ConteudoHeader();

	private ConteudoLexico conteudoLexico = new ConteudoLexico();

	private ConteudoSintatico conteudoSintatico = new ConteudoSintatico();

	public ConteudoHeader getConteudoHeader() {
		return conteudoHeader;
	}

	public void setConteudoHeader(ConteudoHeader conteudoHeader) {
		this.conteudoHeader = conteudoHeader;
	}

	public ConteudoLexico getConteudoLexico() {
		return conteudoLexico;
	}

	public void setConteudoLexico(ConteudoLexico conteudoLexico) {
		this.conteudoLexico = conteudoLexico;
	}

	public ConteudoSintatico getConteudoSintatico() {
		return conteudoSintatico;
	}

	public void setConteudoSintatico(ConteudoSintatico conteudoSintatico) {
		this.conteudoSintatico = conteudoSintatico;
	}

	@Override
	public String toString() {
		return "ArquivoDescricao \n[conteudoHeader=\n" + conteudoHeader + ", \nconteudoLexico=\n" + conteudoLexico
				+ ", \nconteudoSintatico=\n" + conteudoSintatico + "]";
	}
}
