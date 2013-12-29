<?php

if(isset($_POST['token'])) session_id($_POST['token']);
session_start();
require_once('./head/connexion.php');
require_once('./head/https.php');
require_once('./models/article.php');
require_once('./models/utilisateur.php');

//print_r($_SESSION);


if(empty($_SESSION['token']) || empty($_SESSION['user']))
{
	exit;
}

$error = '';

$article = new Article();

if($_SESSION['token'] == $_POST['token'])
{
	$user = unserialize($_SESSION['user']);

	

	/**
	 * Vérification du titre
	 */
	if(isset($_POST['titre']) && is_string($_POST['titre']) && $_POST['titre'] != '')
	{
		$titre = htmlspecialchars($_POST['titre']);
	}
	else
	{
		$error .= '\r\n - titre : mauvais format';
	}
	
	/**
	 * Vérification image /!\
	 * @todo: Insertion d'image Article
	 */
	 if(isset($_POST['image']) && is_string($_POST['image']) && $_POST['image'] != '')
	{
		$image = htmlspecialchars($_POST['image']);
	}
	else
	{
		$error .= '\r\n - image : mauvais format';
	}
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










/*

// Where the file is going to be placed
$target_path = $_SERVER ['DOCUMENT_ROOT'] . "/uploads/";
$small_path = $_SERVER ['DOCUMENT_ROOT'] . "/uploads/smallimg/";

// $target_path = "uploads/";
// Add the original filename to our target path.
$file = basename ( $_FILES ['uploadedfile'] ['name'] );
$target_path = $target_path . $file;
echo "target_path= " . $target_path . " ";

// pour tester que c'est bien une image
$extensions = array (
		'.png',
		'.gif',
		'.jpg',
		'.jpeg',
		'.JPG',
		'.JPEG' 
);
$extension = strrchr ( $_FILES ['uploadedfile'] ['name'], '.' );
$ok = 0;

// check if the file is uploaded
if (in_array ( $extension, $extensions )) {
	if (is_uploaded_file ( $_FILES ['uploadedfile'] ['tmp_name'] )) {
		echo "Fichier= " . $_FILES ['uploadedfile'] ['name'] . " telechargement OK.\n";
		
		if (move_uploaded_file ( $_FILES ['uploadedfile'] ['tmp_name'], $target_path ) === TRUE) {
			echo "The file " . basename ( $_FILES ['uploadedfile'] ['name'] ) . " has been uploaded";
			$ok = 1;
		} else {
			echo "There was an error uploading the file, please try again!";
			echo "error =" . basename ( $_FILES ['uploadedfile'] ['error'] );
			echo "file=" . $_FILES ['uploadedfile'] ['tmp_name'];
		}
	} else {
		echo " Nom du fichier : '" . $_FILES ['uploadedfile'] ['name'];
	}
} else {
	echo "mauvaise extension de fichier";
}

if ($ok == 1) {
	$small_path = $small_path . $file;
	$img_path = $target_path;
	$redimLargeur = 300;
	$redimHteur = 300;
	
	if (! file_exists ( $small_path )) {
		// creation de notre copie de travail de l'image 
		$tmpImg = imagecreatefromjpeg ( $img_path );
		
		// recup des dimensions de l'image a redimensionner 
		$tailleImg = getimagesize ( $img_path );
		
		// calcul des nouvelles dimensions de l'image 
		if ($tailleImg [0] > $tailleImg [1]) {
			
			// format paysage 
			$ratioReduc = (($redimLargeur * 100) / $tailleImg [0]);
			
			$newHteur = (($tailleImg [1] * $ratioReduc) / 100);
			$newLargeur = $redimLargeur;
		} else {
			// format portrait 
			$ratioReduc = (($redimHteur * 100) / $tailleImg [1]);
			
			$newLargeur = (($tailleImg [0] * $ratioReduc) / 100);
			$newHteur = $redimHteur;
		}
		
		// creation de la nouvelle image en couleurs vraies 
		$rszImg = imagecreatetruecolor ( $newLargeur, $newHteur ) or die ( "Erreur" );
		
		// reechantillonnage de l'image 
		imagecopyresampled ( $rszImg, $tmpImg, 0, 0, 0, 0, $newLargeur, $newHteur, $tailleImg [0], $tailleImg [1] );
		
		// enregisttrement de l'image redimensionnée 
		imagejpeg ( $rszImg, $small_path, 100 );
	}
}*/
?>

