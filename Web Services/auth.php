<?php
    session_start();
    require_once('./head/https.php');
    require_once('./models/utilisateurs.php');
    
    $token = '';
    $error = '';
	$ok = false;
	
	
    /**
     * V�rification des entr�es
     */
    if(isset($_GET['login']) && isset($_GET['password'])) 
    {
    	
    	$login = $_GET['login'];
    	$password = $_GET['password'];

    	$user = new Utilisateurs();
    	if($user->existeDeja($login))
    	{ // Existe d�j� ?
    		$user->charger($login);
    		if($user->estBloque()) 
    		{ // Est bloqu� ?
    			$error = "Compte bloqu�";
    		}
    		else 
    		{
    			if(($user->peutEtreDebloque()))
    			{ // Peut �tre d�bloqu� ?
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
    		{ // Authentification r�ussie
    			$token = md5(uniqid(rand(), true));
    			$_SESSION['token'] = $token;
    			$_SESSION['login'] = $login;
    			
    			$tentatives = 0; // RAZ tentatives
    		} 
    		else 
    		{ // Mauvaise authentification
    			$tentatives = intval($user->getNbTentatives());
    			$tentatives++; // Incr�mentation tentatives
    		
    			if($tentatives < 4)
    			{
    				$error = "Erreur de connexion";
    			}
    			else 
    			{
    				$error = "Compte bloqu�";
    			}
    			
    			
    			$user->setDateTentative(date("Y-m-d H:i:s"));
    		}
    		$user->setNbTentatives($tentatives);
    		$user->sauvegarder();
    	}
    } 
    else 
    {
    	$error = "Manque des param�tres";
    }
    	
    	

    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;

?>