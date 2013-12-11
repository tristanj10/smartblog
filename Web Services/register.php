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

    $user = new Utilisateur();
    
    /**
     * Vérification du nom
     */
    if(isset($_POST['nom']) && is_string($_POST['nom']) && !preg_match("/[^A-Za-z- ]+/", $_POST['nom']) && $_POST['nom'] != '') 
    { 
    	$nom = $_POST['nom'];
    } 
    else 
    {
    	$error .= '\r\n - nom : mauvais format';
    }
    
    /**
     * Vérification du prénom
     */
    if(isset($_POST['prenom']) && is_string($_POST['prenom']) && !preg_match("/[^A-Za-z- ]+/", $_POST['prenom']) && $_POST['prenom'] != '') 
    {
    	$prenom = $_POST['prenom'];
    } 
    else 
    {
    	$error .= '\r\n - prenom : mauvais format';
    }
    
    /**
     * Vérification du mot de passe & repeat
     */
    if(isset($_POST['password']) && is_string($_POST['password']) && strlen($_POST['password']) >= 8) 
    {
    	$_POST['password'] = htmlspecialchars($_POST['password']);
    	$_POST['repeat'] = htmlspecialchars($_POST['repeat']);
    	if(isset($_POST['repeat']) && is_string($_POST['repeat'])) 
    	{
    		 if($_POST['password'] == $_POST['repeat']) 
    		 {
    		 	$password = $_POST['password'];
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
    if(isset($_POST['login']) && is_string($_POST['login']) && filter_var($_POST['login'], FILTER_VALIDATE_EMAIL)) 
    {
    	
    	if($user->existeDeja($dbh, $_POST['login']))
    	{
    		$error .= '\r\n - login : déjà utilisé';
    	}
    	else 
    	{
    		$login = $_POST['login'];
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
    	
    	$user->sauvegarder($dbh);

    	
    	// Génération d'un token et connexion
    	$token = md5(uniqid(rand(), true));
    	$_SESSION['token'] = $token;
    	$_SESSION['login'] = $login;

    	
    }


    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;


?>