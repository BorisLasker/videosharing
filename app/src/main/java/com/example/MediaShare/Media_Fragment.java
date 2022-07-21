package com.example.MediaShare;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.preference.PreferenceManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;


public class Media_Fragment extends Fragment {

    private static ArrayList<String> remove_medialist;
    RecyclerView recyclerView;
    private DatabaseReference myRef;

    private ArrayList<MultiMedia> MultiMedia_List;
    private ArrayList<MultiMedia> TempMultiMedia_List = new ArrayList<MultiMedia>();

    private MultiMediaAdapter recyclerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Shared prefs for the removed Media.
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());

        //Recycler view creation and bounding.
        recyclerView = view.findViewById(R.id.recyclerview);
        LinearLayoutManager layoutManger = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();


        //-----SharedPreferences-----------
        // adds the removed media from prefs to the remove_media array list
        remove_medialist = new ArrayList<>();
        for(Map.Entry<String,?> entry : prefs.getAll().entrySet()){
            if (entry.getValue() instanceof String) {
                remove_medialist.add(String.valueOf(entry.getValue()));
            }
        }

        ClearALl();

        GetDataFromFirebase();

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            /*

            require 1 2 3


             */
            @Override
            public void onClick(View view, int position) {

                //going bac to frag_to_main activity because we are in a fragment.
                Intent intent = new Intent(getActivity(), Fragment_to_main.class);

                //info contains the multimedia object in the position that was clicked.
                MultiMedia info = MultiMedia_List.get(position);
                intent.putExtra("time",info.data.getCurrentDateTime());
                intent.putExtra("email",info.data.getEmail());
                intent.putExtra("username",info.data.getUsername());
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                getActivity().startActivity(intent);

            }

            // Long click deletes the media object from the recyclerview by adding it to the prefs.
            @Override
            public void onLongClick(View view, int position) {

                //-----SharedPreferences-----------
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(MultiMedia_List.get(position).data.getImageUrl(), MultiMedia_List.get(position).data.getImageUrl());
                    editor.commit();
                //-----end-----------

                MultiMedia_List.remove(position);
                recyclerAdapter.setDataSet(MultiMedia_List);
                recyclerView.setAdapter(recyclerAdapter);
            }}));
            /*

            require 1 2 3


             */
    }


    private void GetDataFromFirebase() {

        Query query = myRef.child("message");
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {

                ClearALl();

                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Media media = new Media();
                    media.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    media.setCurrentDateTime(snapshot.child("currentDateTime").getValue().toString());
                    media.setUsername(snapshot.child("username").getValue().toString());
                    media.setEmail(snapshot.child("email").getValue().toString());

                    //TODO: POSSIBLE TYPES OF VIDEOS
                    MultiMedia multimedia = null;

                    //if the current media is a picture.
                    if (media.getImageUrl().contains(".png") || media.getImageUrl().contains(".jpg")) {
                        multimedia = new MultiMedia(MultiMedia.IMAGE_TYPE, media, "image");

                        //if the current media is a video file.
                    } else if (media.getImageUrl().contains(".mp4")) {
                        multimedia = new MultiMedia(MultiMedia.VIDEO_TYPE, media, "video");
                    }
                    MultiMedia_List.add(multimedia);
                }

                for (MultiMedia multimedia : MultiMedia_List) {
                    //temp list contains only the media that was not removed.
                    if (!remove_medialist.contains(multimedia.data.getImageUrl())){
                        TempMultiMedia_List.add(multimedia);
                    }
                }

                //updating the multimedia list to contain only the not removed media.
                MultiMedia_List.clear();
                for (MultiMedia temp : TempMultiMedia_List) {
                    MultiMedia_List.add(temp);
                }

                // After the Multimedia list is complete, create recyclerview.
                recyclerAdapter = new MultiMediaAdapter(MultiMedia_List,getContext().getApplicationContext());
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    // clears the data of the adapter and multimedia list
    private void ClearALl() {
        if (MultiMedia_List != null) {
            MultiMedia_List.clear();
            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }
        }
        MultiMedia_List = new ArrayList<>();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Do something that differs the Activity's menu here
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletedmedia:
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                //-----SharedPreferences-----------
                //Clearing the prefs so the media is restored.
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                //-----end-----------
                //-----SharedPreferences-----------

                remove_medialist = new ArrayList<>();
                getActivity().recreate();
                return true;
            default:
                break;
        }
        return false;
    }
}