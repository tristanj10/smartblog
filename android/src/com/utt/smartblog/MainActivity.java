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

	private Utilisateur user;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

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

		EditText et = null;

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();

		et = (EditText) getWindow().getDecorView().findViewById(
				R.id.loginLField);
		user.setLogin(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(
				R.id.passwordLField);
		user.setPassword(et.getText().toString());

		postParameters.add(new BasicNameValuePair("login", user.getLogin()));
		postParameters.add(new BasicNameValuePair("password", user.getPassword()));

		// getting JSON string from URL
		// JSONObject json =
		// JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+user.getLogin()+"&password="+user.getPassword());
		JSONObject json = JSONParser.getJSONFromUrl(
				"https://10.0.2.2/auth.php", postParameters);
		try {
			// Storing each json item in variable
			String token = json.getString("token");
			String error = json.getString("error");

			if (!token.isEmpty()) {
				// Pas d'erreur
				//Toast.makeText(this, "Vous �tes connect� ! Token : " + token, Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(this, LoggedInActivity.class);
				intent.putExtra("token", token);
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
		Intent intent = new Intent(this, RegisterActivity.class);
		startActivity(intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}
}
