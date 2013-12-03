<?php
    session_start();
    include_once('./head/https.php');
    include_once('./head/bdd.php');
    
    $token = '';
    $error = '';

    
    /**
     * Vérification du nom
     */
    if(isset($_GET['nom']) && is_string($_GET['nom']) && !preg_match("/[^A-Za-z -]+/", $_GET['nom'])) {
    	$nom = $_GET['nom'];
    } else {
    	$error .= ' - nom : mauvais format';
    }
    
    /**
     * Vérification du prénom
     */
    if(isset($_GET['prenom']) && is_string($_GET['prenom']) && !preg_match("/[^A-Za-z -]+/", $_GET['prenom'])) {
    	$prenom = $_GET['prenom'];
    } else {
    	$error .= ' - prenom : mauvais format';
    }
    
    /**
     * Vérification du login
     */
    if(isset($_GET['login']) && is_string($_GET['login']) && filter_var($_GET['login'], FILTER_VALIDATE_EMAIL)) {
    	$login = $_GET['login'];
    } else {
    	$error .= ' - login : mauvais format';
    }
    
    /**
     * Vérification du mot de passe & repeat
     */
    if(isset($_GET['password']) && is_string($_GET['password']) && strlen($_GET['password']) >= 8) {
    	if(isset($_GET['repeat']) && is_string($_GET['repeat'])) {
    		 if($_GET['password'] == $_GET['repeat']) {
    		 	$password = $_GET['password'];
    		 
    		 	// Génération d'un salt
    			$rand = md5(uniqid(rand(), true));
    			$salt = substr($rand, -10);
    			
    			// Implémentation du salt dans le mot de passe
    			$password = md5($password.$salt).$salt;
    		 } else { $error .= ' - mots de passe différents'; }
    	} else { $error .= ' - mot de passe : mauvais format'; }
    } else { $error .= ' - mot de passe : mauvais format'; }
    
    if($error == '') {
    	// Pas d'erreur 
    	
    	/**
    	 * Enregistrement de l'utilisateur
    	 */
    	try {
    		$stmt = $dbh->prepare("INSERT INTO utilisateurs(`nom`,`prenom`,`login`,`password`, `date_tentative`, `nb_tentatives`) VALUES ('?', '?', '?', '?', ?, 0) ");
    		$stmt->bindValue(1, $nom,PDO::PARAM_STR);
    		$stmt->bindValue(2, $prenom,PDO::PARAM_STR);
    		$stmt->bindValue(3, $login,PDO::PARAM_STR);
    		$stmt->bindValue(4, $password, PDO::PARAM_STR);
    		$stmt->bindValue(5, date("Y-m-d H:i:s"));
    		var_dump($stmt);
    		var_dump($nom);
    		$stmt->execute();
    		
    		// Génération d'un token et connexion
    		$token = md5(uniqid(rand(), true));
    		$_SESSION['token'] = $token;
    		$_SESSION['login'] = $_GET['login'];
    	} catch(Exception $e) 
    	{
    		//echo $e->getMessage();
    	}
    	
    }


    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;


?>