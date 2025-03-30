<?php
    if(!isset($_GET)){
        exit();
    }
    $ID_WEB = isset($_GET["IDW"]) ? $_GET["IDW"] : "";
    $ITEM_NAME = isset($_GET["INM"]) ? $_GET["INM"] : "";
    $ITEM_NUMBER = isset($_GET["INB"]) ? $_GET["INB"] : "";

    if($ID_WEB == "" || $ITEM_NAME == "" || $ITEM_NUMBER == ""){
        exit();
    }
?>

<html>
	<body>
		<form action="https://www.paypal.com/cgi-bin/webscr" method="post" target="_top">
		<input type="hidden" name="cmd" value="_s-xclick" />
		<input type="hidden" name="hosted_button_id" value="A3P6R7X6KRGEC" />
		<input type="hidden" name="item_name" value="<?php echo $ID_WEB ?>" />
        	<input type="hidden" name="item_number" value="<?php echo $ID_WEB ?>" />
		<input type="hidden" name="quantity" value="1" />
		<input type="image" src="https://www.paypalobjects.com/en_US/i/btn/btn_donateCC_LG.gif" border="0" name="submit" title="PayPal - The safer, easier way to pay online!" alt="Donate with PayPal button" />
		<img alt="" border="0" src="https://www.paypal.com/en_SK/i/scr/pixel.gif" width="1" height="1" />
		</form>
	</body>
</html>

