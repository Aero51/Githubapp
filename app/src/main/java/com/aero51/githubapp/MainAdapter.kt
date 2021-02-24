package com.aero51.githubapp

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.aero51.githubapp.databinding.RepoItemBinding
import com.aero51.githubapp.model.Repo
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions

class MainAdapter(private val listener: OnItemClickListener) : PagingDataAdapter<Repo, MainAdapter.RepoViewHolder>(REPO_COMPARATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = RepoItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class RepoViewHolder(private val binding: RepoItemBinding) : RecyclerView.ViewHolder(binding.root) {


        init {
            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        listener.onItemClick(item)
                    }
                }
            }
        }

        fun bind(repo: Repo) {
            binding.apply {
                Glide.with(itemView)
                        .load(repo.owner.avatarUrl)
                        .centerCrop()
                        .transition(DrawableTransitionOptions.withCrossFade())
                        .error(R.drawable.ic_error)
                        .into(authorImage)

                repoName.text = repo.name
                repoDescription.text = repo.description
                repoForks.text = repo.forks.toString()
                repoIssues.text = repo.openIssues.toString()
                repoStars.text = repo.stars.toString()
                repoLanguage.text = repo.language
                authorName.text = repo.owner.login


            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(repo: Repo)
    }

    companion object {
        private val REPO_COMPARATOR = object : DiffUtil.ItemCallback<Repo>() {
            override fun areItemsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Repo, newItem: Repo): Boolean {
                return oldItem == newItem
            }
        }
    }

}