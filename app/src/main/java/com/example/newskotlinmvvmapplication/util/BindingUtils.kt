package com.example.newskotlinmvvmapplication.util

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.newskotlinmvvmapplication.R
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*


@BindingAdapter("image")
fun loadImage(view: ImageView,url: String) {
    if(!url.equals("")) {
        Glide.with(view)
            .load(url)
            .centerCrop()
            .into(view)
    }
    else{
        Glide.with(view)
            .load(R.mipmap.ic_news_placeholder)
            .centerCrop()
            .into(view)
    }
}

@BindingAdapter("dateformat")
fun bindServerDate(textView: TextView, dateString: String?) {

    val dateFormat: DateFormat =
        SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    var date: Date? = null
    try {
        date = dateFormat.parse(
            dateString
        ) //You will get date object relative to server/client timezone wherever it is parsed
    } catch (e: ParseException) {
        e.printStackTrace()
    }
    val formatter: DateFormat = SimpleDateFormat("yyyy-MM-dd")
    textView.setText(formatter.format(date))
}
