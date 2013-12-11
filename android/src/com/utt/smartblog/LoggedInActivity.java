package com.utt.smartblog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;
import backup._LoginController;

import com.utt.smartblog.controller.ArticleController;
import com.utt.smartblog.models.Utilisateur;

public class LoggedInActivity extends FragmentActivity implements OnClickListener {

	private static final String KEY_FRAGMENT = "fragment_save";

	// Fragment actif
	// test mon git
	private String mFragment;
	public Utilisateur user;

	// Fragments
	//private final LoginController loginFragment = new LoginController();
	private final ArticleController articleFragment = new ArticleController();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in);
		
		user = new Utilisateur();
		
		if(this.getIntent().getExtras() != null)
		{
			// Passage du token
    	    user.setToken(this.getIntent().getExtras().getString("token"));
    	    Toast.makeText(this, "Ca y est, on est co token : " + user.getToken(), Toast.LENGTH_LONG).show();
    	    
    	    if (savedInstanceState != null)
    	    {
    	    	mFragment = savedInstanceState.getString(KEY_FRAGMENT);
    	    }
    	    else
    	    {
    	    	mFragment = getIntent().getStringExtra(KEY_FRAGMENT);
    	    }
	            
		 
    	    showFragment(this.articleFragment);
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

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putString(KEY_FRAGMENT, mFragment != null ? mFragment : "");
		super.onSaveInstanceState(outState);
	}

	private void showFragment(final Fragment fragment) {
		if (fragment == null)
			return;

		final FragmentManager fm = getSupportFragmentManager();
		final FragmentTransaction ft = fm.beginTransaction();

		// Animation
		ft.setCustomAnimations(android.R.anim.slide_in_left,
				android.R.anim.slide_out_right);

		// Affecter le nouveau fragment auFrameLayout
		ft.replace(R.id.fragmentContenaire, fragment);

		// Possibilité de retourner à l'écran précédent en appuyant sur le
		// bouton précédent
		ft.addToBackStack(null);
		ft.commit();
	}

}
