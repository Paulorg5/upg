angular.module("app").factory("upgAPIService", function ($http, config) {
	var _baixar = function (entradaUsuario) {
		return $http.post(config.baseUrl + "/baixar", entradaUsuario, {responseType:'arraybuffer'});
	};
	
	var _executar = function (codigoTeste) {
		return $http.post(config.baseUrl + "/executar", codigoTeste);
	};
	
	var _getExemploExpressaoAritmetica = function () {
		return $http.get(config.baseUrl + "/exemploExpressaoAritmetica");
	};
	
	var _getExemploBnf = function () {
		return $http.get(config.baseUrl + "/exemploBnf");
	};
	
	var _getExemploRecursivo = function () {
		return $http.get(config.baseUrl + "/exemploExpressaoAritmeticaRecursiva");
	};
	
	var _getExemploNaoFatorado = function() {
		return $http.get(config.baseUrl + "/exemploNaoFatorada");
	};
	
	var _testar = function (entrada) {
		return $http.post(config.baseUrl + "/regex", entrada);
	};
	
	return {
		baixar: _baixar,
		executar: _executar,
		getExemploExpressaoAritmetica: _getExemploExpressaoAritmetica,
		getExemploBnf: _getExemploBnf,
		testar: _testar,
		getExemploRecursivo: _getExemploRecursivo,
		getExemploNaoFatorado: _getExemploNaoFatorado
	};
});