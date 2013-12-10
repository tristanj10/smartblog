<?php
    session_start();
    require_once('./head/https.php');
    require_once('./models/utilisateurs.php');
    
    $token = '';
    $error = '';

    $user = new Utilisateur();
    
    /**
     * Vérification du nom
     */
    if(isset($_GET['nom']) && is_string($_GET['nom']) && !preg_match("/[^A-Za-z-]+/", $_GET['nom']) && $_GET['nom'] != '') 
    { 
    	$nom = $_GET['nom'];
    } 
    else 
    {
    	$error .= '\r\n - nom : mauvais format';
    }
    
    /**
     * Vérification du prénom
     */
    if(isset($_GET['prenom']) && is_string($_GET['prenom']) && !preg_match("/[^A-Za-z-]+/", $_GET['prenom']) && $_GET['prenom'] != '') 
    {
    	$prenom = $_GET['prenom'];
    } 
    else 
    {
    	$error .= '\r\n - prenom : mauvais format';
    }
    
    /**
     * Vérification du mot de passe & repeat
     */
    if(isset($_GET['password']) && is_string($_GET['password']) && strlen($_GET['password']) >= 8 && 
    preg_match("/[ ]+/", $_GET['password']) && preg_match("/[ ]+/", $_GET['repeat'])) 
    {
    	$_GET['password'] = htmlspecialchars($_GET['password']);
    	$_GET['repeat'] = htmlspecialchars($_GET['repeat']);
    	if(isset($_GET['repeat']) && is_string($_GET['repeat'])) 
    	{
    		 if($_GET['password'] == $_GET['repeat']) 
    		 {
    		 	$password = $_GET['password'];
    		 } 
    		 else 
    		 { $error .= '\r\n - mots de passe différents'; }
    	}
    	else 
    	{ $error .= '\r\n - mot de passe : mauvais format'; }
    }
    else 
    { $error .= '\r\n - mot de passe : mauvais format'; }
    
    /**
     * Vérification du login
     */
    if(isset($_GET['login']) && is_string($_GET['login']) && filter_var($_GET['login'], FILTER_VALIDATE_EMAIL)) 
    {
    	
    	if($user->existeDeja($_GET['login']))
    	{
    		$error .= '\r\n - login : déjà utilisé';
    	}
    	else 
    	{
    		$login = $_GET['login'];
    	}
    	 
    	 
    } 
    else 
    {
    	$error .= '\r\n - login : mauvais format';
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

    	
    	// Génération d'un token et connexion
    	$token = md5(uniqid(rand(), true));
    	$_SESSION['token'] = $token;
    	$_SESSION['login'] = $login;

    	
    }


    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;


?>