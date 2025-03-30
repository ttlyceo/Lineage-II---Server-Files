<?php
    class database{
        private $IP = "127.0.0.1";
        private $USER = "server_user";
        private $PASS = "bec170723ab9c6edef68f03efd40da96";
        private $DB = "live_server";
        
        public function local_ejec_sql($SQL){
			$this->Local_Conex = mysqli_connect($this->IP, $this->USER, $this->PASS, $this->DB);
            mysqli_set_charset($this->Local_Conex, 'utf8');
			$Result = mysqli_query($this->Local_Conex, $SQL);
			return $Result;
		}
		
		public function local_getRows(){
			return mysqli_affected_rows($this->Local_Conex);	
		}
		
		public function local_getId(){
			return mysqli_insert_id($this->Local_Conex);
		}
				
		public function local_close_sql(){
			mysqli_close($this->Local_Conex);
		}        
        
    }
?>