<?php 

$login = "test@test.fr";
$password = "lolol";

$rand = md5(uniqid(rand(), true));
$salt = substr($rand, -10);

echo md5($password.$salt).$salt;


?>