package com.utt.smartblog.models;

public class Article 
{

	private int id;
	private String titre;
	private String date;
	private String image;
	private String contenu;
	private int nb_vues;
	private int like;
	private int dislike;
	private Utilisateur auteur;
	
	
	
	
	public Article() {
		this.auteur = new Utilisateur();
	}

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getTitre() {
		return titre;
	}
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getContenu() {
		return contenu;
	}
	public void setContenu(String contenu) {
		this.contenu = contenu;
	}
	public int getNb_vues() {
		return nb_vues;
	}
	public void setNb_vues(int nb_vues) {
		this.nb_vues = nb_vues;
	}
	public int getLike() {
		return like;
	}
	public void setLike(int like) {
		this.like = like;
	}
	public int getDislike() {
		return dislike;
	}
	public void setDislike(int dislike) {
		this.dislike = dislike;
	}

	public Utilisateur getAuteur() {
		return auteur;
	}
	public void setAuteur(Utilisateur auteur) {
		this.auteur = auteur;
	}
	
	
	
}