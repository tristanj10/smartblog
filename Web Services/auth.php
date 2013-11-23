<?php
    session_start();
    include_once('./head/bdd.php');
    
    $token = '';
    $error = '';
	$ok = false;
    
    if(isset($_GET['login']) && isset($_GET['password'])) {
        
        
        
        try {
            
            // salt DONE
            //$salt = "4251452356"; // rand
            //$password = $_GET['password'];
            //$password_chiffre = sha1($password.$salt).$salt;
            
            $stmt = $dbh->prepare("SELECT * FROM utilisateurs where login = ?");
            $stmt->bindValue(1, $_GET['login'],PDO::PARAM_STR);
            $stmt->execute();
            
            $row = $stmt->fetch();
            $id = $row['id'];
            if($stmt->rowCount() == 1) {
            	
            	// Bloque ?
            	if(intval($row['nb_tentatives']) < 4) { // Pas de pb
            		
            		$ok = true; // Connexion
            		
            	} elseif (intval($row['nb_tentatives']) > 3 && (strtotime(date("Y-m-d H:i:s"))-strtotime($row['date_tentative']) < 900)) {

           			// Compte bloqué
           			$error = "Cpte bloqué";
           			
            			
           		} else {
            			
           			// Déblocage de compte
           			$stmt = $dbh->prepare("UPDATE utilisateurs SET nb_tentatives = 0 WHERE id = ?");
           			$stmt->execute(array($id));
	
           			$ok = true; // Connexion
	            } 
            
            } else {
            	// Login inexistant
            	$error = "Erreur de connexion";
            }
            
            /**
             * Connexion
             */
            if($ok == true)
            {
            	// 10 derniers caracteres = salt
            	$salt = substr($row['password'], -10);
            	$pwd = substr($row['password'], 0, -10);
            	 
            	$password = $_GET['password'];
            	(is_string($_GET['password'])) ? $password = $_GET['password'] : $password = '';
            	 
            	if($row['password'] == md5($password.$salt).$salt) { // Vérification mdp
            	
            		// Bon mot de passe
            		$token = md5(uniqid(rand(), true));
            		$_SESSION['token'] = $token;
            		$_SESSION['login'] = $_GET['login'];
            	} else {
            		$tentatives = intval($row['nb_tentatives']) + 1; // tentatives++
            		$stmt = $dbh->prepare("UPDATE utilisateurs SET nb_tentatives = ?, date_tentative = ? WHERE id = ?");
            		$stmt->execute(array($tentatives,date("Y-m-d H:i:s"),$id));
            	
            		$error = "Erreur de connexion";
            	}
            }

        } catch(Exception $e) {
        	//echo $e->getMessage();
        }
        
    } else {
        $error = 'Manque paramètres';
        
    }

    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;

?>