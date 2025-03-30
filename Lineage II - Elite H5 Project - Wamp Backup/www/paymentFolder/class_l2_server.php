<?php
    include_once("class_db.php");
    class l2server{
        private $ID_WEB = "";
        private $PAYMENT_DATE = "";
        private $FIRTS_NAME  = "";
        private $LAST_NAME = "";
        private $PAYER_EMAIL = "";
        private $PAYER_ID = "";
        private $ITEM_NUMBER = "";
        private $MC_FEE = "";
        private $TXN_ID = "";
        
        private $strERROR = "";
        
        
        private $EMAIL_LINKED_GAME = "";
        private $ACCOUNT_LINKED_GAME = "";
        
        public function setID_WEB ($value){
            $this->ID_WEB = $value;
        }
        public function setPAYMENT_DATE ($value){
            $this->PAYMENT_DATE = $value;
        }
        public function setFIRTS_NAME  ($value){
            $this->FIRTS_NAME = $value;
        }
        public function setLAST_NAME ($value){
            $this->LAST_NAME = $value;
        }
        public function setPAYER_EMAIL ($value){
            $this->PAYER_EMAIL = $value;
        }
        public function setPAYER_ID ($value){
            $this->PAYER_ID = $value;
        }
        public function setITEM_NUMBER ($value){
            $this->ITEM_NUMBER = $value;
        }
        public function setMC_FEE ($value){
            $this->MC_FEE = $value;
        }
        public function setTXN_ID ($value){
            $this->TXN_ID = $value;
        }
        
        public function getERROR(){
            return $this->strERROR;
        }
        
        
        public function isOK(){
            $DataB = new database();
            if($this->ID_WEB == ""){
                $this->strERROR = "Id WEB is Empty";
                return false;
            }
            
            $Qry = "SELECT id, createTime, gameEmail, gameAccount FROM zeus_paypal WHERE zeus_paypal.idweb = '$this->ID_WEB' AND isOK = 'N'";//AND UNIX_TIMESTAMP() < createTimeLimit
            $Result = $DataB->local_ejec_sql($Qry);
            $Qn = $DataB->local_getRows();
            if($Qn <= 0){
                $this->strERROR = "NO DONATION INFO WITH THE ID: " . $Qry . " Qn: " . $Qn;
                return false;
            }
            $RowResu = mysqli_fetch_array($Result);
            $this->ACCOUNT_LINKED_GAME = $RowResu["gameAccount"];
            $this->EMAIL_LINKED_GAME = $RowResu["gameEmail"];
            return true;
        }
        

        public function saveAllData(){
            if($this->savePayPalData()){
                $this->saveDonationZeuS();
            }
        }
        
        private function getDonationPoints(){
            $DonationToGive = ( 10 * $this->MC_FEE ) / 1;
            if($this->MC_FEE >= 10 && $this->MC_FEE < 25 ){
                $DonationToGive += ( $DonationToGive * 0.1 );
            }else if($this->MC_FEE >= 25 && $this->MC_FEE < 50){
                $DonationToGive += ( $DonationToGive * 0.15 );
            }else if($this->MC_FEE >= 50 && $this->MC_FEE < 100){
                $DonationToGive += ( $DonationToGive * 0.2 );
            }else if($this->MC_FEE >= 100 && $this->MC_FEE < 250){
                $DonationToGive += ( $DonationToGive * 0.25 );
            }else if($this->MC_FEE >= 250 && $this->MC_FEE < 500){
                $DonationToGive += ( $DonationToGive * 0.35 );
            }else if($this->MC_FEE >= 500){
                $DonationToGive += ( $DonationToGive * 0.50 );
            }
            
            return round($DonationToGive);
            
        }
        
        private function saveDonationZeuS(){
            $DataB = new database();
            $Qry = "INSERT INTO zeus_dona_creditos(cuenta, creditos) VALUES ('$this->ACCOUNT_LINKED_GAME', ". $this->getDonationPoints() .")";
            $DataB->local_ejec_sql($Qry);
            if($DataB->local_getRows() > 0){
                return true;
            }
            $this->strERROR = "CANT CREATE DONATION REWARD ON ZEUS";
            return false;
            
        }
        
        private function savePayPalData(){
            $DataB = new database();
            $Qry = "UPDATE zeus_paypal SET 
                payment_date = '$this->PAYMENT_DATE', 
                firts_name = '$this->FIRTS_NAME',
                last_name = '$this->LAST_NAME',
                payer_email = '$this->PAYER_EMAIL',
                payer_id = '$this->PAYER_ID',
                item_number = '$this->ITEM_NUMBER',
                mc_fee = '$this->MC_FEE',
                txn_id = '$this->TXN_ID',
                isOK = 'Y'
            WHERE
                idweb = '$this->ID_WEB'";
            $Resul = $DataB->local_ejec_sql($Qry);
            $ReturnData = ($DataB->local_getRows() > 0 ? true : false);
            $this->strERROR = $ReturnData ? "" : "CANT UPDATE THE DONATION INFO";
            return $ReturnData;
        }
        
        
    }
?>