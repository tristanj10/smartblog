<?php
    session_start();
    require_once('./head/https.php');
    require_once('./models/utilisateurs.php');
    
    $token = '';
    $error = '';

    $user = new Utilisateurs();
    
    /**
     * V�rification du nom
     */
    if(isset($_GET['nom']) && is_string($_GET['nom']) && !preg_match("/[^A-Za-z -]+/", $_GET['nom'])) 
    {
    	$nom = $_GET['nom'];
    } 
    else 
    {
    	$error .= ' - nom : mauvais format';
    }
    
    /**
     * V�rification du pr�nom
     */
    if(isset($_GET['prenom']) && is_string($_GET['prenom']) && !preg_match("/[^A-Za-z -]+/", $_GET['prenom'])) 
    {
    	$prenom = $_GET['prenom'];
    } 
    else 
    {
    	$error .= ' - prenom : mauvais format';
    }
    
    /**
     * V�rification du mot de passe & repeat
     */
    if(isset($_GET['password']) && is_string($_GET['password']) && strlen($_GET['password']) >= 8) 
    {
    	if(isset($_GET['repeat']) && is_string($_GET['repeat'])) 
    	{
    		 if($_GET['password'] == $_GET['repeat']) 
    		 {
    		 	$password = $_GET['password'];
    		 } 
    		 else 
    		 { $error .= ' - mots de passe diff�rents'; }
    	}
    	else 
    	{ $error .= ' - mot de passe : mauvais format'; }
    }
    else 
    { $error .= ' - mot de passe : mauvais format'; }
    
    /**
     * V�rification du login
     */
    if(isset($_GET['login']) && is_string($_GET['login']) && filter_var($_GET['login'], FILTER_VALIDATE_EMAIL)) 
    {
    	
    	if($user->existeDeja($_GET['login']))
    	{
    		$error .= ' - login : d�j� utilis�';
    	}
    	else 
    	{
    		$login = $_GET['login'];
    	}
    	 
    	 
    } 
    else 
    {
    	$error .= ' - login : mauvais format';
    }
    
    if($error == '') {
    	// Pas d'erreur 
    	
    	/**
    	 * Enregistrement de l'utilisateur
    	 */
    	$user->setNom($nom);
    	$user->setPrenom($prenom);
    	$user->setLogin($login);
    	$user->setPassword($password);
    	$user->setDateTentative(date("Y-m-d H:i:s"));
    	
    	$user->sauvegarder();

    	
    	// G�n�ration d'un token et connexion
    	$token = md5(uniqid(rand(), true));
    	$_SESSION['token'] = $token;
    	$_SESSION['login'] = $login;

    	
    }


    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;


?>