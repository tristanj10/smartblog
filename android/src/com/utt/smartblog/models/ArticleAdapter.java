package com.utt.smartblog.models;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.utt.smartblog.R;


public class ArticleAdapter extends BaseAdapter 
{

	List<Article> articles;

	LayoutInflater inflater;

	public ArticleAdapter(Context context,List<Article> articles) 
	{
		inflater = LayoutInflater.from(context);
		this.articles = articles;
	}
	
	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return articles.size();
	}

	@Override
	public Object getItem(int position) 
	{
		// TODO Auto-generated method stub
		return articles.get(position);
	}

	@Override
	public long getItemId(int position) 
	{
		// TODO Auto-generated method stub
		return position;
	}
	
	private class ViewHolder
	{
		TextView itemArticleTitre;
		TextView itemArticleDate;
		TextView itemArticlePrenomNom;
		TextView itemArticleNumBar;
	}
	

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		// TODO Auto-generated method stub
		
		ViewHolder holder;

		if(convertView == null) 
		{
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_article, null);

			holder.itemArticleTitre = (TextView)convertView.findViewById(R.id.itemArticleTitre);
			holder.itemArticleDate = (TextView)convertView.findViewById(R.id.itemArticleDate);
			holder.itemArticlePrenomNom = (TextView)convertView.findViewById(R.id.itemArticlePrenomNom);
			holder.itemArticleNumBar = (TextView)convertView.findViewById(R.id.itemArticleNumBar);
			
			convertView.setTag(holder);
		} 
		else 
		{
			holder = (ViewHolder) convertView.getTag();
		}

		holder.itemArticleTitre.setText(articles.get(position).getTitre());
		holder.itemArticleDate.setText(articles.get(position).getDate());
		holder.itemArticlePrenomNom.setText("De " + articles.get(position).getAuteur().getPrenom() + " " + articles.get(position).getAuteur().getNom());
		holder.itemArticleNumBar.setText("Vu " + articles.get(position).getNb_vues() + " fois - " + articles.get(position).getLikes() + " Likes");
		
		return convertView;
	}

}
