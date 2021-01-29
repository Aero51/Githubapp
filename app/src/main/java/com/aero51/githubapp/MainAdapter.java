package com.aero51.githubapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aero51.githubapp.db.model.Repo;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.aero51.githubapp.utils.Constants.LOG;

/**
 * Adapter for the list of repositories.
 *
 * @author Nikola Srdoc
 */
public class MainAdapter extends PagedListAdapter<Repo, MainAdapter.RepoViewHolder> {
    private Context context;
    /**
     * DiffUtil to compare the Repo data (old and new)
     * for issuing notify commands suitably to update the list
     */
    private static DiffUtil.ItemCallback<Repo> REPO_COMPARATOR
            = new DiffUtil.ItemCallback<Repo>() {
        @Override
        public boolean areItemsTheSame(Repo oldItem, Repo newItem) {
            return oldItem.fullName.equals(newItem.fullName);
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(Repo oldItem, Repo newItem) {
            return oldItem.equals(newItem);
        }
    };

    public MainAdapter(Context context) {
        super(REPO_COMPARATOR);
        this.context = context;
    }

    @NonNull
    @Override
    public RepoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.repo_item, parent, false);
        RepoViewHolder repoViewHolder = new RepoViewHolder(view);
        return repoViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RepoViewHolder holder, int position) {
        Log.d(LOG, "login: position:  "+position +" , "+ getItem(position).owner.avatarUrl);
        holder.bind(getItem(position));
    }

    /**
     * View Holder for a {@link Repo} RecyclerView list item.
     */
    public class RepoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView textViewRepoName;
        private TextView textViewRepoDescription;
        private TextView textViewRepoLanguage;
        private TextView textViewRepoStars;
        private TextView textViewRepoForks;
        private TextView textViewAuthorName;
        private TextView textViewRepoIssues;
        private CircleImageView imageViewAuthorAvatar;

        public RepoViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            textViewRepoName = itemView.findViewById(R.id.repo_name);
            textViewRepoDescription = itemView.findViewById(R.id.repo_description);
            textViewRepoLanguage = itemView.findViewById(R.id.repo_language);
            textViewRepoStars = itemView.findViewById(R.id.repo_stars);
            textViewRepoForks = itemView.findViewById(R.id.repo_forks);
            textViewAuthorName=itemView.findViewById(R.id.author_name);
            textViewRepoIssues=itemView.findViewById(R.id.repo_issues);
            imageViewAuthorAvatar=itemView.findViewById(R.id.author_image);
        }

        public void bind(Repo repo) {
            if (repo == null) {
                Resources resources = context.getResources();
                resources.getString(R.string.loading);
                textViewRepoName.setText(resources.getString(R.string.loading));
                textViewRepoDescription.setVisibility(View.GONE);
                textViewRepoLanguage.setVisibility(View.GONE);
                textViewRepoStars.setText(resources.getString(R.string.unknown));
                textViewRepoForks.setText(resources.getString(R.string.unknown));

            } else {
                textViewRepoName.setText(repo.name);
                textViewRepoDescription.setVisibility(View.VISIBLE);
                textViewRepoDescription.setText(repo.description);
                textViewRepoLanguage.setVisibility(View.VISIBLE);
                textViewRepoLanguage.setText(repo.language);
                textViewRepoStars.setText(repo.stars+"");
                textViewRepoForks.setText(repo.forks+"");

                textViewAuthorName.setText(repo.owner.login);
                textViewRepoIssues.setText(repo.openIssues+"");
                Picasso.get().load(repo.owner.avatarUrl).into(imageViewAuthorAvatar);

            }
            }

            /**
             * Called when a view has been clicked.
             *
             * @param view The view that was clicked.
             */
            @Override
            public void onClick (View view){
                if (getAdapterPosition() > RecyclerView.NO_POSITION) {
                    Repo repo = getItem(getAdapterPosition());
                    if (repo != null && !TextUtils.isEmpty(repo.url)) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(repo.url));
                        view.getContext().startActivity(intent);
                    }
                }
            }
        }
    }
