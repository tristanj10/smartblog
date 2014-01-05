package com.utt.smartblog.models;

/**
 * Classe Utilisateur
 */
public class Utilisateur 
{

	private int id;
	private String nom;
	private String prenom;
	private String password;
	private String login;
	private String date_tentative;
	private int nb_tentatives;
	private String last_article;
	private String last_commentaire;
	
	private String token;
	
	/**
	 * Constructeur
	 */
	public Utilisateur()
	{
		
	}
	
	/**
	 * Getters et Setters
	 */
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getPrenom() {
		return prenom;
	}
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getDate_tentative() {
		return date_tentative;
	}
	public void setDate_tentative(String date_tentative) {
		this.date_tentative = date_tentative;
	}
	public int getNb_tentatives() {
		return nb_tentatives;
	}
	public void setNb_tentatives(int nb_tentatives) {
		this.nb_tentatives = nb_tentatives;
	}
	public String getLast_article() {
		return last_article;
	}
	public void setLast_article(String last_article) {
		this.last_article = last_article;
	}
	public String getLast_commentaire() {
		return last_commentaire;
	}
	public void setLast_commentaire(String last_commentaire) {
		this.last_commentaire = last_commentaire;
	}
	
	public void setToken(String token)
	{
		this.token = token;
	}
	
	public String getToken()
	{
		return token;
	}

	
}

