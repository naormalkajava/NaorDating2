package com.example.naormalka.naordating2;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.auth.User;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


/**
 * A simple {@link Fragment} subclass.
 */
public class Chat_fragment extends Fragment {

     List<UserChat> row_chat_user;
    ArrayAdapterChatUsers arrayAdapterChatUsers;
    private SwipeFlingAdapterView flingadapter;
     Chat_fragment newInstance() {

        Bundle args = new Bundle();
        Chat_fragment fragment = new Chat_fragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View v = inflater.inflate(R.layout.fragment_chat_fragment, container, false);
        Toast.makeText(getContext(), "ssssss", Toast.LENGTH_SHORT).show();




     //  row_chat_user = new ArrayList<UserChat>();
     //  arrayAdapterChatUsers = new ArrayAdapterChatUsers(getContext(), R.layout.chat_item, row_chat_user);
     //  row_chat_user.add(new UserChat("naor","https://firebasestorage.googleapis.com/v0/b/naordating2.appspot.com/o/profileImages%2F0fPQfDfG7CdM3wqYHzzK1IzkMzi1?alt=media&token=76029cb2-0c0e-48b5-abb6-fc91f20c28dd"));
     //  arrayAdapterChatUsers.notifyDataSetChanged();


     //  flingadapter.setAdapter(arrayAdapterChatUsers);




        return v;
    }





}
