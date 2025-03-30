<?php
	class EmailZeuS{
		public $GS_IP = array();
		public $GS_USER = array();
		public $GS_PASS = array();
		public $GS_BD = array();
		public $GS_SERVER_NAME = array();		
		public $GetName_type_ = "tip";
		
		/***/
		public $WEB_LINK_TO_ACTIVE_DONATION = "http://elixsum.com/ZeuS_MaiN.php"; // For Admin 
		public $WEB_DONATION_LINK_KEY = "0000000000000000000"; // Need to be the same on zeus_email.properties (WebDonationLinkKey)
		public $getNAME_GET_ID_DONACION = "DO_ID_CHECK"; // Need to be the same on zeus_email.properties (WebGetCommandName_for_ID_DONATION)
		public $getNAME_GET_LINK_KEY = "LINK_KEY_CHECK"; // Need to be the same on zeus_email.properties (WebGetCommandName_for_ID_KEY)
		/***/
		
		function EmailZeuS(){
			/*This Information can get it from hexid File*/
			/*ServerID*/
			/*GS_SERVER_NAME[ServerID]*/
			$this->GS_SERVER_NAME[1] = "SERVER 1";
			$this->GS_SERVER_NAME[2] = "SERVER 2";
			$this->GS_SERVER_NAME[3] = "SERVER 3";
			$this->GS_SERVER_NAME[4] = "SERVER 4";
			$this->GS_IP[1] = "IP FROM SERVER 1";
			$this->GS_USER[1] = "USER FROM SERVER 1";
			$this->GS_PASS[1] = "PASS FROM SERVER 1";
			$this->GS_BD[1] = "GAME DATABASE FROM SERVER 1";
			
			$this->GS_IP[2] = "IP FROM SERVER 2";
			$this->GS_USER[2] = "USER FROM SERVER 2";
			$this->GS_PASS[2] = "PASS FROM SERVER 2";
			$this->GS_BD[2] = "GAME DATABASE FROM SERVER 2";
			
			$this->GS_IP[3] = "IP FROM SERVER 3";
			$this->GS_USER[3] = "USER FROM SERVER 3";
			$this->GS_PASS[3] = "PASS FROM SERVER 3";
			$this->GS_BD[3] = "GAME DATABASE FROM SERVER 3";
			
			$this->GS_IP[4] = "IP FROM SERVER 4";
			$this->GS_USER[4] = "USER FROM SERVER 4";
			$this->GS_PASS[4] = "PASS FROM SERVER 4";
			$this->GS_BD[4] = "GAME DATABASE FROM SERVER 4";
			
		}
		
		function _Ejec_sql($SQL,$IDServer){
			$Conex  = mysqli_connect($this->GS_IP[$IDServer], $this->GS_USER[$IDServer] ,$this->GS_PASS[$IDServer],$this->GS_BD[$IDServer]);
			$Resultado = mysqli_query($Conex,$SQL);
			return $Resultado;			
		}
		
		function GET_FORMULARIO_ACTIVACION ($IDDONACION,$IDLINK,$IDSERVER){
			$Consulta = "SELECT zeus_dona_espera.dona_email, zeus_dona_espera.dona_medio, zeus_dona_espera.dona_obser, (SELECT characters.account_name FROM characters WHERE characters.charId = zeus_dona_espera.dona_char), zeus_dona_espera.id, zeus_dona_espera.dona_monto, zeus_dona_espera.dona_activa FROM zeus_dona_espera WHERE zeus_dona_espera.id = '". $IDDONACION . "'";
			
			$Resul = $this->_Ejec_sql($Consulta,$IDSERVER);
			$RowResu = mysqli_fetch_array($Resul);
			if(!$RowResu){
				echo 'ERROR: No hay Información de Donación o ya está activaba. <br>ID Donación ingresada: <b>'.$IDDONACION.'</b><br>';
				echo 'ERROR: Id del Servidor <b>'.$IDSERVER.'</b>';
			}else{
				
				if($RowResu[6]=="false"){
					echo 'ERROR: No hay Información de Donación o ya está activaba. <br>ID Donación ingresada: <b>'.$IDDONACION.'</b><br>';
					echo 'ERROR: Id del Servidor <b>'.$IDSERVER.'</b>';					
					exit();
				}
				
				echo '
					<div id="divCuenta">
					<p>
					  Servidor
					  <label for="textfield"></label>
					<input name="textfield" type="text" id="textfield" readonly="readonly" value="'. $this->GS_SERVER_NAME[$IDSERVER] .'"/>
					</p>
					
					<p>
					  Cuenta
					  <label for="textfield"></label>
					<input name="textfield" type="text" id="textfield" readonly="readonly" value="'.$RowResu[3].'"/>
					</p>
					<p>Donado
					  <label for="textfield2"></label>
					  <input name="textfield2" type="text" id="textfield2" readonly="readonly" value="'.$RowResu[5].'" />
					</p>
					<p>Medio Donación
					  <label for="textfield3"></label>
					  <input name="textfield3" type="text" id="textfield3" readonly="readonly" value="'.$RowResu[1].'" />
					</p>
					<p>PJ Elegido
					  <label for="textfield4"></label>
					  <input name="textfield4" type="text" id="textfield4" readonly="readonly" value="'.$RowResu[3].'" />
					</p>
					<p>Puntos a dar
					  <label for="textfield5"></label>
					  <input name="textfield5" type="text" id="textfield5" value = "0" />
					</p>
					<p>
<input type="submit" name="button" id="buttonEnviar" value="Enviar" onclick="operaChar(\'2\',\''.$IDDONACION.'\',\''.$IDLINK.'\', document.getElementById(\'textfield5\').value,\''.$IDSERVER.'\' )" />
</p>
</div>				
				
				';
			}
			
		}
		
		function setPuntosDonacion($IDClavePaso, $IDDonacionWeb, $PuntosPasar,$IDSERVER){
			
			if($IDClavePaso != $this->WEB_DONATION_LINK_KEY){
				$Resultado['a1'] = "err";
				$Resultado['a2'] = "Link key dont much";
				return json_encode($Resultado);
			}
			
			$Consulta = "SELECT characters.account_name, zeus_dona_espera.dona_fecha FROM zeus_dona_espera INNER JOIN characters ON zeus_dona_espera.dona_char = characters.charId WHERE zeus_dona_espera.id = '". $IDDonacionWeb."' ";
			
			$Resul = $this->_Ejec_sql($Consulta,$IDSERVER);
			$RowResu = mysqli_fetch_array($Resul);
			$AccountName = $RowResu[0];
			$Fecha = $RowResu[1];
			
			$Consulta = "insert into zeus_dona_creditos(cuenta, creditos,fechaDeposit) values ('". $AccountName ."',". $PuntosPasar .",'".$Fecha."')";
			$Resul = $this->_Ejec_sql($Consulta,$IDSERVER);
			
			
			$Consulta = "update zeus_dona_espera set dona_activa='false' where id='".$IDDonacionWeb."'";
			$Resul=$this->_Ejec_sql($Consulta,$IDSERVER);
			
			$Retorno["a1"] = "cor";
			$Retorno["a2"] = "Entregados / Give = " . $PuntosPasar;
			
			return json_encode($Retorno);
		}		
	}
?>
<?php
	if(!isset($_POST["tipJAVA"])){
		return '';
	}
	$Config = new EmailZeuS();
	switch ($_POST["tipJAVA"]){
		case "SET_PROCE_CHAR":
			echo $Config->setPuntosDonacion($_POST["c2"],$_POST["c1"], $_POST["p"],$_POST["IS"]);
			break;
	}	
?>