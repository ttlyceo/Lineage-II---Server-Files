<?php

    include_once("class_l2_server.php");

    $classServer = new l2server();


    if(!isset($_POST)){
        return;
    }

    $IPS = $_SERVER['REMOTE_ADDR'];

    $toFile = "IP: " . $IPS . PHP_EOL;
    foreach($_POST as $P => $V){
        $toFile .= $P . "=" . $V . PHP_EOL;
    }

    $PAYMENT_DATE = isset($_POST["payment_date"]) ? $_POST["payment_date"] : "";
    $FIRST_NAME = isset($_POST["first_name"]) ? $_POST["first_name"] : "";
    $LAST_NAME = isset($_POST["last_name"]) ? $_POST["last_name"] : "";
    $PAYER_EMAIL = isset($_POST["payer_email"]) ? $_POST["payer_email"] : "";
    $PAYER_ID = isset($_POST["payer_id"]) ? $_POST["payer_id"] : "";
    $ITEM_NAME = isset($_POST["item_name"]) ? $_POST["item_name"] : "";
    $ITEM_NUMBER = isset($_POST["item_number"]) ? $_POST["item_number"] : "";
    $DONATION = isset($_POST["mc_gross"]) ? $_POST["mc_gross"] : "";
    $TRANSACTION_ID = isset($_POST["txn_id"]) ? $_POST["txn_id"] : "";

    $IdCo = str_replace(".", "", microtime(true));

    if($DONATION == "" || $ITEM_NAME == ""){
        createFile(false, $toFile . PHP_EOL . "Important Files are Empty" , $IdCo);
        exit();
    }

    if($DONATION < 0){
        createFile(false, $toFile . PHP_EOL . "Value is Under 0" , $IdCo);
        exit();        
    }
    

    $classServer->setPAYMENT_DATE($PAYMENT_DATE);
    $classServer->setFIRTS_NAME($FIRST_NAME);
    $classServer->setLAST_NAME($LAST_NAME);
    $classServer->setPAYER_EMAIL($PAYER_EMAIL);
    $classServer->setPAYER_ID($PAYER_ID);
    $classServer->setITEM_NUMBER($ITEM_NAME);
    $classServer->setMC_FEE($DONATION);
    $classServer->setTXN_ID($TRANSACTION_ID);
    $classServer->setID_WEB($ITEM_NAME);


    if($classServer->isOK()){
        $classServer->saveAllData();
        createFile(true, $toFile, $IdCo);
    }else{
        createFile(false, $toFile . PHP_EOL . $classServer->getERROR() , $IdCo);
    }


    function createFile($isOK, $strData, $TRANSACTION_ID){
	   $FileName = "./" . ( $isOK ? "donationFiles" : "donationFilesWrong" ) . "/TRX-" . $TRANSACTION_ID . ".txt";
	   $myfile = fopen( $FileName ,"a");
	   $txt = $strData . PHP_EOL;
	   fwrite($myfile, $txt);
	   fclose($myfile);        
    }

?>