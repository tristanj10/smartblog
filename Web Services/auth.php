<?php
    session_start();
    require_once('./head/connexion.php');
    require_once('./head/https.php');
    require_once('./models/utilisateur.php');
    
    if(empty($_POST))
    {
    	exit;
    }
    	
    
    $token = '';
    $error = '';
	$ok = false;
	
	
    /**
     * Vérification des entrées
     */
    if(isset($_POST['login']) && isset($_POST['password'])) 
    {
    	
    	$login = htmlspecialchars($_POST['login']);
    	$password = htmlspecialchars($_POST['password']);

    	$user = new Utilisateur();
    	if($user->existeDeja($dbh, $login))
    	{ // Existe déjà ?
    		$user->charger($dbh, $login);
    		if($user->estBloque()) 
    		{ // Est bloqué ?
    			$error = "Compte bloqué";
    		}
    		else 
    		{
    			if(($user->peutEtreDebloque()))
    			{ // Peut être débloqué ?
    				$user->setNbTentatives(0);
    				$user->sauvegarder($dbh);
    				 
    				
    			}
    			$ok = true;
    		}
    	}
    	else 
    	{
    		$error = 'Erreur de connexion';
    	}
    	
    	
    	if($ok == true)
    	{
    		if($user->login($dbh, $login, $password))
    		{ // Authentification réussie
    			//$token = md5(uniqid(rand(), true));
    			$_SESSION['token'] = session_id();
    			$_SESSION['user'] = serialize($user);
    			$token = $_SESSION['token'];
    			
    			$tentatives = 0; // RAZ tentatives
    		} 
    		else 
    		{ // Mauvaise authentification
    			$tentatives = intval($user->getNbTentatives());
    			//$tentatives++; // Incrémentation tentatives
    			
    			if($tentatives < 4)
    			{
    				$error = "Erreur de connexion";
    			}
    			else 
    			{
    				$error = "Compte bloqué";
    			}
    			
    			
    			$user->setDateTentative(date("Y-m-d H:i:s"));
    		}
    		$user->setNbTentatives($tentatives);
    		$user->sauvegarder($dbh);
    	}
    } 
    else 
    {
    	$error = "Manque des paramètres";
    }
    	
    	

    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;

?>