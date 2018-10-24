package io.github.paulorg5.converters;

import io.github.paulorg5.ast.No;
import io.github.paulorg5.ast.NoProducao;
import io.github.paulorg5.ast.NoToken;
import io.github.paulorg5.glc.Epsilon;
import io.github.paulorg5.vo.ArvoreAnaliseVO;

public class ConversorCST implements Conversor<No, ArvoreAnaliseVO> {

	@Override
	public ArvoreAnaliseVO converter(No origem) {
		ArvoreAnaliseVO resultado = new ArvoreAnaliseVO();
		converter(origem, resultado);
		return resultado;
	}

	private void converter(No arvore, ArvoreAnaliseVO arvoreVO) {
		if (arvore instanceof NoProducao) {
			arvoreVO.setText(((NoProducao) arvore).getProducao().getNt().getValor().replaceAll("<|>", ""));
			for (No filho : ((NoProducao) arvore).getFilhos()) {
				ArvoreAnaliseVO filhoVO = new ArvoreAnaliseVO();
				arvoreVO.getChildren().add(filhoVO);
				converter(filho, filhoVO);
			}
		} else {
			arvoreVO.setText(((NoToken) arvore).getValor().getSimbolo().getValor().replaceAll("<|>", ""));
			ArvoreAnaliseVO valor = new ArvoreAnaliseVO();

			if (((NoToken) arvore).getValor().getSimbolo() instanceof Epsilon) {
				valor.setText("Valor: " + " VAZIO ");
				arvoreVO.getChildren().add(valor);
			} else {
				valor.setText("Valor: " + ((NoToken) arvore).getValor().getValor());
				ArvoreAnaliseVO linha = new ArvoreAnaliseVO();
				linha.setText("Linha: " + ((NoToken) arvore).getValor().getLinha());
				ArvoreAnaliseVO coluna = new ArvoreAnaliseVO();
				coluna.setText("Coluna: " + ((NoToken) arvore).getValor().getColunaInicio() + " - "
						+ ((NoToken) arvore).getValor().getColunaFim());
				arvoreVO.getChildren().add(valor);
				arvoreVO.getChildren().add(linha);
				arvoreVO.getChildren().add(coluna);
			}
		}
	}
}
