package com.utt.smartblog;

import com.utt.smartblog.R;
import com.utt.smartblog.controller.LoginController;

import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements OnClickListener {
	

	private static final String KEY_FRAGMENT = "fragment_save";
	
	// Fragment actif
	private String mFragment;
	
	// Fragments
	private final LoginController loginFragment = new LoginController();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		 if (savedInstanceState != null)
	            mFragment = savedInstanceState.getString(KEY_FRAGMENT);
	     else
	            mFragment = getIntent().getStringExtra(KEY_FRAGMENT);
		 
		 showFragment(this.loginFragment);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
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
	        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	        
	        // Affecter le nouveau fragment auFrameLayout
	        ft.replace(R.id.fragmentContenaire, fragment);
	        
	        // Possibilité de retourner à l'écran précédent en appuyant sur le bouton précédent
	        ft.addToBackStack(null);
	        ft.commit();
	    }
}
