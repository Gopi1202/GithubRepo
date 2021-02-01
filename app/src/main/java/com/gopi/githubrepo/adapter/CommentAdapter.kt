package com.gopi.githubrepo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gopi.githubrepo.R
import java.util.*

class CommentAdapter (val context: Context, private val commentList: ArrayList<String>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
            return CommentViewHolder(
                LayoutInflater.from(context).inflate(R.layout.item_comment, parent, false)
            )
        }

        override fun getItemCount(): Int {
            return commentList.size
        }

        override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {

            val comment = commentList[position]
            holder.tvComment.text = comment
        }

        class CommentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            var tvComment: TextView = itemView.findViewById(R.id.tvComment)
        }
}