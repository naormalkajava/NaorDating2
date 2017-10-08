package com.example.naormalka.naordating2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.bumptech.glide.load.model.GlideUrl;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by naor malka on 10/09/2017.
 */

public class ArrayAdapterCards extends android.widget.ArrayAdapter<Cards> {

    String name;
    Context a;

    public ArrayAdapterCards(Context context, int resourceId, List<Cards> items) {
        super(context, resourceId, items);

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        Cards cards_item = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.name);
        ImageView image = (ImageView) convertView.findViewById(R.id.image23);
        name.setText(cards_item.getName());
        String profileImageUrl = cards_item.getProfileImageUrl();
        Glide.with(getContext()).load().dontAnimate().into(image);


        return convertView;
    }


}
