<?php 

if(isset($_POST['token'])) session_id($_POST['token']);
session_start();
require_once('./head/connexion.php');
require_once('./head/https.php');
require_once('./models/article.php');
require_once('./models/commentaire.php');
require_once('./models/utilisateur.php');


if(empty($_SESSION['token']) || empty($_SESSION['user']))
{
	exit;
}

$error = '';

$commentaire = new Commentaire();
$article = new Article();
$nb = ""; // Nb de like

if($_SESSION['token'] == $_POST['token'])
{
	$user = unserialize($_SESSION['user']);


	
	/**
	 * Vérification de l'id de l'article
	 */
	if(isset($_POST['id_article']) && $_POST['id_article'] != '')
	{
		$id_article = intval($_POST['id_article']);
		if(!$article->charger($dbh, $id_article))
			$error .= '\r\n - article inexistant';
		else
			$article->setId($id_article);
	}
	else
	{
		$error .= '\r\n - erreur article';
	}

	/**
	 * Enregistrement
	 */
	if($error == '')
	{
		$res = $article->addLikes($dbh, $user->getId());
		$nb = intval($article->getLikes($dbh));
		
		if(!$res){
			$error .= '\r\n - erreur addLike';
		}
	}
	
	$str='{"nb":"'.$nb.'", "error":"'.$error.'"}';
	
	echo $str;
	
}


