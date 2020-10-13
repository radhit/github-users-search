package com.example.riskaadhita.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.getColor
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.riskaadhita.R
import com.example.riskaadhita.data.Item
import kotlinx.android.synthetic.main.item_users.view.*

class UsersAdapter(private val mContext: Context) : RecyclerView.Adapter<UsersAdapter.ViewHolder>(){
    var lists: ArrayList<Item> = arrayListOf()
    var requestOptions = RequestOptions().placeholder(R.drawable.loading_github).error(R.drawable.img_not_found).diskCacheStrategy(
        DiskCacheStrategy.AUTOMATIC).skipMemoryCache(true)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_users,parent,false))

    override fun getItemCount(): Int = lists.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val value = lists[position]
        holder.view.tv_name.text = value.login
        Glide.with(mContext).applyDefaultRequestOptions(requestOptions).load(value.avatar_url).into(holder.view.iv_avatar)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view){
        var data: Item? = null
        set(value){field = value}
    }
}