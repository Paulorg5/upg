package io.github.paulorg5.resource;

import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.Charset;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.paulorg5.domain.EntradaUsuario;
import io.github.paulorg5.exceptions.ResourceException;

@RestController
public class EntradaUsuarioResource {

	private static final String CAMINHO_ARQUIVOS_EXEMPLOS = "/exemplos_arquivos_descricao/";

	private static final String MSG_ERRO_PADRAO = "Falha ao carregar arquivo de exemplo!";

	@Autowired
	private ResourceLoader resourceLoader;

	@RequestMapping(value = "/exemploExpressaoAritmetica", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntradaUsuario> entradaPadrao() {
		try {
			EntradaUsuario entradaUsuario = new EntradaUsuario();
			entradaUsuario.setCodigoArquivoDescricao(carregarConteudoArquivo("expressao_aritmetica.txt"));
			entradaUsuario.setCodigoTeste(carregarConteudoArquivo("expressao_aritmetica_teste.txt"));
			return new ResponseEntity<>(entradaUsuario, HttpStatus.OK);
		} catch (IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_PADRAO + e.getMessage());
		}
	}

	@RequestMapping(value = "/exemploBnf", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntradaUsuario> exemploBnf() {
		try {
			EntradaUsuario entradaUsuario = new EntradaUsuario();
			entradaUsuario.setCodigoArquivoDescricao(carregarConteudoArquivo("bnf.txt"));
			entradaUsuario.setCodigoTeste(carregarConteudoArquivo("bnf_entrada_teste.txt"));
			return new ResponseEntity<>(entradaUsuario, HttpStatus.OK);
		} catch (IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_PADRAO + e.getMessage());
		}
	}

	@RequestMapping(value = "/exemploExpressaoAritmeticaRecursiva", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntradaUsuario> exemploExpressaoAritmeticaRecursiva() {
		try {
			EntradaUsuario entradaUsuario = new EntradaUsuario();
			entradaUsuario.setCodigoArquivoDescricao(carregarConteudoArquivo("expressao_aritmetica_recursiva.txt"));
			entradaUsuario.setCodigoTeste(carregarConteudoArquivo("expressao_aritmetica_teste.txt"));
			return new ResponseEntity<>(entradaUsuario, HttpStatus.OK);
		} catch (IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_PADRAO + e.getMessage());
		}
	}

	@RequestMapping(value = "/exemploNaoFatorada", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<EntradaUsuario> exemploNaoFatorada() {
		try {
			EntradaUsuario entradaUsuario = new EntradaUsuario();
			entradaUsuario.setCodigoArquivoDescricao(carregarConteudoArquivo("exemplo_nao_fatorada.txt"));
			entradaUsuario.setCodigoTeste(carregarConteudoArquivo("exemplo_nao_fatorada_entrada.txt"));
			return new ResponseEntity<>(entradaUsuario, HttpStatus.OK);
		} catch (IOException e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, MSG_ERRO_PADRAO + e.getMessage());
		}
	}

	private String carregarConteudoArquivo(String nomeArquivo) throws IOException {
		Resource resource = resourceLoader.getResource("classpath:" + CAMINHO_ARQUIVOS_EXEMPLOS + nomeArquivo);

		System.out.println(resource.getURL().toString());

		StringWriter writer = new StringWriter();
		IOUtils.copy(resource.getInputStream(), writer, Charset.forName("UTF-8"));
		String theString = writer.toString();

		return theString;
	}
}
