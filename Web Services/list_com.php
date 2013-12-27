<?php
if(isset($_POST['token'])) session_id($_POST['token']);
session_start();
require_once('./head/connexion.php');
require_once('./head/https.php');
require_once('./models/commentaire.php');
require_once('./models/utilisateur.php');

if(empty($_SESSION['token']) || empty($_SESSION['user']))
{
	exit;
}

$error = '';

$commentaire = new Commentaire();

if($_SESSION['token'] == $_POST['token'])
{
	$user = unserialize($_SESSION['user']);
	
	if(isset($_POST['id_article']) && $_POST['id_article'] != '')
	{
		$id_article = intval($_POST['id_article']);
		
	}
	else
	{
		$error .= '\r\n - erreur article';
	}
	
	$commentaires = Commentaire::getCommentaires($dbh, $id_article);
	$str = '{';

	$i = 0;
	foreach($commentaires as $a)
	{
		$str .= '"'.$i.'": { ';
		foreach($a as $key => $value)
		{
			$str .= '"'.$key.'": "'.$value.'", ';
		}
		
		// Suppression de la dernière virgule
		$str = substr($str, 0, -2);
		$str .= '}, ';
		
		$i++;
	}
	
	// Suppression de la dernière virgule
	$str = substr($str, 0, -2);

	$str .= '}'; 
	

	echo $str;

}
