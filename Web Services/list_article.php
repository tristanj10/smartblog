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

if($_SESSION['token'] == $_POST['token'])
{//*/
	$user = unserialize($_SESSION['user']);

	$articles = Article::lister($dbh);
	
	$str = '{';

	$i = 0;
	foreach($articles as $a)
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
