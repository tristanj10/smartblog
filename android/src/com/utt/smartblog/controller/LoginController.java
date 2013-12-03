package com.utt.smartblog.controller;





import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.utt.smartblog.R;
import com.utt.smartblog.network.JSONParser;


public class LoginController extends Fragment implements OnClickListener
{

	Button buttonSend = null;
	TextView login;
	TextView password;
	
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
        View view = inflater.inflate(R.layout.login, container, false);
        
        buttonSend = (Button)view.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        return view;
    }

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		// Envoi des données de connexion 
		if(v == buttonSend) {
			login = (EditText) getView().findViewById(R.id.editText1);
			password = (EditText) getView().findViewById(R.id.editText2);
			
			String l = login.getText().toString();
			String p = password.getText().toString();
			
			connexion(l, p);

		}
	}
	
	public void connexion(String login, String password)
	{
		//getting JSON string from URL
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+login+"&password="+password);
		
		try {
	        // Storing each json item in variable
	        String token = json.getString("token");
	        String error = json.getString("error");

	        if(!token.isEmpty()) 
	        {
	        	// Pas d'erreur
	        	Toast.makeText(getActivity(), "Vous êtes connecté ! Token : " + token, Toast.LENGTH_SHORT).show();
	        }
	        else
	        {
	        	// Erreur(s)
	        	Toast.makeText(getActivity(), "Erreur : " + error, Toast.LENGTH_SHORT).show();
	        }
	        
		} catch (JSONException e) {
		    e.printStackTrace();
		}//*/
	}

	


}
