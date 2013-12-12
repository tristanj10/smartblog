package backup;

import com.utt.smartblog.R;
import com.utt.smartblog.R.id;
import com.utt.smartblog.R.layout;
import com.utt.smartblog.R.menu;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CopyOfMainActivity extends FragmentActivity implements OnClickListener {
	

	private static final String KEY_FRAGMENT = "fragment_save";
	
	
	// Fragment actif
	//test mon git
	private String mFragment;
	private String token;
	
	private Boolean lance = false;
	
	// Fragments
	private final _LoginController loginFragment = new _LoginController();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		
		/*
		 if (savedInstanceState != null)
	            mFragment = savedInstanceState.getString(KEY_FRAGMENT);
	     else
	            mFragment = getIntent().getStringExtra(KEY_FRAGMENT);
		 
		 showFragment(this.loginFragment);
		*/
	}
	
	@Override
	public void onResume()
	{
		super.onResume();
		
		if(!lance)
		{
			Intent intent = new Intent(this, _LoginActivity.class);
			startActivity(intent);
			lance = true;
		}
		/*final Intent callingIntent = getIntent();
		token = callingIntent.getStringExtra("token");
		System.out.println(token);
		*/
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
	        
	        // Possibilit� de retourner � l'�cran pr�c�dent en appuyant sur le bouton pr�c�dent
	        ft.addToBackStack(null);
	        ft.commit();
	    }
}