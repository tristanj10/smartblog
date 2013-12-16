package com.utt.smartblog;

import java.io.ByteArrayOutputStream;

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
import backup._LoginController;

import com.utt.smartblog.controller.ArticleController;
import com.utt.smartblog.controller.LectureArticleController;
import com.utt.smartblog.controller.NewArticleController;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.Utilisateur;

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
	
	private ImageView photo = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_logged_in);
		
		this.photo = (ImageView)findViewById(R.id.photo);
		
		//User vierge
		user = new Utilisateur();
		
		if(this.getIntent().getExtras() != null)
		{
			// Passage du token
    	    user.setToken(this.getIntent().getExtras().getString("token"));
    	    Toast.makeText(this, "Ca y est, on est co token : " + user.getToken(), Toast.LENGTH_LONG).show();//a suppr
    	    
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
                Toast.makeText(this, "Image sauvée" + data.getData(), Toast.LENGTH_LONG).show();
                
               /*   Uri selectedImage = (Uri) data.getData();
                System.out.println(selectedImage.toString());
                  String[] filePathColumn = {MediaStore.Images.Media.DATA};
                
                Cursor cursor = getContentResolver().query(
                                   selectedImage, filePathColumn, null, null, null);
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String filePath = cursor.getString(columnIndex);
                cursor.close();


                Bitmap yourSelectedImage = BitmapFactory.decodeFile(filePath);
                
                this.photo.setImageBitmap(yourSelectedImage);*/
            	
            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the image capture
            	Toast.makeText(this, "Image saved 2 to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
            } else {
                // Image capture failed, advise user
            	Toast.makeText(this, "Image saved 3 to:\n" +
                        data.getData(), Toast.LENGTH_LONG).show();
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
        }
        }
    
    

}
