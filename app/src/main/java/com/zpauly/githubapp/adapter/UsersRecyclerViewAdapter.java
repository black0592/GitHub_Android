package com.zpauly.githubapp.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.zpauly.githubapp.R;
import com.zpauly.githubapp.entity.response.UserBean;
import com.zpauly.githubapp.utils.ImageUtil;
import com.zpauly.githubapp.view.profile.OthersActivity;
import com.zpauly.githubapp.view.viewholder.UsersViewHolder;

/**
 * Created by zpauly on 16-6-15.
 */
public class UsersRecyclerViewAdapter extends LoadMoreRecyclerViewAdapter<UserBean, UsersViewHolder> {
    private final String TAG = getClass().getName();

    private View mView;

    public UsersRecyclerViewAdapter(Context context) {
        super(context);
    }

    @Override
    public UsersViewHolder createContentViewHolder(ViewGroup parent, int viewType) {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.item_recyclerview_users, parent, false);
        UsersViewHolder viewHolder = new UsersViewHolder(mView);
        return viewHolder;
    }

    @Override
    public void bindContentViewHolder(UsersViewHolder holder, int position) {
        final UserBean bean = getData().get(position);
        holder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bean.getType().equals("User")) {
                    OthersActivity.lanuchActivity(getContext(), bean.getLogin());
                } else if (bean.getType().equals("Organization")) {
                    OthersActivity.launchOrganizationActivity(getContext(), bean.getLogin());
                }
            }
        });
        holder.mUsernameTV.setText(bean.getLogin());
        ImageUtil.loadAvatarImageFromUrl(getContext(), bean.getAvatar_url(), holder.mAvatarIV);
    }
}
