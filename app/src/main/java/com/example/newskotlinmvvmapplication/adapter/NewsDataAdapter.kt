package com.example.newskotlinmvvmapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.newskotlinmvvmapplication.R
import com.example.newskotlinmvvmapplication.databinding.RowLayoutBinding
import com.example.newskotlinmvvmapplication.model.NewsDataResponseClass

class NewsDataAdapter (
    val context: Context,
    var newsResponseArrayList: List<NewsDataResponseClass>,
    val mOnCLickListener:onCLickListener
): RecyclerView.Adapter<NewsDataAdapter.ViewHolder>() {

    fun updateData(newsDataResponseArrayList:
                   List<NewsDataResponseClass>) {
        newsResponseArrayList = newsDataResponseArrayList
        notifyDataSetChanged()
    }

    interface onCLickListener {
        fun onCLick(view: View?, position: Int)
    }

    class ViewHolder(val rowLayoutBinding: RowLayoutBinding) :RecyclerView.ViewHolder(rowLayoutBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder( DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.row_layout,
            parent,
            false
        ))

    override fun getItemCount(): Int = newsResponseArrayList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.rowLayoutBinding.news = newsResponseArrayList[position]

    }
}