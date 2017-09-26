package com.example.naormalka.naordating2;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class NewMatchFragment extends DialogFragment {

    BootstrapButton btnNewConversion;
    BootstrapButton btnKeepSurfing;
    CircleImageView profile1;
    CircleImageView profile2;
    TextView etprofile1;
    TextView etprofile2;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View v = inflater.inflate(R.layout.fragment_new_match, container, false);

        TypefaceProvider.registerDefaultIconSets();

        profile1 = (CircleImageView) v.findViewById(R.id.profile1);
        profile2 = (CircleImageView) v.findViewById(R.id.profile2);
        btnKeepSurfing = (BootstrapButton) v.findViewById(R.id.keepSurfing);
        btnNewConversion = (BootstrapButton) v.findViewById(R.id.btnConversion);
        etprofile1 = (TextView) v.findViewById(R.id.etprofile1);
        etprofile2 = (TextView) v.findViewById(R.id.etprofile2);

        return v;

    }



}
