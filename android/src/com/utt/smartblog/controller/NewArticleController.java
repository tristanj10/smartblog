package com.utt.smartblog.controller;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

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
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
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

import com.utt.smartblog.LoggedInActivity;
import com.utt.smartblog.R;
import com.utt.smartblog.models.Article;
import com.utt.smartblog.models.ArticleAdapter;
import com.utt.smartblog.models.Utilisateur;
import com.utt.smartblog.network.CameraSaveFile;
import com.utt.smartblog.network.JSONParser;


public class NewArticleController extends Fragment implements OnClickListener
{
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private Uri fileUri;
	private Button articleButton = null;
	private Utilisateur user = null;
	private LoggedInActivity monActivity = null;
	private ImageButton prendrePhoto = null;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_new_article, container, false);
	    
	    articleButton = (Button)view.findViewById(R.id.articleButton);
	    monActivity = (LoggedInActivity) this.getActivity();
	    prendrePhoto = (ImageButton)view.findViewById(R.id.prendre_photo);
	    
	    this.user = ((LoggedInActivity)getActivity()).user;
	    
	    articleButton.setOnClickListener(this);
	    this.prendrePhoto.setOnClickListener(this);
	    
	    return view;
	}
	
    @Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
    	
    	
		// Envoi des données
		if(v == articleButton) 
		{
			
			EditText titre = (EditText) getView().findViewById(R.id.titreField);
			EditText contenu = (EditText) getView().findViewById(R.id.contenuField);
			
			String strTitre = titre.getText().toString();
			String strContenu = contenu.getText().toString();
			
			if(nouvelArticle(strTitre, strContenu))
			{
				monActivity.showFragment(monActivity.articleFragment);
			}
			
			
			
		}else if(v == prendrePhoto){
			
			 	
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

			    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
			    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
			    //intent.putExtra("test", fileUri);
			    
			    // start the image capture Intent
			    this.monActivity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			
		}
		
	}
    
    
    public boolean nouvelArticle(String titre, String contenu) {
		// Envoi des données sur la base

    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
		postParameters.add(new BasicNameValuePair("titre", titre));
		postParameters.add(new BasicNameValuePair("contenu", contenu));
		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));

		// getting JSON string from URL
		// JSONObject json =
		// JSONParser.getJSONFromUrl("https://10.0.2.2/auth.php?login="+user.getLogin()+"&password="+user.getPassword());
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/new_article.php", postParameters);
		try {
			// Storing each json item in variable
			String id = json.getString("id");
			String error = json.getString("error");
			String token = json.getString("token");
			
			if (!id.isEmpty()) 
			{
				// Pas d'erreur
				Toast.makeText(getActivity(), "C'est bon ! " + id + " " + token, Toast.LENGTH_LONG).show();
				return true;
				
			} else 
			{
				// Erreur(s)
				Toast.makeText(getActivity(), "Erreur : " + error , Toast.LENGTH_LONG).show();
				
			}

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return false;
	}
    
    private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}

	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    // This location works best if you want the created images to be shared
	    // between applications and persist after your app has been uninstalled.

	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("MyCameraApp", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else if(type == MEDIA_TYPE_VIDEO) {
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "VID_"+ timeStamp + ".mp4");
	    } else {
	        return null;
	    }
	    return mediaFile;
	}

}
