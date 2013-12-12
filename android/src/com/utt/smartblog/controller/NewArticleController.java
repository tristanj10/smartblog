package com.utt.smartblog.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.ArticleAdapter;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;


public class NewArticleController extends Fragment implements OnClickListener
{

	Button articleButton = null;
	Utilisateur user = null;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_new_article, container, false);
	    
	    articleButton = (Button)view.findViewById(R.id.articleButton);
	    
	    this.user = ((LoggedInActivity)getActivity()).user;
	    
	    articleButton.setOnClickListener(this);
	    
	    return view;
	}
	
    @Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
    	
    	
		// Envoi des données
		if(v == articleButton) 
		{
			
			EditText titre = (EditText) getView().findViewById(R.id.titreField);
			EditText contenu = (EditText) getView().findViewById(R.id.contenuField);
			
			String strTitre = titre.getText().toString();
			String strContenu = contenu.getText().toString();
			
			nouvelArticle(strTitre, strContenu);
			
			LoggedInActivity monActivity = (LoggedInActivity) this.getActivity();
			monActivity.showFragment(monActivity.articleFragment);
			
		}
		
		
	}

    public void nouvelArticle(String titre, String contenu) {
		// Envoi des données sur la base

    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
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
				Toast.makeText(getActivity(), "C'est bon ! " + id + " " + token, Toast.LENGTH_LONG).show();
				
			} else 
			{
				// Erreur(s)
				Toast.makeText(getActivity(), "Erreur : " + error , Toast.LENGTH_LONG).show();
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

}
