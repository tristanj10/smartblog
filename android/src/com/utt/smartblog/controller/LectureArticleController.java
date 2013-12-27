package com.utt.smartblog.controller;

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

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
	    
	    this.titre.setText(this.article.getTitre());
	    this.date.setText(this.article.getDate());
	    this.contenu.setText(this.article.getContenu());
	    this.nb_vues.setText( String.valueOf(this.article.getNb_vues()));
	    this.like.setText(String.valueOf(this.article.getLikes()));
	    this.auteur.setText((this.article.getAuteur().getNom() + this.article.getAuteur().getPrenom()));
	    //this.image.setImageBitmap(this.article.getImage());
	    
	    this.envoi_com.setOnClickListener(this);
	    
	    chargerListeCom();
	    System.out.println("Liste chargée.");
	    
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
					com.setImage(object2.getString("image"));
					com.setId_auteur(Integer.parseInt(object2.getString("id_auteur")));
					
					commentaires.add(com);
					System.out.println(object2.getString("contenu"));
					//System.out.println(commentaires.get(i).getContenu());
					//System.out.println(com.getContenu());
				}
				
				
				
				if (!commentaires.isEmpty()) 
				{
					// Pas d'erreur
					Toast.makeText(getActivity(), "C'est bon ! ", Toast.LENGTH_LONG).show();
					
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
				System.out.println("PUTAIN");
			}
	    
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		if(v == this.envoi_com){
			this.contenu_com = this.editContenu.getText().toString();
			Envoi_com(this.contenu_com);
		}
	}
	
	public boolean Envoi_com(String contenu){
		
		Commentaire monCom = new Commentaire(contenu, this.article.getId(), this.monActivity.user.getToken() );
		if(monCom.saveCommentaire()){
			System.out.println("Recharge la page");
		}
		
		return false;
	}
	
}
