<?php


require ('wampserver.lib.php');

require 'config.inc.php';



$onlineText = "#   onlineoffline tag - don't remove
    Require all granted";
    
$offlineText = "#   onlineoffline tag - don't remove
    Require local"; 


$httpConfFileContents = file_get_contents($c_apacheConfFile) or die ("httpd.conf file not found");


// on modifie le fichier httpd.conf 
if ($_SERVER['argv'][1] == 'off')
{
    
    $wampIniNewContents['status'] = 'offline';
    $httpConfFileContents = str_replace($onlineText,$offlineText,$httpConfFileContents);
    $fpHttpd = fopen($c_apacheConfFile,"w");
    fwrite($fpHttpd,$httpConfFileContents);
    fclose($fpHttpd);
}

if ($_SERVER['argv'][1] == 'on')
{
    $wampIniNewContents['status'] = 'online';
    $httpConfFileContents = str_replace($offlineText,$onlineText,$httpConfFileContents);
    $fpHttpd = fopen($c_apacheConfFile,"w");
    fwrite($fpHttpd,$httpConfFileContents);
    fclose($fpHttpd);
}


//on enregistre la nouvelle configuration
wampIniSet($configurationFile, $wampIniNewContents);
?>