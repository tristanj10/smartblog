<?php

if(isset($_POST['token'])) session_id($_POST['token']);
session_start();
require_once('./head/connexion.php');
require_once('./head/https.php');
require_once('./models/article.php');
require_once('./models/utilisateur.php');


if(empty($_SESSION['token']) || empty($_SESSION['user']))
{
	exit;
}

$error = '';

$article = new Article();
$image = "";

if($_SESSION['token'] == $_POST['token'])
{
	$user = unserialize($_SESSION['user']);

	

	/**
	 * Vérification du titre
	 */
	if(isset($_POST['titre']) && is_string($_POST['titre']) && $_POST['titre'] != '')
	{
		// Filtre
		$titre = htmlspecialchars($_POST['titre']);
	}
	else
	{
		$error .= '\r\n - titre : mauvais format';
	}
	
	/**
	 * Vérification image
	 */
	if(isset($_POST['image']))
	{
		if(is_string($_POST['image']) && $_POST['image'] != '')
		{
			$image = htmlspecialchars($_POST['image']);
		}
		else
		{
			$error .= '\r\n - image : mauvais format';
		}
		
	}
	
	/**
	 * Vérification du contenu
	 */
	if(isset($_POST['contenu']) && is_string($_POST['contenu']) && $_POST['contenu'] != '')
	{
		// Filtre
		$contenu = htmlspecialchars($_POST['contenu']);
	}
	else
	{
		$error .= '\r\n - contenu : mauvais format';
	}
	
	
	/**
	 * Vérification date du dernier article
	 */
	if(!$user->peutEcrireUnArticle($dbh))
	{
		$error = '\r\nMerci d\'attendre 1 minute avant de poster un autre article';
		
	}
	
	
	/**
	 * Enregistrement
	 */
	if($error == '')
	{
		$article->setTitre($titre);
		$article->setImage($image);
		$article->setContenu($contenu);
		$article->setAuteur($user);
		$article->setId($article->sauvegarder($dbh));
	}
	
	$str='{"id":"'.$article->getId().'", "error":"'.$error.'", "token":"'.$_SESSION['token'].'"}';
	
	echo $str;
	
	
}

?>

