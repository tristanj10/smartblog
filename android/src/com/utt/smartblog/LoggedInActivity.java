package com.utt.smartblog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;
import backup._LoginController;

import com.utt.smartblog.controller.ArticleController;
import com.utt.smartblog.controller.LectureArticleController;
import com.utt.smartblog.controller.NewArticleController;
import com.utt.smartblog.models.Utilisateur;

public class LoggedInActivity extends FragmentActivity implements OnClickListener {

	private static final String KEY_FRAGMENT = "fragment_save";

	// Fragment actif
	private String mFragment;
	
	// Fragments
	public final ArticleController articleFragment = new ArticleController();//Controller de la liste des articles
	private final NewArticleController newArticleFragment = new NewArticleController();//Controller de la création d'un nouvel article
	private final LectureArticleController lectureArticleFragment = new LectureArticleController();//Controller de la création d'un nouvel article

	//Composants interactifs
	ImageButton buttonNouvelArticle = null;
	ImageButton buttonRetour = null;
	Button buttonEnvoye = null;
	
	public Utilisateur user;

	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in);
		
		//User vierge
		user = new Utilisateur();
		
		if(this.getIntent().getExtras() != null)
		{
			// Passage du token
    	    user.setToken(this.getIntent().getExtras().getString("token"));
    	    Toast.makeText(this, "Ca y est, on est co token : " + user.getToken(), Toast.LENGTH_LONG).show();//a suppr
    	    
    	    this.buttonNouvelArticle = (ImageButton) findViewById(R.id.newArticleButton);
            this.buttonRetour = (ImageButton) findViewById(R.id.listeArticleButton);
            //this.buttonEnvoye = (Button) findViewById(R.id.articleButton);

            
    		// On spécifie que le listener est notre classe
            this.buttonNouvelArticle.setOnClickListener(this);
            this.buttonRetour.setOnClickListener(this);
            //this.buttonEnvoye.setOnClickListener(this);
    	    
    	    if (savedInstanceState != null)
                mFragment = savedInstanceState.getString(KEY_FRAGMENT);
            else
                mFragment = getIntent().getStringExtra(KEY_FRAGMENT);
             
            if (mFragment != null) {
            	// Sélection d'un choix dans le menu
                if (mFragment.equals(articleFragment.getClass().getSimpleName())) {
                    showFragment(this.articleFragment);
                } else if (mFragment.equals(newArticleFragment.getClass().getSimpleName())) {
                    showFragment(this.newArticleFragment);
                } 
            } else {
                showFragment(this.articleFragment);
            }
        }
		else
		{
			// Erreur
			System.out.println("Erreur");
			finish();
		}
	
	}

	@Override
	public void onResume() {
		super.onResume();

		/*
		 * final Intent callingIntent = getIntent(); token =
		 * callingIntent.getStringExtra("token"); System.out.println(token);
		 */
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logged_in, menu);
		return true;
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		// Choix : Affichage du fragment + changement de couleurs
				if(v == this.buttonNouvelArticle) {
					showFragment(this.newArticleFragment); 
				}else if(v == this.buttonRetour){
					showFragment(this.articleFragment);
				}else if(v == this.buttonEnvoye){
					showFragment(this.articleFragment);
				}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_FRAGMENT, mFragment != null ? mFragment : "");
		super.onSaveInstanceState(outState);
	}
    
    public void showFragment(final Fragment fragment) {
		if (fragment == null)
			return;

        final FragmentManager fm = getSupportFragmentManager();
        final FragmentTransaction ft = fm.beginTransaction();

        // Animation
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        
        // Affecter le nouveau fragment auFrameLayout
        ft.replace(R.id.fragmentContenaire, fragment);
        
        // Possibilité de retourner à l'écran précédent en appuyant sur le bouton précédent
        ft.addToBackStack(null);
        ft.commit();
    }

}
