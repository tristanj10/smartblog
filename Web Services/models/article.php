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
	
	
	/**
	 * Renvoie le nombre de likes
	 * @return boolean|unknown
	 */
	public function getLikes()
	{
		if(!$this->existeDeja($dbh, $this->getId()))
			return false;
		
		try
		{
			$stmt = $dbh->prepare("SELECT count(*) as likes FROM likes WHERE id_article = ?");
			$stmt->bindValue(1, $this->getId(),PDO::PARAM_STR);
			$stmt->execute();
				
			$res = $stmt->fetch();
			return $res['likes'];
			
		}
		catch(Exception $e)
		{
			return false;
		}
		
	}
	
	
	/**
	 * Vérifie si un user a déjà like l'article
	 * @param unknown $dbh
	 * @param unknown $id_user
	 * @return boolean|unknown
	 */
	public function aDejaLike($dbh, $id_user)
	{
		if(!$this->existeDeja($dbh, $this->getId()))
			return false;
		
		try
		{
			$stmt = $dbh->prepare("SELECT count(*) as like FROM likes WHERE id_article = ? AND id_utilisateur = ?");
			$stmt->bindValue(1, $this->getId(),PDO::PARAM_STR);
			$stmt->bindValue(1, $id_user,PDO::PARAM_STR);
			$stmt->execute();
		
			$res = $stmt->fetch();
			return $res['like'];
				
		}
		catch(Exception $e)
		{
			return false;
		}
	}
	
	/**
	 * Incrémente le nombre de likes
	 * @param unknown $dbh
	 * @param unknown $id_user
	 * @return boolean
	 */
	public function addLikes($dbh, $id_user)
	{
		if(!$this->existeDeja($dbh, $this->getId())) 
			return false;
		
		if(!$this->aDejaLike($dbh, $id_user))
			return false;
		
		try {
			$stmt = $dbh->prepare("INSERT INTO likes(`id_utilisateur`,`id_article`) VALUES (?, ?) ");
			$stmt->bindValue(1, $id_user , PDO::PARAM_INT);
			$stmt->bindValue(2, $this->getId(), PDO::PARAM_INT);
			$stmt->execute();
			
		}
		catch(Exception $e)
		{
			return false;
		}
		
		return true;
	}
	
	public function getAuteur()
	{
		return $this->auteur;
	}
	
	public function setAuteur($auteur)
	{
		$this->auteur = $auteur;
	}
	
	/**
	 * Charge l'instance 
	 * @param unknown $dbh
	 * @param string $id de l'article
	 * @return boolean
	 */
	public function charger($dbh, $id = null)
	{
		if ($id == null)
		{
			$id = (string) $this->id;
		}
	
		try
		{
			$stmt = $dbh->prepare("SELECT a.*, u.nom, u.prenom, u.login FROM articles a, utilisateurs u WHERE a.id = ? AND a.id_auteur = u.id");
			$stmt->bindValue(1, $id,PDO::PARAM_STR);
			$stmt->execute();
			
			$row = $stmt->fetch();
			
			
			if($stmt->rowCount() == 1)
			{
				$this->setId($row['id']);
				$this->setTitre($row['titre']);
				$this->setDate($row['date']);
				$this->setImage($row['image']);
				$this->setContenu($row['contenu']);
				$this->setNbVues($row['nb_vues']);

				$id_auteur = $row['id_auteur'];
				
				$this->auteur->setId($id_auteur);
				$this->auteur->setNom($row['nom']);
				$this->auteur->setPrenom($row['prenom']);
				$this->auteur->setLogin($row['login']);
				
				return true;
			}
				
			return false;
		}
		catch(Exception $e)
		{
			return false;
		}
	
	}
	
	/**
	 * Vérifie l'existence de l'article
	 * @param unknown $dbh
	 * @param unknown $id
	 * @return boolean
	 */
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
	
	/**
	 * Enregistre/modifie l'article
	 * @param unknown $dbh
	 * @return number|unknown
	 */
	public function sauvegarder($dbh)
	{
	
		// Existe déjà ?
		if($this->existeDeja($dbh, $this->getId()))
		{
			// UPDATE
			try {
				$stmt = $dbh->prepare("UPDATE articles SET titre = ?, date = ?, image = ?, contenu = ?, nb_vues = ? WHERE id = ?");
				$stmt->bindValue(1, $this->getTitre(), PDO::PARAM_STR);
				$stmt->bindValue(2, $this->getDate());
				$stmt->bindValue(3, $this->getImage(), PDO::PARAM_STR);
				$stmt->bindValue(4, $this->getContenu(), PDO::PARAM_STR);
				$stmt->bindValue(5, $this->getNbVues(), PDO::PARAM_INT);
				$stmt->bindValue(6, $this->getId(), PDO::PARAM_INT);
				$stmt->execute();
				
				$id =  $dbh->lastInsertId();
			}
			catch(Exception $e)
			{
				return 0;
			}
		}
		else
		{
			$date = date("Y-m-d H:i:s");
			// INSERT
			try {
				$stmt = $dbh->prepare("INSERT INTO articles(`titre`,`date`,`image`,`contenu`, `nb_vues`,`id_auteur`) VALUES (?, ?, ?, ?, ?, ?) ");
				$stmt->bindValue(1, $this->getTitre(), PDO::PARAM_STR);
				$stmt->bindValue(2, $date);
				$stmt->bindValue(3, $this->getImage(), PDO::PARAM_STR);
				$stmt->bindValue(4, $this->getContenu(), PDO::PARAM_STR);
				$stmt->bindValue(5, 0, PDO::PARAM_INT);
				$stmt->bindValue(6, $this->getAuteur()->getId(), PDO::PARAM_INT);
				$stmt->execute();
				
				$id =  $dbh->lastInsertId();
			}
			catch(Exception $e)
			{
				return 0;
			}
		}
		$this->setId($id);
		return $id;
	}
	

	/**
	 * Renvoie tous les commentaires de l'article
	 * @param unknown $dbh
	 * @return Ambigous <boolean, unknown>
	 */
	public function getCommentaires($dbh)
	{
		return Commentaire::getCommentaires($dbh, $this->getId());	
	}
	
	/**
	 * Liste tous les articles
	 * @param unknown $dbh
	 * @return unknown|boolean
	 */
	public static function lister($dbh)
	{
		try
		{
			$stmt = $dbh->prepare("SELECT a.*, u.nom, u.prenom, u.login, (SELECT count(*) FROM likes l WHERE a.id = l.id_article) as likes FROM articles a, utilisateurs u WHERE a.id_auteur = u.id  ORDER BY a.id DESC");
			$stmt->execute();
		
			$res = $stmt->fetchAll(PDO::FETCH_ASSOC);
		
			
			return $res;
		}
		catch(Exception $e)
		{
			return false;
		}
	}
	
	/**
	 * Retourne la date du dernier article d'un utilisateur
	 * @param unknown $dbh
	 * @param unknown $id_user
	 * @return unknown|boolean
	 */
	public static function getDateDernierArticle($dbh, $id_user)
	{
		try
		{
			$stmt = $dbh->prepare("SELECT MAX(date) FROM articles WHERE id_auteur = ?");
			$stmt->bindValue(1, $id_user,PDO::PARAM_STR);
			$stmt->execute();
				
			$row = $stmt->fetch();
				
			return $row[0];
		}
		catch(Exception $e)
		{
			return false;
		}
	}
}