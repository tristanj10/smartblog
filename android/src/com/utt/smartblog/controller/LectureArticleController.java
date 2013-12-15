package com.utt.smartblog.controller;

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.models.Article;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class LectureArticleController extends Fragment{
	
	Article article = null;
	LoggedInActivity monActivity = null;
	
	private TextView titre;
	private TextView date;
	private ImageView image;
	private TextView contenu;
	private TextView nb_vues;
	private TextView like;
	private TextView dislike;
	private TextView auteur;

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
	    this.dislike =  (TextView)view.findViewById(R.id.dislike);
	    this.auteur =  (TextView)view.findViewById(R.id.auteur);
	    this.image =  (ImageView)view.findViewById(R.id.image);
	    
	    this.titre.setText(this.article.getTitre());
	    this.date.setText(this.article.getDate());
	    this.contenu.setText(this.article.getContenu());
	    this.nb_vues.setText( String.valueOf(this.article.getNb_vues()));
	    this.like.setText(String.valueOf(this.article.getLike()));
	    this.dislike.setText(String.valueOf(this.article.getDislike()));
	    this.auteur.setText((this.article.getAuteur().getNom() + this.article.getAuteur().getPrenom()));
	    //this.image.setImageBitmap(this.article.getImage());
	    
	    //this.contenu.setText("Blabla de contznue");
	    
	    
	    
	    return view;
	}
	
}
