package com.utt.smartblog.models;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.utt.smartblog.network.JSONParser;

import android.widget.Toast;

public class Commentaire {
	private int id;
	private String date;
	private String image;
	private String contenu;
	private int id_article;
	private int id_auteur;
	
	public Commentaire(){
		
	}
	
	public Commentaire(String contenu, int id_article, int id_auteur){
		this.setContenu(contenu);
		this.setId_article(id_article);
		this.setId_auteur(id_auteur);
	}
	
	public Commentaire(int id, String date, String image, String contenu, int id_article, int id_auteur){
		this.setId(id);
		this.setDate(date);
		this.setImage(image);
		this.setContenu(contenu);
		this.setId_article(id_article);
		this.setId_auteur(id_auteur);
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public int getId_article() {
		return id_article;
	}
	public void setId_article(int id_article) {
		this.id_article = id_article;
	}
	public int getId_auteur() {
		return id_auteur;
	}
	public void setId_auteur(int id_auteur) {
		this.id_auteur = id_auteur;
	}
	
	public boolean saveCommentaire(){
		
		System.out.println("Commentaire : "+this.getContenu() + " " + this.getId_article() + " " + this.getId_auteur() + " sauvegardé");
		
		/*ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
		postParameters.add(new BasicNameValuePair("titre", titre));
		postParameters.add(new BasicNameValuePair("contenu", contenu));
		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));

		// getting JSON string from URL
		// JSONObject json =
		// JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+user.getLogin()+"&password="+user.getPassword());
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/new_article.php", postParameters);
		try {
			// Storing each json item in variable
			String id = json.getString("id");
			String error = json.getString("error");
			String token = json.getString("token");
			
			if (!id.isEmpty()) 
			{
				// Pas d'erreur
				return true;
				
			} else 
			{
				// Erreur(s)
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;*/
		
		
		
		
		return true;
	}
	
	
}
