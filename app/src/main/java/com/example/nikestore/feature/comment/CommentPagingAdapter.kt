package com.example.nikestore.feature.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.nikestore.R
import com.example.nikestore.data.Comment
import com.example.nikestore.data.source.comment.State

class CommentPagingAdapter :
    PagedListAdapter<Comment, CommentPagingAdapter.CommentViewHolder>(CommentDiffCallBack) {


   inner class CommentViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTv = itemView.findViewById<TextView>(R.id.commentTitleTv)
        val dateTv = itemView.findViewById<TextView>(R.id.commentDateTv)
        val authorTv = itemView.findViewById<TextView>(R.id.commentAuthorTv)
        val contentTv = itemView.findViewById<TextView>(R.id.commentContentTv)
        fun bind(comment: Comment?) {
            comment?.let {
                titleTv.text = comment.title
                dateTv.text = comment.date
                authorTv.text = comment.author.email
                contentTv.text = comment.content
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return CommentViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_comment, parent, false)
        )
    }


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    companion object {
        val CommentDiffCallBack = object : DiffUtil.ItemCallback<Comment>() {
            override fun areItemsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Comment, newItem: Comment): Boolean {
                return oldItem == newItem
            }

        }
    }
}