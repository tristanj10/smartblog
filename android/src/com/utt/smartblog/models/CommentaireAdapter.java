package com.utt.smartblog.models;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.apache.commons.lang3.StringEscapeUtils;

import com.utt.smartblog.R;

/**
 * CommentaireAdapter pour afficher la liste des commentaires
 */
public class CommentaireAdapter extends BaseAdapter 
{

	List<Commentaire> commentaires; // Liste de commentaires

	LayoutInflater inflater;

	public CommentaireAdapter(Context context,List<Commentaire> commentaires) 
	{
		inflater = LayoutInflater.from(context);
		this.commentaires = commentaires;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return commentaires.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return commentaires.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}
	
	private class ViewHolder
	{
		// Items d'un commentaire
		
		TextView itemCommentaireContenu;
		TextView itemCommentaireDate;
		TextView itemCommentairePrenomNom;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		
		ViewHolder holder;

		if(convertView == null) 
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_commentaire, null);

			// Attribution des items
			holder.itemCommentaireContenu = (TextView)convertView.findViewById(R.id.itemCommentaireContenu);
			holder.itemCommentaireDate = (TextView)convertView.findViewById(R.id.itemCommentaireDate);
			holder.itemCommentairePrenomNom = (TextView)convertView.findViewById(R.id.itemCommentairePrenomNom);
			
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		// Texte des items
		holder.itemCommentaireContenu.setText(StringEscapeUtils.unescapeHtml4(commentaires.get(position).getContenu()));
		holder.itemCommentaireDate.setText(StringEscapeUtils.unescapeHtml4(commentaires.get(position).getDate()));
		holder.itemCommentairePrenomNom.setText("De " + StringEscapeUtils.unescapeHtml4(commentaires.get(position).getNom_auteur()));
		
		return convertView;
	}

}
