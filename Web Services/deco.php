<?php
	// Récupération de la session
	if(isset($_POST['token'])) session_id($_POST['token']);
	session_start();
	require_once('./head/https.php');
	

	$ok = "";
	
	// Vérification du token
	if($_SESSION['token'] == $_POST['token'])
	{
		// Destruction de la session
		$_SESSION = "";
		
		$ok = "ok";
	}
	
	$str = "{'reponse':'".$ok."'}";
	echo $str;
?>