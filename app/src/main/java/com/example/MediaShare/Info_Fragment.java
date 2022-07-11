package com.example.MediaShare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Info_Fragment extends Fragment {

    TextView textView;
    Button btn_delete;
    String txt ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_info, container, false);

        if(getArguments()!= null){

             //txt = getArguments().getString("message");

        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textView = view.findViewById(R.id.textView);
        btn_delete = view.findViewById(R.id.btn_delete);
        textView.setText(txt);
/*
        btn_delete.setOnClickListener((View.OnClickListener) new RecyclerTouchListener(getContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {

            }
            @Override
            public void onLongClick(View view, int position) {

                MultiModel message = messagesList.remove(position);
                recyclerAdapter.setDataSet(messagesList);
                recyclerView.setAdapter(recyclerAdapter);
            }
        }));*/
    }


    }


