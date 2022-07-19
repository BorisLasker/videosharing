package com.example.MediaShare;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.preference.PreferenceManager;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;


import android.util.Log;
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

    private ArrayList<MultiModel> messagesList;
    private  ArrayList<MultiModel>tempmessageList = new ArrayList<MultiModel>();

    private MultiAdapter recyclerAdapter;


    String email;
    public Media_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        setHasOptionsMenu(true);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_media, container, false);
        Bundle message = getArguments();
        if (message != null){
            email = message.getString("email");
        }else {
        }
        return view;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManger = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();



        //-----SharedPreferences-----------
        remove_medialist = new ArrayList<>();
        for(Map.Entry<String,?> entry : prefs.getAll().entrySet()){
            if (entry.getValue() instanceof String) {
                remove_medialist.add(String.valueOf(entry.getValue()));
            }
        }

        ClearALl();


        GetDataFromFirebase();



        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {

            @Override
            public void onClick(View view, int position) {


                Intent intent = new Intent(getActivity(), Fragment_to_main.class);

                MultiModel info = messagesList.get(position);

                intent.putExtra("time",info.data.getCurrentDateTime());
                intent.putExtra("email",info.data.getEmail());
                intent.putExtra("username",info.data.getUsername());
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                getActivity().startActivity(intent);

            }
            @Override
            public void onLongClick(View view, int position) {

                //-----SharedPreferences-----------
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity().getBaseContext());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(messagesList.get(position).data.getImageUrl(),messagesList.get(position).data.getImageUrl());
                    editor.commit();
                //-----end-----------
                MultiModel message = messagesList.remove(position);
                recyclerAdapter.setDataSet(messagesList);
                recyclerView.setAdapter(recyclerAdapter);
            }
                 }));
            }







    private void GetDataFromFirebase() {

        Query query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearALl();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Messages messages = new Messages();
                    messages.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    messages.setCurrentDateTime(snapshot.child("currentDateTime").getValue().toString());
                    messages.setUsername(snapshot.child("username").getValue().toString());
                    messages.setEmail(snapshot.child("email").getValue().toString());

                    //TODO: POSSIBLE TYPES OF VIDEOS
                    MultiModel multiModel = null;

                    if (messages.getImageUrl().contains(".png") || messages.getImageUrl().contains(".jpg")) {
                        multiModel = new MultiModel(MultiModel.IMAGE_TYPE, messages, "image");

                    } else if (messages.getImageUrl().contains(".mp4")) {
                        multiModel = new MultiModel(MultiModel.VIDEO_TYPE, messages, "video");

                    }

                    messagesList.add(multiModel);
                    Log.i("test", multiModel.getData().getImageUrl());

                    for (MultiModel message : messagesList) {
                        Log.i("test", message.getData().getImageUrl());


                    }

                }


                for (MultiModel message : messagesList) {
                    Log.i("1234","list"+ message.data.getImageUrl());
                    if (!remove_medialist.contains(message.data.getImageUrl())){
                        Log.i("1234","here");
                        tempmessageList.add(message);
                        Log.i("a", message.data.getImageUrl()+"  "+message.data.getCurrentDateTime());
                    }
                }
                messagesList.clear();
                for (MultiModel temp : tempmessageList) {
                    messagesList.add(temp);
                    Log.i("12345","templist"+ temp);

                }


                recyclerAdapter = new MultiAdapter(messagesList,getContext().getApplicationContext());
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }


    private void ClearALl() {
        if (messagesList != null) {
            messagesList.clear();

            if (recyclerAdapter != null) {
                recyclerAdapter.notifyDataSetChanged();
            }

        }

        messagesList = new ArrayList<>();
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
                SharedPreferences.Editor editor = prefs.edit();
                editor.clear();
                editor.commit();
                //-----end-----------
                //-----SharedPreferences-----------
                remove_medialist = new ArrayList<>();
                for(Map.Entry<String,?> entry : prefs.getAll().entrySet()){
                    if (entry.getValue() instanceof String) {
                        remove_medialist.add(String.valueOf(entry.getValue()));
                    }
                }
                return true;

            default:
                break;
        }

        return false;
    }



}