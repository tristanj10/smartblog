<?php
// Interdiction de passer en http
if(!isset($_SERVER["HTTPS"]))
{
	header('Location: https://' . $_SERVER['HTTP_HOST'] . $_SERVER['REQUEST_URI']);
	exit();
}