<?php
require_once('utilisateur.php');

class Article
{
	private $id;
	private $titre;
	private $date;
	private $image;
	private $contenu;
	private $nb_vues;
	private $like;
	private $dislike;
	private $auteur;
	
	
	public function __construct()
	{
		$this->auteur = new Utilisateur();
	}

	public function getId()
	{
		return $this->id;
	}
	
	public function setId($id)
	{
		$this->id = $id;
	}
	
	public function getTitre()
	{
		return $this->titre;
	}
	
	public function setTitre($titre)
	{
		$this->titre = $titre;
	}
	
	public function getDate()
	{
		return $this->date;
	}
	
	public function setDate($date)
	{
		$this->date = $date;
	}
	
	public function getImage()
	{
		return $this->image;
	}
	
	public function setImage($image)
	{
		$this->image = $image;
	}
	
	public function getContenu()
	{
		return $this->contenu;
	}
	
	public function setContenu($contenu)
	{
		$this->contenu = $contenu;
	}
	
	public function getNbVues()
	{
		return $this->nb_vues;
	}
	
	public function setNbVues($nb_vues)
	{
		$this->nb_vues = $nb_vues;
	}
	
	public function getLike()
	{
		return $this->like;
	}
	
	public function setLike($like)
	{
		$this->like = $like;
	}
	
	public function getDislike()
	{
		return $this->dislike;
	}
	
	public function setDislike($dislike)
	{
		$this->dislike = $dislike;
	}
	
	public function getAuteur()
	{
		return $this->auteur;
	}
	
	public function setAuteur($auteur)
	{
		$this->auteur = $auteur;
	}
	
	public function charger($dbh, $id = null)
	{
		if ($login == null)
		{
			$login = $this->login;
		}
	
		try
		{
			$stmt = $dbh->prepare("SELECT a.*, u.id, u.nom, u.prenom, u.login FROM articles a, utilisateurs u WHERE a.id = ? AND a.id_auteur = u.id");
			$stmt->bindValue(1, $id,PDO::PARAM_STR);
			$stmt->execute();
				
			$row = $stmt->fetch();
				
			if($stmt->rowCount() == 1)
			{
				$this->setId($row['a.id']);
				$this->setTitre($row['a.titre']);
				$this->setDate($row['a.date']);
				$this->setImage($row['a.image']);
				$this->setContenu($row['a.contenu']);
				$this->setNbVues($row['a.nb_vues']);
				$this->setLike($row['a.like']);
				$this->setDislike($row['a.dislike']);

				$id_auteur = $row['id_auteur'];
				
				$this->auteur->setNom($row['u.nom']);
				$this->auteur->setPrenom($row['u.prenom']);
				$this->auteur->setLogin($row['u.login']);
				
				return true;
			}
				
			return false;
		}
		catch(Exception $e)
		{
			return false;
		}
	
	}
	
	public function existeDeja($dbh, $id)
	{
		
		if($id == null)
		{
			return false;
		}

		try
		{
			$stmt = $dbh->prepare("SELECT * FROM articles where id = ?");
			$stmt->bindValue(1, $id,PDO::PARAM_STR);
			$stmt->execute();
				
			return ($stmt->rowCount() > 0);
		}
		catch(Exception $e)
		{
			return false;
		}
	
	}
	
	public function sauvegarder($dbh)
	{
	
		// Existe déjà ?
		if($this->existeDeja($dbh, $this->getId()))
		{
			// UPDATE
			try {
				$stmt = $dbh->prepare("UPDATE articles SET titre = ?, date = ?, image = ?, contenu = ?, nb_vues = ?, like = ?, dislike = ?, id_auteur = ? WHERE id = ?");
				$stmt->bindValue(1, $this->getTitre(), PDO::PARAM_STR);
				$stmt->bindValue(2, $this->getDate());
				$stmt->bindValue(3, $this->getImage(), PDO::PARAM_STR);
				$stmt->bindValue(4, $this->getContenu(), PDO::PARAM_STR);
				$stmt->bindValue(5, $this->getNbVues(), PDO::PARAM_INT);
				$stmt->bindValue(6, $this->getLike(), PDO::PARAM_INT);
				$stmt->bindValue(7, $this->getDislike(), PDO::PARAM_INT);
				$stmt->bindValue(8, $this->getAuteur()->getId(), PDO::PARAM_INT);
				$stmt->bindValue(9, $this->getId(), PDO::PARAM_INT);
				$stmt->execute();
				
				$id =  $dbh->lastInsertId();
			}
			catch(Exception $e)
			{
				return "";
			}
		}
		else
		{
			// INSERT
			try {
				$stmt = $dbh->prepare("INSERT INTO articles(`titre`,`date`,`image`,`contenu`, `nb_vues`, `like`, `dislike`,`id_auteur`) VALUES (?, ?, ?, ?, ?, ?, ?, ?) ");
				$stmt->bindValue(1, $this->getTitre(), PDO::PARAM_STR);
				$stmt->bindValue(2, date("Y-m-d H:i:s"));
				$stmt->bindValue(3, $this->getImage(), PDO::PARAM_STR);
				$stmt->bindValue(4, $this->getContenu(), PDO::PARAM_STR);
				$stmt->bindValue(5, 0, PDO::PARAM_INT);
				$stmt->bindValue(6, 0, PDO::PARAM_INT);
				$stmt->bindValue(7, 0, PDO::PARAM_INT);
				$stmt->bindValue(8, $this->getAuteur()->getId(), PDO::PARAM_INT);
				$stmt->execute();
				
				$id =  $dbh->lastInsertId();
				
				$this->getAuteur()->setLastArticle(date("Y-m-d H:i:s"));
				$this->getAuteur()->sauvegarder($dbh);
			}
			catch(Exception $e)
			{
				return "";
			}
		}
		return $id;
	}
	
	/**
	 * @todo : A FINIR
	 * @param unknown $dbh
	 * @return boolean
	 */
	
	public static function lister($dbh)
	{
		try
		{
			$stmt = $dbh->prepare("SELECT a.*, u.id, u.nom, u.prenom, u.login FROM articles a, utilisateurs u WHERE a.id = ? AND a.id_auteur = u.id");
			$stmt->bindValue(1, $id,PDO::PARAM_STR);
			$stmt->execute();
		
			$row = $stmt->fetchAll();
			
		/*
			if($stmt->rowCount() == 1)
			{
				$this->setId($row['a.id']);
				$this->setTitre($row['a.titre']);
				$this->setDate($row['a.date']);
				$this->setImage($row['a.image']);
				$this->setContenu($row['a.contenu']);
				$this->setNbVues($row['a.nb_vues']);
				$this->setLike($row['a.like']);
				$this->setDislike($row['a.dislike']);
		
				$id_auteur = $row['id_auteur'];
		
				$this->auteur->setNom($row['u.nom']);
				$this->auteur->setPrenom($row['u.prenom']);
				$this->auteur->setLogin($row['u.login']);
		
				return true;
			}*/
		
			return false;
		}
		catch(Exception $e)
		{
			return false;
		}
	}
}
