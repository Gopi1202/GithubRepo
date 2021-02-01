package com.gopi.githubrepo.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.gopi.githubrepo.R
import com.gopi.githubrepo.RepoClickListener
import com.gopi.githubrepo.model.GithubModel
import java.util.*

class RepoAdapter(val context: Context, private val repoList: ArrayList<GithubModel>, private var repoClickListener: RepoClickListener) :
    RecyclerView.Adapter<RepoAdapter.RepoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        return RepoViewHolder(
            LayoutInflater.from(context).inflate(R.layout.item_repo, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return repoList.size
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {

        val title = repoList[position].description
        val createdBy = repoList[position].owner.login
        val avatarUrl = repoList[position].owner.avatarUrl

        if (title != null) {
            holder.tvTitle.text = title
        }

        if (createdBy != null) {
            holder.tvCreatedBy.text = createdBy
        }

        Glide.with(context)
            .load(avatarUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
            /*.placeholder(R.drawable.ic_movie_black_18dp)
            .error(R.drawable.ic_movie_black_18dp)*/
            .into(holder.ivThumbnail)

        holder.itemView.setOnClickListener {
            repoClickListener.repoClicked(holder.adapterPosition)
        }
    }

    class RepoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvCreatedBy: TextView = itemView.findViewById(R.id.tvCreatedBy)
        val ivThumbnail: ImageView = itemView.findViewById(R.id.ivThumbnail)
    }
}