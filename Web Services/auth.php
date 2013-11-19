<?php
    session_start();
    include_once('./head/bdd.php');
    
    $token = '';
    $error = '';

    if(isset($_GET['login']) && isset($_GET['password'])) {
        
        if(!$dbh) {
            $error = 'Erreur base de données';
        }
        
        try {
            
            // salt DONE
            //$salt = "4251452356"; // rand
            //$password = $_GET['password'];
            //$password_chiffre = sha1($password.$salt).$salt;
            
            $stmt = $dbh->prepare("SELECT * FROM users where login = ?");
            $stmt->bindValue(1, $_GET['login'],PDO::PARAM_STR);
            $stmt->execute();
            
            $row = $stmt->fetch();
            $id = $row['id'];
            if($stmt->rowCount() == 1) {
            	
            	// Bloque ?
            	if(intval($row['nb_tentatives']) < 4) { // Pas de pb
            		
            		// 10 derniers caracteres = salt
            		$salt = substr($row['password'], -10);
            		$pwd = substr($row['password'], 0, -10);
            		 
            		$password = $_GET['password'];
            		(is_string($_GET['password'])) ? $password = $_GET['password'] : $password = '';
            		 
            		if($row['password'] == sha1($password.$salt).$salt) { // Vérification mdp
            		
            			// Bon mot de passe
            			$token = md5(uniqid(rand(), true));
            			$_SESSION['token'] = $token;
            			$_SESSION['login'] = $_GET['login'];
            		} else {
            			$tentatives = intval($row['nb_tentatives']) + 1; // tentatives++
            			$stmt = $dbh->prepare("UPDATE users SET nb_tentatives = ?, date_tentative = ? WHERE id = ?");
            			$stmt->execute(array($tentatives,date("Y-m-d H:i:s"),$id));
            			 
            			$error = "Erreur de connexion";
            		}
            		
            	} elseif (intval($row['nb_tentatives']) > 3 && (strtotime(date("Y-m-d H:i:s"))-strtotime($row['date_tentative']) < 900)) {

           			// Compte bloqué
           			$error = "Cpte bloqué";
           			
            			
           		} else {
            			
           			// Déblocage de compte
           			$stmt = $dbh->prepare("UPDATE users SET nb_tentatives = 0 WHERE id = ?");
           			$stmt->execute(array($id));
	
           			// 10 derniers caracteres = salt
           			$salt = substr($row['password'], -10);
           			$pwd = substr($row['password'], 0, -10);
           			 
           			$password = $_GET['password'];
           			(is_string($_GET['password'])) ? $password = $_GET['password'] : $password = '';
           			 
           			if($row['password'] == sha1($password.$salt).$salt) { // Vérification mdp
           			
           				// Bon mot de passe
           				$token = md5(uniqid(rand(), true));
           				$_SESSION['token'] = $token;
           				$_SESSION['login'] = $_GET['login'];
           			} else {
           				$tentatives = intval($row['nb_tentatives']) + 1; // tentatives++
           				$stmt = $dbh->prepare("UPDATE users SET nb_tentatives = ?, date_tentative = ? WHERE id = ?");
           				$stmt->execute(array($tentatives,date("Y-m-d H:i:s"),$id));
           				 
           				$error = "Erreur de connexion";
           			}
           			
           			
	            } 
            
            } else {
            	// Login inexistant
            	$error = "Erreur de connexion";
            }

        } catch(Exception $e) {
        	echo $e->getMessage();
        }
        
    } else {
        $error = 'Manque paramètres';
        
    }

    $str='{"token":"'.$token.'", "error":"'.$error.'"}';
    echo $str;

/*
-- phpMyAdmin SQL Dump
-- version 4.0.4
-- http://www.phpmyadmin.net
--
-- Client: localhost
-- Généré le: Mer 02 Octobre 2013 à 20:22
-- Version du serveur: 5.6.12-log
-- Version de PHP: 5.4.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


--
-- Base de données: `projetaaa`
--
CREATE DATABASE IF NOT EXISTS `projetaaa` DEFAULT CHARACTER SET latin1 COLLATE latin1_swedish_ci;
USE `projetaaa`;

-- --------------------------------------------------------

--
-- Structure de la table `users`
--

CREATE TABLE IF NOT EXISTS `users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `login` varchar(255) NOT NULL,
  `password` varchar(255) NOT NULL,
  `date_tentative` datetime NOT NULL,
  `nb_tentatives` int(11) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB  DEFAULT CHARSET=latin1 AUTO_INCREMENT=2 ;

--
-- Contenu de la table `users`
--

INSERT INTO `users` (`id`, `login`, `password`, `date_tentative`, `nb_tentatives`) VALUES
(1, 'super', '15e5a6201a2549faf49f4978a0cefc2b134fb9d64251452356', '2013-10-02 20:19:07', 0);


*/

?>