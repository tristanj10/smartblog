package com.utt.smartblog.controller;



import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.utt.smartblog.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginController extends Fragment implements OnClickListener 
{

	Button buttonSend = null;
	TextView login;
	TextView password;
	
	 @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	 
        View view = inflater.inflate(R.layout.login, container, false);
        
        buttonSend = (Button)view.findViewById(R.id.buttonSend);
        buttonSend.setOnClickListener(this);

        return view;
    }

	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
		// Envoi des données de connexion 
		if(v == buttonSend) {
			login = (EditText) getView().findViewById(R.id.editText1);
			password = (EditText) getView().findViewById(R.id.editText2);
			
			String l = login.getText().toString();
			String p = login.getText().toString();
			
			//Toast.makeText(getActivity(), l, Toast.LENGTH_SHORT).show();
			
			// HTTPS 
			System.out.println(connexion(l,p));

		}
	}
	
	public HttpResponse connexion(String login, String password) {
		HttpResponse response = null;
		try {        
		        HttpClient client = new DefaultHttpClient();
		        HttpGet request = new HttpGet();
		        request.setURI(new URI("https://smartblog.local/auth.php?login=" + login + "&password=" + password));
		        response = client.execute(request);
		    } catch (URISyntaxException e) {
		        e.printStackTrace();
		    } catch (ClientProtocolException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    } catch (IOException e) {
		        // TODO Auto-generated catch block
		        e.printStackTrace();
		    }   
		 return response;
	}


}
