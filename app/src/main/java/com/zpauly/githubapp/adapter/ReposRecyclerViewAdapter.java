package com.zpauly.githubapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zpauly.githubapp.R;
import com.zpauly.githubapp.db.ReposModel;
import com.zpauly.githubapp.view.repositories.RepoContentActivity;
import com.zpauly.githubapp.view.viewholder.ReposViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpauly on 16-7-15.
 */

public class ReposRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter<ReposViewHolder> {
    private List<ReposModel> mData;

    public ReposRecyclerViewAdapter(Context context) {
        super(context);
        mData = new ArrayList<>();
    }

    public void addData(ReposModel data) {
        mData.add(data);
        notifyDataSetChanged();
    }

    public void addAllData(List<ReposModel> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void swapData(List<ReposModel> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public ReposViewHolder createContentViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_recyclerview_repositories, parent, false);
        ReposViewHolder holder = new ReposViewHolder(mView);
        return holder;
    }

    @Override
    public void bindContentViewHolder(ReposViewHolder holder, int position) {
        final ReposModel repo = mData.get(position);
        holder.mReposForksTV.setText(String.valueOf(repo.getForks_count()));
        holder.mReposStarsTV.setText(String.valueOf(repo.getStargazers_count()));
        holder.mReposTechLanguageTV.setText(repo.getLanguage());
        holder.mReposTitleTV.setText(repo.getFull_name());
        holder.mReposUpdateTimeTV.setText(repo.getUpdated_at());
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(RepoContentActivity.REPO, repo);
                intent.putExtra(RepoContentActivity.REPO, bundle);
                intent.setClass(mContext, RepoContentActivity.class);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mData.size() + 1;
    }
}
