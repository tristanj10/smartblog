<?php
    $co = 'mysql:dbname=projetaaa;host=localhost';
    $user = 'root';
    $password = 'root';
    
    try {
        $dbh = new PDO($co, $user, $password);
    } catch (PDOException $e) {
        echo 'Connexion échouée : ';// . $e->getMessage();
    }
?>

