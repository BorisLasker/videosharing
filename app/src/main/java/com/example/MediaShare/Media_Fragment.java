package com.example.MediaShare;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
import android.view.LayoutInflater;

import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class Media_Fragment extends Fragment {


    RecyclerView recyclerView;

    private DatabaseReference myRef;

    private ArrayList<MultiModel> messagesList;
    private MultiAdapter recyclerAdapter;
    private Context mContext;

    public Media_Fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_media, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManger = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManger);
        recyclerView.setHasFixedSize(true);

        myRef = FirebaseDatabase.getInstance().getReference();
        ClearALl();
        messagesList = GetDataFromFirebase();


        //recyclerAdapter = new MultiAdapter(messagesList, getContext().getApplicationContext());

        //recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));
        //recyclerView.setAdapter(recyclerAdapter);

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent intent = new Intent(getActivity(), Fragment_to_main.class);
                startActivity(intent);
                ((Activity) getActivity()).overridePendingTransition(0, 0);
                getActivity().startActivity(intent);

            }
            @Override
            public void onLongClick(View view, int position) {

                MultiModel message = messagesList.remove(position);
                recyclerAdapter.setDataSet(messagesList);
                recyclerView.setAdapter(recyclerAdapter);
            }
                 }));
            }






    private ArrayList<MultiModel> GetDataFromFirebase() {

        Query query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearALl();
                for (DataSnapshot snapshot : datasnapshot.getChildren()) {
                    Messages messages = new Messages();
                    messages.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    //TODO: POSSIBLE TYPES OF VIDEOS
                    MultiModel multiModel = null;

                    if (messages.getImageUrl().contains(".png") || messages.getImageUrl().contains(".jpg")) {
                        multiModel = new MultiModel(MultiModel.IMAGE_TYPE, messages, "image");

                    } else if (messages.getImageUrl().contains(".mp4")) {
                        multiModel = new MultiModel(MultiModel.VIDEO_TYPE, messages, "video");

                    }
                    messagesList.add(multiModel);


                }
                recyclerAdapter = new MultiAdapter(messagesList,getContext().getApplicationContext());
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return messagesList;

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


}