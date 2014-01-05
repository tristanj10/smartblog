package com.utt.smartblog;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends Activity 
{

	Utilisateur user;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		//Cr�e un utilisateur vierge
		user = new Utilisateur();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void retour(View view)
	{
		//retour au menu principal
		Intent intent = new Intent(this, MainActivity.class);
		startActivity(intent);
	}
	
	public void envoyer(View view)
	{
		// Envoi des donn�es sur la base
		
		//Cr�e un EditText pour r�cup�rer les info renseign�es.
		EditText et = null;
		
		//tableau qui contiendra tous les parametres pour la requ�tes HTTPS/POST
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		//On r�cup les infos et on les met dans notre user vierge qui ne l'est plus.
		et = (EditText) getWindow().getDecorView().findViewById(R.id.nomField);
		user.setNom(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.prenomField);
		user.setPrenom(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.loginField);
		user.setLogin(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.passwordField);
		user.setPassword(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.repeatField);
		
		//On renseigne les params � envoyer au web service
		postParameters.add(new BasicNameValuePair("nom", user.getNom()));
		postParameters.add(new BasicNameValuePair("prenom", user.getPrenom()));
		postParameters.add(new BasicNameValuePair("login", user.getLogin()));
		postParameters.add(new BasicNameValuePair("password", user.getPassword()));
		postParameters.add(new BasicNameValuePair("repeat", et.getText().toString()));
		
		//On envoie la requete au web service en HTTPS
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/register.php", postParameters);
		
		try 
		{
	        //on recup la params envoy� par le web service
	        String token = json.getString("token");
	        String error = json.getString("error");

	        if(!token.isEmpty()) 
	        {
	        	// Pas d'erreur, on retourne au menu principal
	        	Toast.makeText(this, "Compte cr�� avec succ�s", Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(this, MainActivity.class);
	    		startActivity(intent);
	    		finish();
	        }
	        else
	        {
	        	// Erreur(s)
	        	Toast.makeText(this, "Erreur : " + error, Toast.LENGTH_LONG).show();
	        }
	        
		} catch (JSONException e) {
		    e.printStackTrace();
		}//*/
	}

}
