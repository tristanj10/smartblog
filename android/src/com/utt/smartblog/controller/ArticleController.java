package com.utt.smartblog.controller;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ClipData.Item;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.ArticleAdapter;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;

/**
 * Fragment Article
 */
public class ArticleController extends Fragment implements OnClickListener, OnItemClickListener
{

	
	Utilisateur user = null; // Utilisateur courant
	ListView listView = null; // Liste d'articles
	LoggedInActivity monActivity = null; // Activite LoggedIn
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_list_article, container, false);
	   
	    //Initialisation des éléments graphiques
	    listView = (ListView)view.findViewById(R.id.listArticles);
	    monActivity = (LoggedInActivity) this.getActivity();
	    
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
    
    /**
     * Méthode de chargement de la liste des articles
     */
    public void chargerListe()
    {
    	ArrayList<Article> articles = new ArrayList<Article>();
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	Integer  i = 0;
    	Article article = null;

    	// Preparation de la requete POST (token)
		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));
		
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/list_article.php", postParameters);
		
		// Recherche des articles
		try {

			// Parcours des articles
			for(i = 0; i < json.length(); i++)
			{
				// Article 
				JSONObject object = json.getJSONObject(i.toString());
				
				article = new Article();
				
				// Chargement des articles
				article.setTitre(object.getString("titre"));
				article.setDate(object.getString("date"));
				article.setContenu(object.getString("contenu"));
				article.getAuteur().setNom(object.getString("nom"));
				article.getAuteur().setPrenom(object.getString("prenom"));
				article.setImage(object.getString("image"));
				article.setNb_vues(Integer.parseInt(object.getString("nb_vues")));
				article.setLike(Integer.parseInt(object.getString("likes")));
				
				article.setId(object.getInt("id"));
				
				articles.add(article);
			}
			
			
			
			if (!articles.isEmpty()) 
			{
				
				/*
				 * Test
				 */
				for(i = 0; i < articles.size(); i++)
				{
					System.out.println("Titre : " + articles.get(i).getTitre());
					System.out.println("Date : " + articles.get(i).getDate());
					System.out.println("Nom : " + articles.get(i).getAuteur().getNom());
					System.out.println("Prénom : " + articles.get(i).getAuteur().getPrenom());
					System.out.println("\n");
				}
				
				ArticleAdapter adapter = new ArticleAdapter(getActivity(), articles);
				listView.setAdapter(adapter);
				listView.setOnItemClickListener(this);
			}
			else 
			{
				// Vide, pas d'article
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
    }

    /**
     * Méthode pour gérer les click sur les items de la listView
     */
	@Override
	public void onItemClick(AdapterView<?> parent, View  view, int position, long id) {
		// TODO Auto-generated method stub
		
		//On récupère l'article Selectionné
		Article  item = (Article) listView.getItemAtPosition(position);
		
		//On passe a l'activity l'article selectionné.
		this.monActivity.setSelectedArticle(item);
		
		//On lance le fragment de lecture de l'article selectionné.
		this.monActivity.showFragment(this.monActivity.lectureArticleFragment);
	}
	
}
