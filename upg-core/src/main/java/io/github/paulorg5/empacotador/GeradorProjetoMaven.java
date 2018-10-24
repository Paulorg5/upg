package io.github.paulorg5.empacotador;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.commons.io.IOUtils;

import io.github.paulorg5.arquivodescricao.domain.ArquivoDescricao;
import io.github.paulorg5.gerador.Arquivo;
import io.github.paulorg5.gerador.ArquivoAnalisadorLexico;
import io.github.paulorg5.gerador.ArquivoAnalisadorSintaticoLL1;
import io.github.paulorg5.gerador.ArquivoArvoreUtils;
import io.github.paulorg5.gerador.ArquivoJarProjeto;
import io.github.paulorg5.gerador.ArquivoLeitorResourceSerializado;
import io.github.paulorg5.gerador.ArquivoMain;
import io.github.paulorg5.gerador.ArquivoPom;
import io.github.paulorg5.gerador.ArquivoSerializadoConjuntoFirst;
import io.github.paulorg5.gerador.ArquivoSerializadoRegrasLexico;
import io.github.paulorg5.gerador.ArquivoTabelaMovimento;
import io.github.paulorg5.glc.FatoradorGramatica;
import io.github.paulorg5.glc.Gramatica;
import io.github.paulorg5.glc.NaoTerminal;
import io.github.paulorg5.glc.Producao;
import io.github.paulorg5.glc.RemovedorRecursao;
import io.github.paulorg5.glc.Simbolo;
import io.github.paulorg5.ll1.ChaveTabela;
import io.github.paulorg5.ll1.Conjuntos;
import io.github.paulorg5.ll1.TabelaAnaliseSintaticaLL1Builder;

public class GeradorProjetoMaven {

	public static byte[] gerarProjeto(ArquivoDescricao arquivoDescricao) throws Exception {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		ZipOutputStream zipOutputStream = new ZipOutputStream(bufferedOutputStream, Charset.forName("UTF-8"));
		try {
			
			for (Arquivo arquivo : getArquivosParaGravarNoZip(arquivoDescricao)) {
				zipOutputStream.putNextEntry(new ZipEntry(arquivo.getCaminhoNoZip()));
				InputStream in = arquivo.getInputStream();
				IOUtils.copy(in, zipOutputStream);
				in.close();
				zipOutputStream.closeEntry();
			}

			if (zipOutputStream != null) {
				zipOutputStream.finish();
				zipOutputStream.flush();
				IOUtils.closeQuietly(zipOutputStream);
			}
		} catch (Exception e) {
			throw new Exception(e); 
		}

		IOUtils.closeQuietly(bufferedOutputStream);
		IOUtils.closeQuietly(byteArrayOutputStream);

		return byteArrayOutputStream.toByteArray();
	}

	private static List<Arquivo> getArquivosParaGravarNoZip(ArquivoDescricao arquivoDescricao) {
		List<Arquivo> arquivos = new ArrayList<Arquivo>();
		
		Gramatica gramatica = arquivoDescricao.getConteudoSintatico().getGramatica();
		
		if (arquivoDescricao.getConteudoHeader().deveTransformarGramatica()) {
			arquivoDescricao.getConteudoSintatico().setGramatica(RemovedorRecursao.remover(gramatica));
			arquivoDescricao.getConteudoSintatico().setGramatica(FatoradorGramatica.fatorar(gramatica));
		}
		
		Map<NaoTerminal, Set<Simbolo>> first = Conjuntos.getInstance().first(gramatica);
		Map<NaoTerminal, Set<Simbolo>> follow = Conjuntos.getInstance().follow(gramatica, first);
		
		TabelaAnaliseSintaticaLL1Builder tabelaBuilder = new TabelaAnaliseSintaticaLL1Builder();
		Map<ChaveTabela, Producao> tabelaMovimento = tabelaBuilder.comParametros(first, follow, gramatica).getTabela();
			
		arquivos.add(new ArquivoPom(arquivoDescricao.getConteudoHeader()));
		arquivos.add(new ArquivoMain(arquivoDescricao.getConteudoHeader()));
		arquivos.add(new ArquivoSerializadoRegrasLexico(arquivoDescricao));
		arquivos.add(new ArquivoAnalisadorLexico(arquivoDescricao.getConteudoHeader()));
		arquivos.add(new ArquivoAnalisadorSintaticoLL1(arquivoDescricao.getConteudoHeader()));
		arquivos.add(new ArquivoTabelaMovimento(tabelaMovimento,
				arquivoDescricao.getConteudoSintatico().getGramatica().getNtInicial()));
		arquivos.add(new ArquivoLeitorResourceSerializado(arquivoDescricao.getConteudoHeader()));
		arquivos.add(new ArquivoJarProjeto());
		arquivos.add(new ArquivoSerializadoConjuntoFirst(first));
		arquivos.add(new ArquivoArvoreUtils(arquivoDescricao.getConteudoHeader()));
		return arquivos;
	}
}
