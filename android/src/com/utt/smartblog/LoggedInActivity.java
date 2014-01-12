package com.utt.smartblog;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.MediaStore.Images;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.utt.smartblog.controller.ArticleController;
import com.utt.smartblog.controller.LectureArticleController;
import com.utt.smartblog.controller.NewArticleController;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.JSONParser;
import com.utt.smartblog.network.UploadFiles;

public class LoggedInActivity extends FragmentActivity {

	private static final String KEY_FRAGMENT = "fragment_save";

	// Fragment actif
	private String mFragment;
	
	// Fragments
	public final ArticleController articleFragment = new ArticleController();//Controller de la liste des articles
	public final NewArticleController newArticleFragment = new NewArticleController();//Controller de la création d'un nouvel article
	public final LectureArticleController lectureArticleFragment = new LectureArticleController();//Controller de la création d'un nouvel article
	
	public Utilisateur user;
	private Article selectedArticle = null;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static final int CHOISIR_PHOTO = 300;
	public Uri fileURI = null;
	
	
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
    	    
            showFragment(this.articleFragment);
            
        }
		else
		{
			// Erreur
			finish();
		}
	
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.logged_in, menu);
		return true;
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
    
    public Article getSelectedArticle(){
    	return this.selectedArticle;
    }
    
    public void setSelectedArticle(Article article){
    	this.selectedArticle = article;
    }
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // Image captured and saved to fileUri specified in the Intent
               
                Bitmap bmp = BitmapFactory.decodeFile(fileURI.getPath());
                ImageView tmp = new ImageView(this);
                
                tmp = (ImageView)findViewById(R.id.photo);
                tmp.setImageBitmap(bmp);
                
            	
            } else if (resultCode == RESULT_CANCELED) {
            	
            } else {
            	
            }
            
            if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {
                if (resultCode == RESULT_OK) {
                    // Video captured and saved to fileUri specified in the Intent
                    Toast.makeText(this, "Video saved to:\n" +
                             data.getData(), Toast.LENGTH_LONG).show();
                } else if (resultCode == RESULT_CANCELED) {
                    // User cancelled the video capture
                } else {
                    // Video capture failed, advise user
                }
            }
        }else if(requestCode == CHOISIR_PHOTO){
        	if (resultCode == RESULT_OK) {
                this.fileURI = data.getData();
                String path = getPath(fileURI);
                this.fileURI = Uri.parse(path);
                
                Bitmap bmp = BitmapFactory.decodeFile(path);
                ImageView tmp = new ImageView(this);
                
                tmp = (ImageView)findViewById(R.id.photo);
                tmp.setImageBitmap(bmp);
                
        		System.out.println(data.getData());
                }
        }
        }
    
    /**
     * Retourn le path
     * @param uri
     * @return
     */
    public String getPath(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }
    
    /**
     * Deconnexion de l'utilisateur
     */
    public void deconnexion()
    {
    	
    	// Envoi des donnees sur le serveur
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));
    	
		// Envoi de la requete post 
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/deco.php", postParameters);
		
		try {
			
			// Recuperation de la reponse
			String reponse = json.getString("reponse");
			
			if (!reponse.isEmpty()) 
			{
				// Pas d'erreur
				
				// Fin de l'user et de l'activite
				this.user = null;
		    	finish();
				
			}

		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		
    	
    }

}
