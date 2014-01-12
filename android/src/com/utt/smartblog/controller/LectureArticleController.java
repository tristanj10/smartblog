package com.utt.smartblog.controller;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.ArticleAdapter;
import com.utt.smartblog.models.Commentaire;
import com.utt.smartblog.models.CommentaireAdapter;
import com.utt.smartblog.network.JSONParser;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class LectureArticleController extends Fragment implements OnClickListener{
	
	Article article = null;
	LoggedInActivity monActivity = null;
	
	private TextView titre;
	private TextView date;
	private ImageView image;
	private TextView contenu;
	private TextView nb_vues;
	private TextView like;
	private TextView auteur;
	private Button envoi_com;
	private String contenu_com = null;
	private EditText editContenu = null;
	private ListView lescoms = null;
	private Bitmap bitmap = null;
	private URL urlPhoto = null;
	private ImageButton UpLike = null;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_article, container, false);
	   
	    this.monActivity = (LoggedInActivity) this.getActivity();
	    this.article = this.monActivity.getSelectedArticle();
	    
	    this.titre =  (TextView)view.findViewById(R.id.titre);
	    this.date =  (TextView)view.findViewById(R.id.date);
	    this.contenu =  (TextView)view.findViewById(R.id.contenu);
	    this.nb_vues =  (TextView)view.findViewById(R.id.nb_vues);
	    this.like =  (TextView)view.findViewById(R.id.like);
	    this.auteur =  (TextView)view.findViewById(R.id.auteur);
	    this.image =  (ImageView)view.findViewById(R.id.image);
	    this.envoi_com = (Button) view.findViewById(R.id.EnvoiCom);
	    this.editContenu = (EditText) view.findViewById(R.id.Commentaire);
	    this.lescoms = (ListView) view.findViewById(R.id.listCom);
	    this.UpLike = (ImageButton) view.findViewById(R.id.imageButton1);
	    
	    this.titre.setText(this.article.getTitre());
	    this.date.setText(this.article.getDate());
	    this.contenu.setText(this.article.getContenu());
	    this.nb_vues.setText("Vu "+ String.valueOf(this.article.getNb_vues()) + "fois");
	    this.like.setText(String.valueOf(this.article.getLikes()) + " likes");
	    this.auteur.setText((this.article.getAuteur().getNom() + this.article.getAuteur().getPrenom()));
	    //this.image.setImageBitmap(this.article.getImage());
	    
	    //Affichage de l'image
	    try {
	    	//this.urlPhoto = URL.class.getResource("http://10.0.2.2/uploads/" + this.article.getImage());
			 bitmap = BitmapFactory.decodeStream((InputStream)new URL("http://10.0.2.2/uploads/" + this.article.getImage()).getContent());
			 this.image.setImageBitmap(bitmap); 
			 System.out.println("https://10.0.2.2/uploads/" + this.article.getImage());
		} catch (MalformedURLException e) {
			  e.printStackTrace();
		} catch (IOException e) {
			  e.printStackTrace();
		}
	    
	    this.envoi_com.setOnClickListener(this);
	    this.UpLike.setOnClickListener(this);
	    
	    chargerListeCom();
	    System.out.println("Liste chargée.");
	    
	    incrementerVues();
	    
	    return view;
	}

	private void chargerListeCom() {
		// TODO Auto-generated method stub
		
	    	ArrayList<Commentaire> commentaires = new ArrayList<Commentaire>();
	    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
	    	Integer  i = 0;
	    	Commentaire com = null;

			postParameters.add(new BasicNameValuePair("token", this.monActivity.user.getToken()));
			postParameters.add(new BasicNameValuePair("id_article", String.valueOf(this.article.getId())));
			
			JSONObject jsonC = JSONParser.getJSONFromUrl("https://10.0.2.2/list_com.php", postParameters);
			
			try {
				
				
				// Parcours des commentaires
				for(i = 0; i < jsonC.length(); i++)
				{
					// Article 
					JSONObject object2 = jsonC.getJSONObject(i.toString());
					
					com = new Commentaire();
					
					com.setContenu(object2.getString("contenu"));
					com.setDate(object2.getString("date"));
					com.setId(Integer.parseInt(object2.getString("id")));
					com.setNom_auteur(object2.getString("nom") + " " + object2.getString("prenom"));
					com.setImage(object2.getString("image"));
					com.setId_auteur(Integer.parseInt(object2.getString("id_auteur")));
					commentaires.add(com);
				}
				
				
				
				if (!commentaires.isEmpty()) 
				{
					
					CommentaireAdapter adapter2 = new CommentaireAdapter(getActivity(), commentaires);
					lescoms.setAdapter(adapter2);
					//lescoms.setOnItemClickListener(this);
				}
				else 
				{
					// Vide
					System.out.println("C'est vide");
				}
				

			} catch (JSONException e) {
				e.printStackTrace();
			}
	    
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == this.envoi_com){
			
			this.contenu_com = this.editContenu.getText().toString();
			if (Envoi_com(this.contenu_com)){
				
			}else{
				Toast.makeText(this.monActivity, "Un délai de 15 secondes entre chaque commentaire est nécessaire", Toast.LENGTH_SHORT);
			}
			this.chargerListeCom();
			this.editContenu.setText("");
			
			
			
		}else if(v == this.UpLike){
			incrementerLike();
		}
	}
	
	private void incrementerLike() {
		// TODO Auto-generated method stub
		
		System.out.println("On passe par la");
		// Envoi des donnees sur le serveur
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
    	// post
    	postParameters.add(new BasicNameValuePair("token", monActivity.user.getToken()));
		postParameters.add(new BasicNameValuePair("id_article", String.valueOf(this.article.getId())));
		

		// Envoi de la requete post (donnees de l'article)
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/nblikes.php", postParameters);
		try {
			
			// Recuperation de la reponse
			String error = json.getString("error");
			String nb = json.getString("nb");
			
			if (error.isEmpty()) 
			{
				// Pas d'erreur
				this.like.setText(nb + " likes");
			} else 
			{
				// Erreur(s)
				
			}

		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}

	public boolean Envoi_com(String contenu){
		
		Commentaire monCom = new Commentaire(contenu, this.article.getId(), this.monActivity.user.getToken() );
		if(monCom.saveCommentaire()){
			
		}
		
		return false;
	}
	
	private Bitmap resize(Bitmap bm, int w, int h)
	{
		int width = bm.getWidth();
		int height = bm.getHeight();
		int newWidth = w;
		int newHeight = h;
		float scaleWidth = ((float) newWidth) / width;
		float scaleHeight = ((float) newHeight) / height;
 
		Matrix matrix = new Matrix();
		matrix.postScale(scaleWidth, scaleHeight);
		Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
 
		return resizedBitmap;
	}
	
	/**
	 * Incrémente le nombre de vus
	 */
	private void incrementerVues()
	{
		// Envoi des donnees sur le serveur
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
    	// post
    	postParameters.add(new BasicNameValuePair("token", monActivity.user.getToken()));
		postParameters.add(new BasicNameValuePair("id_article", String.valueOf(this.article.getId())));
		

		// Envoi de la requete post (donnees de l'article)
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/nbvues.php", postParameters);
		try {
			
			// Recuperation de la reponse
			String error = json.getString("error");
			String nb = json.getString("nb");
			
			if (error.isEmpty()) 
			{
				// Pas d'erreur
				this.nb_vues.setText("vu " + nb + " fois");
			} else 
			{
				// Erreur(s)
				
			}

		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
	}
}
