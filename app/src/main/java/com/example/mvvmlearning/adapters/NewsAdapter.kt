package com.example.mvvmlearning.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mvvmlearning.R
import com.example.mvvmlearning.models.Article
import org.w3c.dom.Text

class NewsAdapter:RecyclerView.Adapter<NewsAdapter.RecyclerViewHolder>() { //We use diffUtil because it finds the difference between the two lists and only changes the item that was updated that too on background thread
    inner class RecyclerViewHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

    }

     val differCallback=object: DiffUtil.ItemCallback<Article>(){ //Function to compare the two lists
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url==newItem.url //Could have used id but because we have data from API as well and it doesn't have id , so we use url to compare

        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem==newItem
        }
    }
      val differ=AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewHolder {
        return RecyclerViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        )
    }

    override fun getItemCount(): Int {
        return  differ.currentList.size
    }

    override fun onBindViewHolder(holder: RecyclerViewHolder, position: Int) {
        val article=differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(article.urlToImage).into(findViewById(R.id.ivArticleImage))
            findViewById<TextView>(R.id.tvSource).text=article.source.name
            findViewById<TextView>(R.id.tvTitle).text=article.title
            findViewById<TextView>(R.id.tvDescription).text=article.description
            findViewById<TextView>(R.id.tvPublishedAt).text=article.publishedAt
            setOnClickListener{
                onItemClickListener?.let{it(article)}
            }
        }
    }

    private var onItemClickListener:((Article)->Unit)?=null
    fun setOnItemClickListener(listener:(Article)->Unit){
        onItemClickListener=listener
    }
}