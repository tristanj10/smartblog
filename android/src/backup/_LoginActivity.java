package backup;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.utt.smartblog.MainActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.RegisterActivity;
import com.utt.smartblog.R.id;
import com.utt.smartblog.R.layout;
import com.utt.smartblog.R.menu;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class _LoginActivity extends Activity 
{

	
	Utilisateur user;
	
	
	/*@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		
		user = new Utilisateur();
	}*/

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

		ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
		
		et = (EditText) getWindow().getDecorView().findViewById(R.id.loginLField);
		user.setLogin(et.getText().toString());
		et = (EditText) getWindow().getDecorView().findViewById(R.id.passwordLField);
		user.setPassword(et.getText().toString());
		
		postParameters.add(new BasicNameValuePair("login", user.getLogin()));
		postParameters.add(new BasicNameValuePair("password", user.getPassword()));
		
		//getting JSON string from URL
		//JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+user.getLogin()+"&password="+user.getPassword());
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php", postParameters);
		try 
		{
	        // Storing each json item in variable
	        String token = json.getString("token");
	        String error = json.getString("error");

	        if(!token.isEmpty()) 
	        {
	        	// Pas d'erreur
	        	//Toast.makeText(this, "Vous êtes connecté ! Token : " + token, Toast.LENGTH_SHORT).show();
	        	Intent intent = new Intent(this, MainActivity.class);
	        	intent.putExtra("token", token);
	    		startActivity(intent);
	    		finish();
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
