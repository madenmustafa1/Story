package com.maden.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.maden.story.R
import com.maden.story.databinding.ItemPostCommentBinding
import com.maden.story.model.CommentData

class CommentAdapter(private val commentList: ArrayList<CommentData>)
    :RecyclerView.Adapter<CommentAdapter.ViewHolder>(){

    class ViewHolder(var view: ItemPostCommentBinding):
        RecyclerView.ViewHolder(view.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemPostCommentBinding>(
            inflater,
            R.layout.item_post_comment,
            parent, false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.view.commentData = commentList[position]

    }

    override fun getItemCount(): Int {
        return commentList.size
    }
}