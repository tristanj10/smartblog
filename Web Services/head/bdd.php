<?php
    $co = 'mysql:dbname=smartblog;host=localhost';
    $user = 'root';
    $password = '';
    
    try {
        $dbh = new PDO($co, $user, $password);
    } catch (PDOException $e) {
        echo 'Connexion �chou�e : ';// . $e->getMessage();
    }
    
    if(!$dbh) {
    	$error = 'Erreur base de donn�es';
    }
?>

