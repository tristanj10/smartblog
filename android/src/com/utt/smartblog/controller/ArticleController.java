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


public class ArticleController extends Fragment implements OnClickListener
{

	Utilisateur user = null;
	ListView listView = null;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_list_article, container, false);
	    listView = (ListView)view.findViewById(R.id.listArticles);
	    

	    
	    this.user = ((LoggedInActivity)getActivity()).user;
	    
	    // Chargement de la liste des articles
	    chargerListe();
	    
	    return view;
	}
	
    @Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
    	
    	
		
		
	}
    
    public void chargerListe()
    {
    	ArrayList<Article> articles = new ArrayList<Article>();
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	Integer  i = 0;
    	Article article = null;

		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));
		
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/list_article.php", postParameters);
		
		try {
			
			
			// Parcours des articles
			for(i = 0; i < json.length(); i++)
			{
				// Article 
				JSONObject object = json.getJSONObject(i.toString());
				
				article = new Article();
				
				// Pr�voir plus de champs : Photo ? D�but de contenu ?
				article.setTitre(object.getString("titre"));
				article.setDate(object.getString("date"));
				article.getAuteur().setNom(object.getString("nom"));
				article.getAuteur().setPrenom(object.getString("prenom"));
				
				articles.add(article);
			}
			
			
			
			if (!articles.isEmpty()) 
			{
				// Pas d'erreur
				Toast.makeText(getActivity(), "C'est bon ! ", Toast.LENGTH_LONG).show();
				
				/*
				 * Test
				 */
				for(i = 0; i < articles.size(); i++)
				{
					System.out.println("Titre : " + articles.get(i).getTitre());
					System.out.println("Titre : " + articles.get(i).getDate());
					System.out.println("Nom : " + articles.get(i).getAuteur().getNom());
					System.out.println("Pr�nom : " + articles.get(i).getAuteur().getPrenom());
					System.out.println("\n");
				}
				
				ArticleAdapter adapter = new ArticleAdapter(getActivity(), articles);
				listView.setAdapter(adapter);
			}
			else 
			{
				// Vide
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
    }
	
}
