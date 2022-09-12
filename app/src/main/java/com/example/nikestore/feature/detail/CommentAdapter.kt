package com.example.nikestore.feature.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Comment

class CommentAdapter(val showAll: Boolean = false) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    var comments: ArrayList<Comment> = ArrayList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleTv = itemView.findViewById<TextView>(R.id.commentTitleTv)
        val dateTv = itemView.findViewById<TextView>(R.id.commentDateTv)
        val authorTv = itemView.findViewById<TextView>(R.id.commentAuthorTv)
        val contentTv = itemView.findViewById<TextView>(R.id.commentContentTv)
        fun bind(comment: Comment) {
            titleTv.text = comment.title
            dateTv.text = comment.date
            authorTv.text = comment.author.email
            contentTv.text = comment.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(comments[position])
    }

    override fun getItemCount(): Int {
        return if (comments.size > 3 && !showAll) 3 else comments.size
    }
}