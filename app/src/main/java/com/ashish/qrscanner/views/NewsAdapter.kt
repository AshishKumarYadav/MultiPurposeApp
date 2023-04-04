package com.ashish.qrscanner.views

import android.content.Context
import android.content.Intent
import android.content.Intent.ACTION_VIEW
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashish.qrscanner.R
import com.ashish.qrscanner.api.Article
import com.squareup.picasso.Picasso
import java.time.Duration
import java.time.Instant
import java.time.ZoneId

class NewsAdapter(private val context: Context,var itemClickListener: onItemClickListener):
    RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {
    private var newsList: List<Article>? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun getItemCount(): Int {
        Log.i("MSG_ ", "NewsAdapter Size ${newsList?.size}")
        return newsList?.size ?: -1
    }
    fun setList(list: List<Article>){
        newsList =list
        notifyDataSetChanged()
        Log.i("MSG_ ", "NewsAdapter $newsList")
    }
    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {

        val newsData = newsList?.get(holder.adapterPosition)

        holder.headLine.text = newsData?.title
        val time: String? = newsData?.publishedAt
        val imgUrl = newsData?.urlToImage

        if (imgUrl.isNullOrEmpty()) {
            Picasso.get()
                .load( R.drawable.img)
                .fit()
                .centerCrop()
                .into(holder.image)
        } else {
            Picasso.get()
                .load(imgUrl)
                .fit()
                .centerCrop()
                .error(R.drawable.img)
                .into(holder.image)
        }
        val currentTimeInHours = Instant.now().atZone(ZoneId.of("Asia/Kolkata"))
        val newsTimeInHours = Instant.parse(time).atZone(ZoneId.of("Asia/Kolkata"))
        val hoursDifference = Duration.between(currentTimeInHours, newsTimeInHours)
        val hoursAgo = " " + hoursDifference.toHours().toString().substring(1) + " hour ago"
        holder.newsPublicationTime.text = hoursAgo

        holder.mainView.setOnClickListener {

            if (itemClickListener!=null) {
                itemClickListener.onItemClick(newsData?.url!!)
            } else {
                val intent = Intent(ACTION_VIEW, Uri.parse(newsData?.url))
                context.startActivity(intent)
            }

        }


    }

    interface onItemClickListener{
        fun onItemClick(url:String)
    }

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val image: ImageView = itemView.findViewById(R.id.img)
        val headLine: TextView = itemView.findViewById(R.id.news_title)
        val newsPublicationTime: TextView = itemView.findViewById(R.id.news_publication_time)
        val mainView: RelativeLayout = itemView.findViewById(R.id.mainView)

    }
}