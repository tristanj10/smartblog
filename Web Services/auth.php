<?php
    session_start();
    require_once('./head/https.php');
    require_once('./models/utilisateurs.php');
    
    $token = '';
    $error = '';
	$ok = false;
	
	
    /**
     * Vérification des entrées
     */
    if(isset($_GET['login']) && isset($_GET['password'])) 
    {
    	
    	$login = $_GET['login'];
    	$password = $_GET['password'];

    	$user = new Utilisateurs();
    	if($user->existeDeja($login))
    	{ // Existe déjà ?
    		$user->charger($login);
    		if($user->estBloque()) 
    		{ // Est bloqué ?
    			$error = "Compte bloqué";
    		}
    		else 
    		{
    			if(($user->peutEtreDebloque()))
    			{ // Peut être débloqué ?
    				$user->setNbTentatives(0);
    				$user->sauvegarder();
    				 
    				
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
    		if($user->login($login, $password))
    		{ // Authentification réussie
    			$token = md5(uniqid(rand(), true));
    			$_SESSION['token'] = $token;
    			$_SESSION['login'] = $login;
    			
    			$tentatives = 0; // RAZ tentatives
    		} 
    		else 
    		{ // Mauvaise authentification
    			$tentatives = intval($user->getNbTentatives());
    			$tentatives++; // Incrémentation tentatives
    		
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
    		$user->sauvegarder();
    	}
    } 
    else 
    {
    	$error = "Manque des paramètres";
    }
    	
    	

    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;

?>