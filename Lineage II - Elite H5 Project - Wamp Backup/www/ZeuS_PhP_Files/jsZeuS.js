String.prototype.trim = function() { return this.replace(/^\s+|\s+$/g, ""); };
var urlArch = "zeusemail.php";
var urlArch2 = "zeusemail.php";
var filter=/^\w+([\.-]?\w+)*@\w+([\.-]?\w+)*(\.\w{2,4})+$/;
function operaChar(t,c1,c2,p,IS){
	strEnviar = "tipJAVA=SET_PROCE_CHAR&t="+t+"&c1="+c1+"&c2="+c2+"&p="+p+"&IS="+IS;
	document.getElementById("buttonEnviar").disabled = "disabled";
	if(!isNumeric(p)){
		alert('Ingresa solo Números');
		document.getElementById("textfield5").focus();
		document.getElementById("buttonEnviar").disabled = "";
		return;
	}
	jQuery.ajax({
		type:"POST",
		url:urlArch2,
		data:strEnviar,
		dataType:"json",
		success:function(data){	
			if(data.a1=='err'){
				document.getElementById("buttonEnviar").disabled = "";		
			}
			alert(data.a2);
		}
	});
}

function isNumeric(n) {
	return !isNaN(parseFloat(n)) && isFinite(n);
}