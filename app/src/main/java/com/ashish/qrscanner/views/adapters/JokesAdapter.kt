package com.ashish.qrscanner.views.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ashish.qrscanner.R
import com.ashish.qrscanner.api.Jokes

class JokesAdapter(private val mContext: Context, var jokesPlayListener: JokesAdapter.JokesPlayButtonListener): RecyclerView.Adapter<JokesAdapter.JokesViewHolder>() {

    private var jokesList: List<Jokes>? = null
    var content = ""


    class JokesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val jokesTv: TextView = itemView.findViewById(R.id.jokes_tv)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JokesAdapter.JokesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_jokes, parent, false)
        return JokesAdapter.JokesViewHolder(view)
    }

    override fun onBindViewHolder(holder: JokesViewHolder, position: Int) {

        val jokesData = jokesList?.get(holder.adapterPosition)
        if (jokesData?.setup != null) {
            holder.jokesTv.text = (jokesData?.setup +" \n"+ jokesData?.delivery)
            content += "${jokesData?.setup}  ${jokesData?.delivery}  "+"Ha Ha Ha Ha Ha"
            jokesPlayListener.onJokesPlay(content)
        } else {
            holder.jokesTv.visibility = View.GONE
        }

    }

    override fun getItemCount(): Int {
        return jokesList?.size ?: -1
    }

    fun setList(list: List<Jokes>){
        jokesList =list
        content = ""
        notifyDataSetChanged()
    }
    interface JokesPlayButtonListener{
        fun onJokesPlay(string: String)
    }
}