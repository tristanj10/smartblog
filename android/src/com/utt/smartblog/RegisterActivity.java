package com.utt.smartblog;

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
		
		user = new Utilisateur();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.register, menu);
		return true;
	}
	
	public void envoyer(View view)
	{
		// Envoi des données sur la base
		
		EditText et = null;
		
		et = (EditText) getWindow().getDecorView().findViewById(R.id.nomField);
		user.setNom(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.prenomField);
		user.setPrenom(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.loginField);
		user.setLogin(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.passwordField);
		user.setPassword(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.repeatField);
		
		//getting JSON string from URL
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/register.php?nom="+user.getNom()+"&prenom="+user.getPrenom()+"&login="+user.getLogin()+"&password="+user.getPassword()+"&repeat="+et.getText().toString());
		
		
		try 
		{
	        // Storing each json item in variable
	        String token = json.getString("token");
	        String error = json.getString("error");

	        if(!token.isEmpty()) 
	        {
	        	// Pas d'erreur
	        	Toast.makeText(this, "Vous êtes connecté ! Token : " + token, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(this, LoginActivity.class);
	    		startActivity(intent);
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
