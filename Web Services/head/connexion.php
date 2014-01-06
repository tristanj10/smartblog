<?php

$co = 'mysql:dbname=smartblog;host=localhost';
$user = 'root';
$password = '';

try {
	$dbh = new PDO($co, $user, $password);
} catch (PDOException $e) {
}

if(!$dbh)
{
	exit;
}
