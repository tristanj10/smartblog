<?php
require_once('utilisateur.php');
require_once('article.php');

class Commentaire
{
	private $id;
	private $date;
	private $image;
	private $contenu;
	private $article;
	private $auteur;
	
	
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
	
	public function getArticle()
	{
		return $this->article;
	}
	
	public function setArticle($article)
	{
		$this->article = $article;
	}
	
	public function getAuteur()
	{
		return $this->auteur;
	}
	
	public function setAuteur($auteur)
	{
		$this->auteur = $auteur;
	}
	
	public function sauvegarder($dbh)
	{

		$date = date("Y-m-d H:i:s");
		// INSERT
		try {
			
		
			/*$stmt = $dbh->prepare("INSERT INTO commentaires(`date`,`image`,`contenu`, `id_article`,`id_auteur`) VALUES (?, ?, ?, ?, ?) ");
			$stmt->bindValue(1, $date);
			$stmt->bindValue(2, $this->getImage(), PDO::PARAM_STR);
			$stmt->bindValue(3, $this->getContenu(), PDO::PARAM_STR);
			$stmt->bindValue(4, $this->getArticle()->getId(), PDO::PARAM_INT);
			$stmt->bindValue(5, $this->getAuteur()->getId(), PDO::PARAM_INT);
			$stmt->execute();*/
			
			$stmt = $dbh->prepare("INSERT INTO commentaires(`date`,`image`,`contenu`, `id_article`,`id_auteur`) VALUES (?, ?, ?, ?, ?) ");
			$stmt->bindValue(1, $date);
			$stmt->bindValue(2, $this->getImage(), PDO::PARAM_STR);
			$stmt->bindValue(3, $this->getContenu(), PDO::PARAM_STR);
			$stmt->bindValue(4, $this->getArticle()->getId(), PDO::PARAM_INT);
			$stmt->bindValue(5, $this->getAuteur()->getId(), PDO::PARAM_INT);
			$stmt->execute();
			
			$id =  $dbh->lastInsertId();
		}
		catch(Exception $e)
		{
			return 1;
		}

		$this->setId($id);
		return $id;
	}
	
	public static function getCommentaires($dbh, $id_article)
	{
		try
		{
			$stmt2 = $dbh->prepare("SELECT c.*, u.nom, u.prenom, u.login FROM commentaires c, utilisateurs u WHERE c.id_auteur = u.id AND c.id_article = ? ORDER BY c.id DESC");
			$stmt2->bindValue(1, $id_article, PDO::PARAM_INT);
			$stmt2->execute();
		
			$res2 = $stmt2->fetchAll(PDO::FETCH_ASSOC);
			
		
			return $res2;
		}
		catch(Exception $e)
		{
			return false;
		}
	}
	
	public static function getDateDernierCommentaire($dbh, $id_user)
	{
		try
		{
			$stmt = $dbh->prepare("SELECT MAX(date) FROM commentaires WHERE id_auteur = ?");
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
