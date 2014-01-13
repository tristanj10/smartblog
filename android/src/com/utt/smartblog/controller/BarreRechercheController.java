package com.utt.smartblog.controller;

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.MainActivity;
import com.utt.smartblog.R;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class BarreRechercheController extends Fragment implements OnClickListener {

	private ImageButton buttonNouvelArticle = null;
	private ImageButton buttonRetour = null;
	private ImageButton buttonDeco = null;
	private LoggedInActivity monActivity = null;
	
	
	
	 @Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		 
		    View view = inflater.inflate(R.layout.fragment_menu, container, false);
		    
		    //Initialisation des éléments graphiques
		    this.buttonNouvelArticle = (ImageButton) view.findViewById(R.id.newArticleButton);
            this.buttonRetour = (ImageButton) view.findViewById(R.id.listeArticleButton);
            this.buttonDeco = (ImageButton) view.findViewById(R.id.decoButton);
            
            //Activity du fragment
            this.monActivity = (LoggedInActivity) this.getActivity();
		    
            //Les Listener
            this.buttonNouvelArticle.setOnClickListener(this);
            this.buttonRetour.setOnClickListener(this);
		    this.buttonDeco.setOnClickListener(this);
            
		    return view;
		}
	
	@Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
		//On ouvre le fragment pour créer un nouvel article
		if(v == this.buttonNouvelArticle) 
		{
			monActivity.showFragment(monActivity.newArticleFragment); 
		}
		//On ouvre le fragment qui affiche tous les articles
		else if(v == this.buttonRetour)
		{
			//monActivity.articleFragment.chargerListe();
			monActivity.showFragment(monActivity.articleFragment);
		}
		//On se déco de l'appli
		else if(v == this.buttonDeco)
		{
			monActivity.deconnexion();
		}
		
	}
	

}
