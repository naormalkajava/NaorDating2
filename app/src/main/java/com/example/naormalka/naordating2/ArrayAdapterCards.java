package com.example.naormalka.naordating2;

import android.content.Context;
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

    Context context;
    String name;

    public ArrayAdapterCards(Context context, int resourceId , List<Cards> items) {
      super(context,resourceId,items);
    }

    public View getView (int position , View convertView, ViewGroup parent ) {

        Cards cards_item = getItem(position) ;

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView  name = (TextView) convertView.findViewById(R.id.name);
        ImageView  image = (ImageView) convertView.findViewById(R.id.image);
        name.setText(cards_item.getName());
        Picasso.with(getContext()).load(cards_item.getImagesource()).into(image);
      //  Glide.with(getContext()).load(cards_item.getImagesource()).into(image);
        return convertView;
    }


}
