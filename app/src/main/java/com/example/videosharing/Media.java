package com.example.videosharing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public class Media extends Fragment {


    RecyclerView recyclerView;

    private DatabaseReference myRef;

    private ArrayList<Messages> messagesList;

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

        messagesList = new ArrayList<>();

        ClearALl();

        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {


    }

    private void ClearALl(){
        if(messagesList != null){
            messagesList.clear();

        }

        messagesList = new ArrayList<>();
    }
}