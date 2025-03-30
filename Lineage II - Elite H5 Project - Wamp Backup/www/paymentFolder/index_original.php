<?php
	$var2 = "======================" . PHP_EOL . "GET VARS" . PHP_EOL;
	if(isset($_GET)){
	    	foreach($_GET as $GET_INDEX => $GET_VALUE){
        		$var2 .= $GET_INDEX . "=" . $GET_VALUE . PHP_EOL;
		}
	}
	$var2 .= "======================" . PHP_EOL . "POST VARS" . PHP_EOL;
	if(isset($_POST)){
	    	foreach($_POST as $GET_INDEX => $GET_VALUE){
        		$var2 .= $GET_INDEX . "=" . $GET_VALUE . PHP_EOL;
		}
	}
	$var2 .= "=============================";
	$myfile = fopen("Inputs.txt","a");
	$txt = $var2 . PHP_EOL;
	fwrite($myfile,$txt);
	fclose($myfile);
?>