package com.utt.smartblog.models;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utt.smartblog.R;


public class CommentaireAdapter extends BaseAdapter 
{

	List<Commentaire> commentaires;

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

			holder.itemCommentaireContenu = (TextView)convertView.findViewById(R.id.itemCommentaireContenu);
			holder.itemCommentaireDate = (TextView)convertView.findViewById(R.id.itemCommentaireDate);
			holder.itemCommentairePrenomNom = (TextView)convertView.findViewById(R.id.itemCommentairePrenomNom);
			
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.itemCommentaireContenu.setText(commentaires.get(position).getContenu());
		holder.itemCommentaireDate.setText(commentaires.get(position).getDate());
		holder.itemCommentairePrenomNom.setText("De " + commentaires.get(position).getId_auteur());
		
		return convertView;
	}

}
