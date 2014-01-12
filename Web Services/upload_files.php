<?php
$target_path = "./uploads/";
$target_path = $target_path . basename($_FILES['uploadedfile']['name']);

error_log("Upload File >>" . $target_path . $_FILES['error'] . " \r\n", 3,
    "Log.log");

error_log("Upload File >>" . basename($_FILES['uploadedfile']['name']) . " \r\n",
    3, "Log.log");

	
	
$extensions = array ('.png', '.gif', '.jpg', '.jpeg');
// récupère la partie de la chaine à partir du dernier . pour connaître l'extension.
$extension = strrchr ($_FILES['uploadedfile']['name'], '.');
//Ensuite on teste
if (!in_array ($extension, $extensions)) //Si l'extension n'est pas dans le tableau
{
     
}else{	
	if (move_uploaded_file($_FILES['uploadedfile']['tmp_name'], $target_path)) {
		
	} else {
	}
}
?>