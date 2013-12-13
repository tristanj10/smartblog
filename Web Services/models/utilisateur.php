<?php

class Utilisateur
{
	private $id;
	private $nom;
	private $prenom;
	private $login;
	private $password;
	private $date_tentative;
	private $nb_tentatives;
	
	
	public function __construct() 
	{
		
	}
	
	public function getId()
	{
		return $this->id;
	}
	
	public function setId($id)
	{
		$this->id = $id;
	}
	

	public function getNom()
	{
		return $this->nom;
	}
	
	
	public function setNom($nom)
	{
		$this->nom = $nom;
	}
	
	public function getPrenom()
	{
		return $this->prenom;
	}
	
	public function setPrenom($prenom)
	{
		$this->prenom = $prenom;	
	}
	
	public function getLogin()
	{
		return $this->login;
	}
	
	public function setLogin($login)
	{
		$this->login = $login;
	}
	
	public function getPassword()
	{
		return $this->password;
	}
	
	public function setPassword($password)
	{
        // Génération d'un salt
    	$rand = md5(uniqid(rand(), true));
    	$salt = substr($rand, -10);
    	
    	// Implémentation du salt dans le mot de passe
    	$this->password = md5($password.$salt).$salt;
    	
	}
	
	public function getDateTentative()
	{
		return $this->date_tentative;
	}
	
	public function setDateTentative($date)
	{
		$this->date_tentative = $date;
	}
	
	public function getNbTentatives()
	{
		return $this->nb_tentatives;
	}
	
	public function setNbTentatives($nb)
	{
		$this->nb_tentatives = $nb;
	}
	
	
	public function charger($dbh, $login = null)
	{
		if ($login == null) 
		{
			$login = $this->login;
		} 
		
		try 
		{
			$stmt = $dbh->prepare("SELECT * FROM utilisateurs where login = ?");
			$stmt->bindValue(1, $login,PDO::PARAM_STR);
			$stmt->execute();
			
			$row = $stmt->fetch();
			
			if($stmt->rowCount() == 1)
			{
				$this->setId($row['id']);
				$this->setNom($row['nom']);
				$this->setPrenom($row['prenom']);
				$this->setLogin($row['login']);
				$this->password = $row['password'];
				$this->setDateTentative($row['date_tentative']);
				$this->setNbTentatives($row['nb_tentatives']);
					
				return true;
			}
			
			return false;
		}
		catch(Exception $e)
		{
			return false;
		}
		
	}
	
	
	public function login($dbh, $login, $password)
	{
		
		if($this->charger($dbh, $login)) {
			// 10 derniers caracteres = salt
			$salt = substr($this->password, -10);
			
			if($this->password == md5($password.$salt).$salt)
			{
				return true;
			}
		}
		return false;
	}
	
	public function sauvegarder($dbh)
	{
		
		// Existe déjà ?
		if($this->existeDeja($dbh))
		{
			// UPDATE
			try {
				$stmt = $dbh->prepare("UPDATE utilisateurs SET nb_tentatives = ?, date_tentative = ? WHERE id = ?");
				$stmt->bindValue(1, $this->getNbTentatives(), PDO::PARAM_INT);
				$stmt->bindValue(2, $this->getDateTentative());
				$stmt->bindValue(3, $this->getId(), PDO::PARAM_INT);
				$stmt->execute();
			} 
			catch(Exception $e)
			{
				return false;
			}
		} 
		else
		{
			// INSERT
			try {
				$stmt = $dbh->prepare("INSERT INTO utilisateurs(`nom`,`prenom`,`login`,`password`, `date_tentative`) VALUES (?, ?, ?, ?, ?) ");
				$stmt->bindValue(1, $this->getNom(),PDO::PARAM_STR);
				$stmt->bindValue(2, $this->getPrenom(),PDO::PARAM_STR);
				$stmt->bindValue(3, $this->getLogin(),PDO::PARAM_STR);
				$stmt->bindValue(4, $this->getPassword(), PDO::PARAM_STR);
				$stmt->bindValue(5, $this->getDateTentative());
				$stmt->execute();
				
			} 
			catch(Exception $e)
			{
				return false;
			}
		}
		return true;
	}
	
	public function existeDeja($dbh, $login = null)
	{
		if ($login == null)
		{
			$login = $this->login;
		}
		
		try 
		{
			$stmt = $dbh->prepare("SELECT * FROM utilisateurs where login = ?");
			$stmt->bindValue(1, $login,PDO::PARAM_STR);
			$stmt->execute();
			
			return ($stmt->rowCount() > 0);
		}
		catch(Exception $e)
		{
			return false;
		}
		
	}
	
	public function estBloque()
	{
		return (intval($this->getNbTentatives()) > 3 && ((strtotime(date("Y-m-d H:i:s"))-strtotime($this->getDateTentative())) < 900));
	}
	
	public function peutEtreDebloque()
	{
		return (intval($this->getNbTentatives()) > 3 && ((strtotime(date("Y-m-d H:i:s"))-strtotime($this->getDateTentative())) >= 900));
	}
	
	public function peutEcrireUnArticle($dbh, $id = null)
	{
		if ($id == null)
		{
			$id = $this->id;
		}

		$date = Article::getDernierArticle($dbh, $id);
		
		return ((strtotime(date("Y-m-d H:i:s"))-strtotime($date)) >= 60);
		
	}
	
	
}