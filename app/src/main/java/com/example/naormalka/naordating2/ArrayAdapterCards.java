package com.example.naormalka.naordating2;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.firebase.ui.storage.images.FirebaseImageLoader;

import android.net.Uri;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.IOException;
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

   //  if (cards_item.getProfileImageUrl().contains("defalut")) {
   //      Glide.with(getContext()).load(R.drawable.profilelancher).into(image);
   //  }
   //  else
   //  {
   //      Glide.with(convertView.getContext()).load(cards_item.getProfileImageUrl()).into(image);
   //  }
  switch (cards_item.getProfileImageUrl()) {
      case "default" :
          Glide.with(convertView.getContext()).load(R.mipmap.ic_launcher).into(image);
          break;
          default:
              Glide.clear(image);
              Glide.with(convertView.getContext()).load(cards_item.getProfileImageUrl()).into(image);
              break;
  }






        return convertView;
    }


}
