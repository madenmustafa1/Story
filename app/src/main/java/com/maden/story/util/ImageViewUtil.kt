package com.maden.story.util

import android.widget.ImageView
import androidx.core.view.isGone
import androidx.databinding.BindingAdapter
import com.maden.story.R
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

/*
@BindingAdapter("android:likeButton")
fun ImageView.lFULL(view: ImageView, likeControl: String?) {
    view.setImageResource(R.drawable.ic_like_full)



}
                android:src="@{showData.userLikeControl ? @drawable/likeiconfull :@drawable/likeicon }"

 */
fun ImageView.downloadPhoto(url: String) {

    Picasso.get()
        .load(url)
        .placeholder(R.drawable.ic_profile_place_holder)
        .error(R.drawable.ic_profile_place_holder)
        .into(this)
}

@BindingAdapter("android:downloadUrl")
fun downloadImage(view: ImageView, url: String){
    if(url != "" && url != null){
        view.downloadPhoto(url)
    }
}


