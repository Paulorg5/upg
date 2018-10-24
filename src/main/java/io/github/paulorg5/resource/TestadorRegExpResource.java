package io.github.paulorg5.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import io.github.paulorg5.domain.TesteRegExp;
import io.github.paulorg5.exceptions.ResourceException;
import io.github.paulorg5.regexp.TestadorRegExp;

@RestController
public class TestadorRegExpResource {

	@RequestMapping(value = "/regex", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> gerar(@RequestBody TesteRegExp testeRegExp, WebRequest request) {
		try {
			return new ResponseEntity<Boolean>(
					TestadorRegExp.testarRegExp(testeRegExp.getRegexpTeste(), testeRegExp.getStringTesteRegexp()),
					HttpStatus.OK);
		} catch (Exception e) {
			throw new ResourceException(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
		}
	}

}
