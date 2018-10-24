angular.module("app").controller("homeCtrl", function ($scope, upgAPIService) {
	$scope.cmOptLeitura = {
			lineNumbers: true,
			viewportMargin: Infinity,
			readOnly: 'nocursor',
			onLoad: function(_cm) {
				setTimeout(function() {
					_cm.refresh();
					}, 100);
			}
		};
		
	$scope.cmOption = {
			lineNumbers: true,
			indentWithTabs: true,
			styleActiveLine: true,
			matchBrackets: true,
			viewportMargin: Infinity,
			mode: "text/html"
		};
	
	
	$scope.testeRegExp = {
			regexpTeste : "[a-zA-Z]+",
			stringTesteRegexp : "TesTanDo"
	};
	
	$scope.testarRegExp = function() {
		upgAPIService.testar($scope.testeRegExp).then(function(response) {
			criarMsgSucessoTesteRegExp(response);
		}, function(error) {
			addMsg(error.data.mensagem, "Ops! Um erro ocorreu.", "error");
		});
	}
	
	var criarMsgSucessoTesteRegExp = function(response) {
		var stringERegex = $scope.testeRegExp.stringTesteRegexp + "\n RegExp: " + $scope.testeRegExp.regexpTeste;
		var msg; 
		if (response.data) {
			msg = "Entrada de teste reconhecida: \n" + stringERegex;
			addMsg(msg, "Sucesso!", "success");
		} else {
			msg = "Entrada de teste NÃO reconhecida: \n" + stringERegex;
			addMsg(msg ,"Atenção!", "warning");
		}
	};
	
	$scope.baixarProjeto = function() {
		upgAPIService.baixar($scope.entrada).then(function(response) {
			var blob = new Blob([response.data], {type: "application/zip"});
			var a = document.createElement('a');
            a.href = URL.createObjectURL(blob);
            a.download = "projeto.zip";
            a.click();
		}, function(error) {
			addMsg(error.data.message, "Ops! Um erro ocorreu.", "error");
		});
	};
	
	$scope.carregarExemploBnf = function() {
		upgAPIService.getExemploBnf().then(function(response) {
			$scope.entrada = response.data;
		});
	};
	
	$scope.carregarExemploExpressaoAritmetica = function() {
		upgAPIService.getExemploExpressaoAritmetica().then(function(response) {
            $scope.entrada = response.data; 
        });
	};
	
	$scope.carregarExemploRecursivo = function() {
		upgAPIService.getExemploRecursivo().then(function(response) {
            $scope.entrada = response.data; 
        });
	};
	
	$scope.carregarExemploNaoFatorado = function() {
		upgAPIService.getExemploNaoFatorado().then(function(response) {
            $scope.entrada = response.data; 
        });
	};
	
	var carregarArvore = function() {
		if ($scope.treeData == null) {
			$('#cst').jstree({
		        'core' : {
		            'data' : []
		        },
		        "search": {
		            "case_insensitive": true,
		            "show_only_matches" : true
		        },

		        "plugins": ["search"]
				});
		} else {
			$('#cst').jstree(true).settings.core.data = $scope.treeData;
			$('#cst').jstree("refresh");
		}
	};
	
	
	$scope.entrada = {};
	$scope.codigoTeste = {};
	$scope.gramaticas = {
		glcInformada: "",
		glcSemRecursao: "",
		glcFatorada: "",
		glcFinal: ""
	};
	
	$scope.cst = {};
	$scope.ast = {};
	$scope.treeData = null;
	
	$scope.conjuntosFirstFollow = [];
	$scope.tabelaMovimento = {
			header: [],
			conteudo: []
	};
	$scope.pilhaParsing = {};
	
	$scope.itemsByPage = 15;
	$scope.tokens = [];
	
	$scope.pilhaParsing = [];
	
	$scope.executarCodigoTeste = function() {
		upgAPIService.executar($scope.entrada).then(function(response) {
			$scope.gramaticas.glcInformada = response.data.gramaticaInformada;
			$scope.gramaticas.glcSemRecursao = response.data.gramaticaSemRecursao;
			$scope.gramaticas.glcFatorada = response.data.gramaticaFatorada;
			$scope.gramaticas.glcFinal = response.data.gramaticaFinal;
			$scope.conjuntosFirstFollow = response.data.firstFollow;
			$scope.descricaoLexica = response.data.regrasAnalisadorLexico;
			$scope.tabelaMovimento.header = response.data.tabelaMovimento.header;
			$scope.tabelaMovimento.conteudo = response.data.tabelaMovimento.conteudo;
			$scope.tokens = response.data.tokensClassificados;
			$scope.treeData = response.data.arvoreAnalise;		
			$scope.pilhaParsing = response.data.pilhaParsing;
			
			console.log($scope.pilhaParsing);
			
			carregarArvore();
		}, function(error) {
			addMsg(error.data.mensagem, "Ops! Um erro ocorreu.", "error");
		});
	};
	
	$scope.registroParaVisualizar;
	$scope.addParaVisualizar = function(registro) {
		$scope.registroParaVisualizar = registro;
	};
	
	$scope.carregarExemploExpressaoAritmetica();
	carregarArvore();
});