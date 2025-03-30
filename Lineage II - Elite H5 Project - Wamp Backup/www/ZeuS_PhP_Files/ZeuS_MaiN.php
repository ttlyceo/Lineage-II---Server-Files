<?php
	include("zeusemail.php");
	$GetName_type_ = "tip";//Need to be the same setting from zeus_email.properties (WebGetNameType) 
?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<script src="jsZeuS.js"></script>
<script src="http://code.jquery.com/jquery-latest.js"></script>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Documento sin título</title>
</head>
<body>
<?php
	if(isset($_GET[$GetName_type_])){
		$Config = new EmailZeuS();
		$IDSERVER = 1;
		if(isset($_GET["ZEUS_ID_SERVER"])){
			$IDSERVER = $_GET["ZEUS_ID_SERVER"];
		}
		switch ($_GET[$GetName_type_]){
			case "ACTIVE_DONATION":/*DO NOT CHANGE*/
				if(isset($_GET[$Config->getNAME_GET_ID_DONACION]) && isset($_GET[$Config->getNAME_GET_LINK_KEY])){
					$Config->GET_FORMULARIO_ACTIVACION ($_GET[$Config->getNAME_GET_ID_DONACION],$_GET[$Config->getNAME_GET_LINK_KEY],$IDSERVER);
				}
				break;
		}
	}
?>
</body>
</html>