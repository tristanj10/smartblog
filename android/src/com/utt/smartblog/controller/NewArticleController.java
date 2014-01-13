package com.utt.smartblog.controller;

import java.io.File;
import java.io.IOException;
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
import com.utt.smartblog.network.UploadFiles;


public class NewArticleController extends Fragment implements OnClickListener
{
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	public static final int MEDIA_TYPE_VIDEO = 2;
	private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
	private static final int CHOISIR_PHOTO = 300;
	
	private Uri fileUri = null;
	private Button articleButton = null;
	private Utilisateur user = null;
	private LoggedInActivity monActivity = null;
	private ImageButton prendrePhoto = null;
	private ImageButton choisirPhoto = null;
	
	private EditText titreField = null;
	private EditText contenuField = null;
	
    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
	    View view = inflater.inflate(R.layout.fragment_new_article, container, false);
	    
	    //On initialise les éléments graphiques
	    articleButton = (Button)view.findViewById(R.id.articleButton);
	    monActivity = (LoggedInActivity) this.getActivity();
	    prendrePhoto = (ImageButton)view.findViewById(R.id.prendre_photo);
	    choisirPhoto = (ImageButton)view.findViewById(R.id.choisir_photo);
	    titreField = (EditText) view.findViewById(R.id.titreField);
	    contenuField = (EditText) view.findViewById(R.id.contenuField);
	    
	    this.user = ((LoggedInActivity)getActivity()).user;
	    
	    //Les listeners
	    articleButton.setOnClickListener(this);
	    this.prendrePhoto.setOnClickListener(this);
	    this.choisirPhoto.setOnClickListener(this);
	    
	    return view;
	}
	
    @Override
	public void onClick(View v) 
	{
		// TODO Auto-generated method stub
    	
		if(v == articleButton) 
		{
			// Nouvel article
			
			EditText titre = (EditText) getView().findViewById(R.id.titreField);
			EditText contenu = (EditText) getView().findViewById(R.id.contenuField);
			
			String strTitre = titre.getText().toString();
			String strContenu = contenu.getText().toString();
			
			if(nouvelArticle(strTitre, strContenu))
			{
				// Retour au fragment article si article cree
				monActivity.showFragment(monActivity.articleFragment);
			}
			
			
			
		}
		else if(v == prendrePhoto)
		{
			
		 	// Prise de photo
			Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

		    fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE); // create a file to save the image
		    intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file name
		    this.monActivity.fileURI = fileUri;
		    
		    this.monActivity.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			
		}
		else if(v == choisirPhoto)
		{
			// Parcours dans la galerie pour trouver une photo
			Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
			this.monActivity.startActivityForResult(intent, CHOISIR_PHOTO);
		}
		
	}
    
    /**
     * Creation d'un nouvel article
     * @param titre
     * @param contenu
     * @return boolean si article cree ou non
     */
    public boolean nouvelArticle(String titre, String contenu)
    {
		
    	if(this.monActivity.fileURI != null)
    	{
	    	//Upload de l'image vers le serveur
	        UploadFiles upload = new UploadFiles();
	       
	        try {
				upload.upload(this.monActivity.fileURI.getPath());
			} 
	        catch (IOException e) 
	        {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	}
    	
    	// Envoi des donnees sur le serveur
    	ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
    	
    	// post
		postParameters.add(new BasicNameValuePair("titre", titre));
		postParameters.add(new BasicNameValuePair("contenu", contenu));
		if(this.monActivity.fileURI != null)
		{
			postParameters.add(new BasicNameValuePair("image", this.monActivity.fileURI.getLastPathSegment().toString()));
			System.out.println(this.monActivity.fileURI.getLastPathSegment().toString());
		}
		postParameters.add(new BasicNameValuePair("token", this.user.getToken()));
		

		// Envoi de la requete post (donnees de l'article)
		JSONObject json = JSONParser.getJSONFromUrl("https://10.0.2.2/new_article.php", postParameters);
		try {
			
			// Recuperation de la reponse
			String id = json.getString("id");
			String error = json.getString("error");
			String token = json.getString("token");
			
			if (!id.isEmpty()) 
			{
				// Pas d'erreur
				titreField.setText("");
				contenuField.setText("");
				
				return true;
				
			} else 
			{
				// Erreur(s)
				Toast.makeText(getActivity(), "Erreur : " + error , Toast.LENGTH_LONG).show();
				
			}

		} 
		catch (JSONException e)
		{
			e.printStackTrace();
		}
		return false;
	}
    
    /**
     * Sauvegarde de la photo
     * @param int type
     * @return
     */
    private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}


    /**
     * Creation d'un fichier pour la sauvegarde de la photo
     * @param int type
     * @return
     */
	private static File getOutputMediaFile(int type){
	    

	    File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
	              Environment.DIRECTORY_PICTURES), "MyCameraApp");
	    

	    // Créer un répertoire s'il n'existe pas
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            return null;
	        }
	    }

	    // Create un nom de fichier
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }
	    return mediaFile;
	}

}
