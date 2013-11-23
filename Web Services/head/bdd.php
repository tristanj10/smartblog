<?php
    $co = 'mysql:dbname=smartblog;host=localhost';
    $user = 'root';
    $password = '';
    
    try {
        $dbh = new PDO($co, $user, $password);
    } catch (PDOException $e) {
        echo 'Connexion échouée : ';// . $e->getMessage();
    }
    
    if(!$dbh) {
    	$error = 'Erreur base de données';
    }
?>

