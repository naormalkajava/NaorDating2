package com.example.naormalka.naordating2;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ArrayAdapterChatUsers extends ArrayAdapter<UserChat> {


    public ArrayAdapterChatUsers(Context context, int resource, List<UserChat> item) {
        super(context, resource, item);
    }

    public View getView (int position , View convertView , ViewGroup parent) {
        UserChat userChat= getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.chat_item,parent,false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.tvUserProfileOp);
        ImageView imageprofile = (ImageView) convertView.findViewById(R.id.ivProfile_Chat);

        name.setText(userChat.getDisplayName());
        Glide.with(getContext()).load(userChat.getProfileImage()).into(imageprofile);
        return convertView;

    }
}
