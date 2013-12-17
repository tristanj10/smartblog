<?php 

if(isset($_POST['token'])) session_id($_POST['token']);
session_start();
require_once('./head/connexion.php');
require_once('./head/https.php');
require_once('./models/article.php');
require_once('./models/commentaire.php');
require_once('./models/utilisateur.php');

//print_r($_SESSION);


if(empty($_SESSION['token']) || empty($_SESSION['user']))
{
	exit;
}

$error = '';

$commentaire = new Commentaire();
$article = new Article();

if($_SESSION['token'] == $_POST['token'])
{
	$user = unserialize($_SESSION['user']);


	
	/**
	 * Vérification image /!\
	 * @todo: Insertion d'image Article
	 */
	
	/**
	 * Vérification du contenu
	 */
	if(isset($_POST['contenu']) && is_string($_POST['contenu']) && $_POST['contenu'] != '')
	{
		$contenu = htmlspecialchars($_POST['contenu']);
	}
	else
	{
		$error .= '\r\n - contenu : mauvais format';
	}
	
	/**
	 * Vérification de l'id de l'article
	 */
	if(isset($_POST['id_article']) && is_int($_POST['id_article']) && $_POST['id_article'] != '')
	{
		$id_article = intval($_POST['id_article']);
		if(!$article->charger($dbh, $id_article))
			$error .= '\r\n - article inexistant';
	}
	else
	{
		$error .= '\r\n - erreur article';
	}
	
	/**
	 * Vérification date du dernier commentaire
	 */
	if(!$user->peutEcrireUnCommentaire($dbh))
	{
		$error = '\r\nMerci d\'attendre 15 secondes avant de poster un autre article';
		
	}
	
	
	/**
	 * Enregistrement
	 */
	if($error == '')
	{
		// Ne pas oublier d'ajouter l'image
		$commentaire->setContenu($contenu);
		$commentaire->setAuteur($user);
		$commentaire->setArticle($article);
		$commentaire->setId($commentaire->sauvegarder($dbh));
	}
	
	$str='{"id":"'.$article->getId().'", "error":"'.$error.'", "token":"'.$_SESSION['token'].'"}';
	
	echo $str;
	
}


