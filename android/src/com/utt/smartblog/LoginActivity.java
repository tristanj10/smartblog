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

public class LoginActivity extends Activity 
{

	
	Utilisateur user;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		user = new Utilisateur();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}
	
	public void connexion(View view)
	{
		// Envoi des données sur la base
		
		EditText et = null;

		
		et = (EditText) getWindow().getDecorView().findViewById(R.id.loginLField);
		user.setLogin(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.passwordLField);
		user.setPassword(et.getText().toString());
		
		//getting JSON string from URL
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+user.getLogin()+"&password="+user.getPassword());
		
		try 
		{
	        // Storing each json item in variable
	        String token = json.getString("token");
	        String error = json.getString("error");

	        if(!token.isEmpty()) 
	        {
	        	// Pas d'erreur
	        	Toast.makeText(this, "Vous êtes connecté ! Token : " + token, Toast.LENGTH_SHORT).show();
	        }
	        else
	        {
	        	// Erreur(s)
	        	Toast.makeText(this, "Erreur : " + error, Toast.LENGTH_LONG).show();
	        }
	        
		} catch (JSONException e) 
		{
		    e.printStackTrace();
		}//*/
	}
	
	public void nouveauCompte(View view)
	{
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

}
