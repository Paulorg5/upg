Split(['#ten', '#eleven'], {
	gutterSize: 8,
	cursor: 'col-resize'
});

Split(['#twelve', '#thirteen'], {
    	direction: 'vertical',
      sizes: [25, 75],
      gutterSize: 8,
      cursor: 'row-resize'
});

function addMsg(mensagem, titulo, tipo) {
	swal({
		  title: titulo,
		  text: mensagem,
		  type: tipo,
		  confirmButtonText: "Ok"
	});
}

$(".search-input").keyup(function() {

    var searchString = $(this).val();
    console.log(searchString);
    $('#cst').jstree('search', searchString);
});