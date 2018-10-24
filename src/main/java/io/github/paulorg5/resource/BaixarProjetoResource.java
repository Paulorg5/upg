package io.github.paulorg5.resource;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.paulorg5.arquivodescricao.parser.ArquivoDescricaoParser;
import io.github.paulorg5.domain.EntradaUsuario;
import io.github.paulorg5.empacotador.GeradorProjetoMaven;
import io.github.paulorg5.exceptions.ResourceException;

@RestController
public class BaixarProjetoResource {

	@RequestMapping(value = "/baixar", method = RequestMethod.POST, produces = "application/zip")
	public HttpEntity<byte[]> baixarProjeto(@RequestBody EntradaUsuario entradaUsuario) {
		try {
			String nomeArquivo = "projeto";
			byte[] arquivo = GeradorProjetoMaven
					.gerarProjeto(ArquivoDescricaoParser.getInstance().parse(entradaUsuario.getCodigoArquivoDescricao())); 
			return criarHttpEntity(arquivo, nomeArquivo);
		} catch (Exception e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());	
		}
	}

	private HttpEntity<byte[]> criarHttpEntity(byte[] arquivo, String nomeArquivo) {
		HttpHeaders header = new HttpHeaders();
		header.setContentType(new MediaType("application", "zip"));
		header.set("Content-Disposition", "attachment; filename=" + nomeArquivo + ".zip");
		header.setContentLength(arquivo.length);
		return new HttpEntity<byte[]>(arquivo, header);
	}
}
