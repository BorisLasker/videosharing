package com.example.MediaShare;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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


public class Media extends Fragment  {


    RecyclerView recyclerView;

    private DatabaseReference myRef;

    private ArrayList<MultiModel> messagesList;
    private MultiAdapter recyclerAdapter;
    private Context mContext;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext=context;
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

        messagesList = new ArrayList<>();

        ClearALl();

        GetDataFromFirebase();
    }

    private void GetDataFromFirebase() {

        Query query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                ClearALl();
                for (DataSnapshot snapshot : datasnapshot.getChildren()){
                    Messages messages = new Messages();
                    messages.setImageUrl(snapshot.child("imageUrl").getValue().toString());
                    //TODO: POSSIBLE TYPES OF VIDEOS
                    MultiModel multiModel = null;
                    if (messages.getImageUrl().contains(".png") ||messages.getImageUrl().contains(".jpg")){
                        multiModel = new MultiModel(MultiModel.IMAGE_TYPE,messages,"image") ;

                    }
                    else if (messages.getImageUrl().contains(".mp4")){
                        multiModel = new MultiModel(MultiModel.VIDEO_TYPE,messages,"video") ;

                    }
                    messagesList.add(multiModel);




                }
                recyclerAdapter = new MultiAdapter(messagesList,getContext().getApplicationContext() );
                recyclerView.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });





    }

    private void ClearALl(){
        if(messagesList != null){
            messagesList.clear();

            if(recyclerAdapter != null){
                recyclerAdapter.notifyDataSetChanged();
            }

        }

        messagesList = new ArrayList<>();
    }

}