package com.zpauly.githubapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.zpauly.githubapp.R;
import com.zpauly.githubapp.db.ReposDao;
import com.zpauly.githubapp.db.ReposModel;
import com.zpauly.githubapp.entity.response.RepositoriesBean;
import com.zpauly.githubapp.entity.response.events.EventsBean;
import com.zpauly.githubapp.entity.response.events.Payload;
import com.zpauly.githubapp.view.repositories.RepoContentActivity;
import com.zpauly.githubapp.view.viewholder.EventsViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zpauly on 16-6-14.
 */
public class EventsRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter<EventsViewHolder> {
    private final String TAG = getClass().getName();
    private List<EventsBean> mData;

    private EventsCommitsRecyclerViewAdapter mAdapter;

    public EventsRecyclerViewAdapter(Context context) {
        super(context);
        mData = new ArrayList<>();
    }

    public void addAllData(List<EventsBean> list) {
        mData.addAll(list);
        notifyDataSetChanged();
    }

    public void swapData(List<EventsBean> list) {
        mData.clear();
        mData.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public EventsViewHolder createContentViewHolder(ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(mContext).inflate(R.layout.item_recylcleview_events, parent, false);
        EventsViewHolder holder = new EventsViewHolder(mView);
        return holder;
    }

    @Override
    public void bindContentViewHolder(EventsViewHolder holder, int position) {
        final EventsBean data = mData.get(position);
        Glide.with(mContext)
                .load(data.getActor().getAvatar_url())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .crossFade()
                .into(holder.mUserAvatarIV);
        holder.mRepoTV.setText(data.getRepo().getName());
        holder.mUsernameTV.setText(data.getActor().getLogin());
//        holder.mUsernameTV.setText(String.valueOf(position));
        holder.mTimeTV.setText(data.getCreated_at());
        holder.mCommentTV.setVisibility(View.GONE);
        mAdapter = new EventsCommitsRecyclerViewAdapter(mContext);
        holder.mCommitsRV.setLayoutManager(new LinearLayoutManager(mContext));
        holder.mCommitsRV.setAdapter(mAdapter);
        setAction(data.getType(), data.getPayload(), holder, position);

        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ReposModel model = ReposDao.queryRepos("full_name", data.getPayload().getRepository().getFull_name()).get(0);
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putParcelable(RepoContentActivity.REPO, model);
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

    private void setAction(String type, Payload payloadBean, EventsViewHolder holder, int position) {
        if ("WatchEvent".equals(type)) {
            holder.mActionTV.setText("starred ");
            holder.mTypeIV.setImageResource(R.mipmap.ic_star);
        } else if ("CreateEvent".equals(type)) {
            if (payloadBean.getRef_type().equals("repository")) {
                holder.mActionTV.setText("create " + payloadBean.getRef_type());
                holder.mTypeIV.setImageResource(R.mipmap.ic_repos);
            } else if (payloadBean.getRef_type().equals("branch")) {
                holder.mActionTV.setText("create branch " + payloadBean.getMaster_branch() + " at");
                holder.mTypeIV.setImageResource(R.mipmap.ic_fork);
            } else if (payloadBean.getRef_type().equals("tag")) {
                holder.mActionTV.setText("create tag " + payloadBean.getRef() + " at");
            }
        } else if ("PushEvent".equals(type)) {
            String[] str = payloadBean.getRef().split("/");
            String branch = str[str.length - 1];
            holder.mActionTV.setText("pushed to " + branch + " at ");
            holder.mTypeIV.setImageResource(R.mipmap.ic_commit);
            mAdapter.swapData(payloadBean.getCommits());
        } else if ("ForkEvent".equals(type)) {
            holder.mActionTV.setText("forked");
            holder.mTypeIV.setImageResource(R.mipmap.ic_fork);
        } else if ("ReleaseEvent".equals(type)) {
            holder.mActionTV.setText("release " + payloadBean.getRelease().getName() + " at ");
        } else if ("PullRequestEvent".equals(type)) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_pull_request);
            holder.mActionTV.setText(payloadBean.getAction() + " pull request "
                    + payloadBean.getPull_request().getNumber() + " at ");
        } else if ("CommentCommitEvent".equals(type)) {
            holder.mActionTV.setText("comment on ");
            holder.mTypeIV.setImageResource(R.mipmap.ic_comment);
            List<Payload.CommitsBean> list = new ArrayList<>();
            Payload.CommitsBean bean = new Payload.CommitsBean();
            bean.setSha(payloadBean.getComment().getCommit_id());
            bean.setMessage(payloadBean.getComment().getBody());
            list.add(bean);
            mAdapter.swapData(list);
        } else if ("MemberEvent".equals(type)) {
            holder.mActionTV.setText(payloadBean.getAction() + " " + payloadBean.getMember().getLogin() + "to ");
        } else if ("IssueCommentEvent".equals(type)) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_comment);
            holder.mActionTV.setText("comment on issue " + payloadBean.getIssue().getNumber() + " at ");
            holder.mCommentTV.setVisibility(View.VISIBLE);
            holder.mCommentTV.setText(payloadBean.getComment().getBody());
        } else if ("DeleteEvent".equals(type)) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_delete);
            holder.mActionTV.setText("delete " + payloadBean.getRef_type() + " " + payloadBean.getRef() + " at");
        } else if ("IssuesEvent".equals(type)) {
            if (payloadBean.getAction().equals("opened")) {
                holder.mActionTV.setText("opened issue " + payloadBean.getIssue().getNumber() + " at");
                holder.mTypeIV.setImageResource(R.mipmap.ic_issue_opened);
            } else if (payloadBean.getAction().equals("closed")) {
                holder.mActionTV.setText("closed issue " + payloadBean.getIssue().getNumber() + " at");
                holder.mTypeIV.setImageResource(R.mipmap.ic_issue_closed);
            } else if (payloadBean.getAction().equals("reopened")) {
                holder.mActionTV.setText("reopened issue " + payloadBean.getIssue().getNumber() + " at");
                holder.mTypeIV.setImageResource(R.mipmap.ic_issue_reopened);
            }
            holder.mCommentTV.setVisibility(View.VISIBLE);
            holder.mCommentTV.setText(payloadBean.getIssue().getBody());
        } else if ("PublicEvent".equals(type)) {
            holder.mTypeIV.setImageResource(R.mipmap.ic_repos);
            holder.mActionTV.setText("made the repository public");
        } else {
            Log.i(TAG, type + position);
        }
    }
}
