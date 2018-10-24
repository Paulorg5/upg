package io.github.paulorg5.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import io.github.paulorg5.domain.EntradaUsuario;
import io.github.paulorg5.exceptions.ResourceException;
import io.github.paulorg5.vo.RetornoVO;

@RestController
public class ExecutarResource {

	@RequestMapping(value = "/executar", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<RetornoVO> executar(@RequestBody EntradaUsuario entradaUsuario) {
		try {
			GeradorAnalisadores geradorAnalisadores = new GeradorAnalisadores();
			RetornoVO retorno = geradorAnalisadores.analisar(entradaUsuario);
			return new ResponseEntity<RetornoVO>(retorno, HttpStatus.OK);
		} catch (Exception e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
