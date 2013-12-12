package com.utt.smartblog;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import backup._LoginController;

import com.utt.smartblog.R;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.Toast;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements OnClickListener {
//La gestion des �v�nements se fait par les Layouts dans le XML (Click Bouton)
	
	private Utilisateur user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//Cr�e un utilisateur vierge
		user = new Utilisateur();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	public void connexion(View view) {
		// Envoi des donn�es sur la base
		
		//cr�e un EditText qui permet de r�cup�rer le login et le mdp renseign�
		EditText et = null;
		
		//Tableau qui contiendra les param�tres � envoyer en HTTP/POST
		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		//On r�cup le login dans le EditText
		et = (EditText) getWindow().getDecorView().findViewById(
				R.id.loginLField);
		//On renseigne le login de l'Utilisateur
		user.setLogin(et.getText().toString());
		
		//On r�cup le mdp dans le EditText
		et = (EditText) getWindow().getDecorView().findViewById(
				R.id.passwordLField);
		//On renseigne le mdp de l'Utilisateur
		user.setPassword(et.getText().toString());
		
		//On ajoute les parametre dans le tableau
		postParameters.add(new BasicNameValuePair("login", user.getLogin()));
		postParameters.add(new BasicNameValuePair("password", user.getPassword()));
		
		//On POST les param�tres au WebService pour s'authentifier en HTTPS
		JSONObject json = JSONParser.getJSONFromUrl(
				"https://10.0.2.2/auth.php", postParameters);
		try {
			//On stock les param renvoy� par le WebService
			String token = json.getString("token");
			String error = json.getString("error");

			if (!token.isEmpty()) {
				// Pas d'erreur, on ouvre l'activit� LoggedIn
				//On cr�e l'intent
				Intent intent = new Intent(this, LoggedInActivity.class);
				//on ajoute le token en extra (permet de le renvoyer le cas �ch�ant)
				intent.putExtra("token", token);
				//On d�marre l'activit�
				startActivity(intent);
			} else {
				// Erreur(s)
				Toast.makeText(this, "Erreur : " + error, Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}// */
	}

	public void nouveauCompte(View view) {
		//On d�marre l'activit� qui permet de cr�e un nouveau compte Utilisateur
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
