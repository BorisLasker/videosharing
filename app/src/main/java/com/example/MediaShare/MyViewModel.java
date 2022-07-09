package com.example.MediaShare;


import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class MyViewModel extends AndroidViewModel {
    private ArrayList<MultiModel> messagesList;
    private MutableLiveData<ArrayList<MultiModel>> mediaList = new MutableLiveData<ArrayList<MultiModel>>();
    private final MutableLiveData<Integer> index = new MutableLiveData<Integer>();
    public Context context;
    private static ArrayList<String> remove_media;
    private ArrayList<MultiModel> tempMediaList;
    private static String file_path;
    public static final String FILE_NAME = "remove_media.txt";

    public MyViewModel(@NonNull Application application) {
        super(application);
        this.context = application.getApplicationContext();
        initiali(application.getApplicationContext());
    }

    public MutableLiveData<ArrayList<MultiModel>> getMediaList() {
        return mediaList;
    }

    public MutableLiveData<Integer> getIndex() {
        return index;
    }

    public void setIndex(int i) {
        index.setValue(i);
    }


    private void initiali(Context applicationContext) {
        index.setValue(-1);
        file_path = context.getFilesDir().getAbsolutePath();
        tempMediaList = new ArrayList<MultiModel>();
        mediaList.setValue(messagesList);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        boolean remember = prefs.getBoolean("check_box_preference_1", false);


        remove_media = new ArrayList<>();
        for(Map.Entry<String,?> entry : prefs.getAll().entrySet()){
            if (entry.getValue() instanceof String) {
                remove_media.add(String.valueOf(entry.getValue()));
            }
        }


        if(remember == true)
        {
            for (Country country : Media.getValue()) {
                if (!remove_countries.contains(country.getName())) {
                    tempCountryList.add(country);
                }
            }
            countryList.setValue(tempCountryList);
        }
        else {
            //-----SharedPreferences-----------
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.commit();
            //-----end-----------
        }
    }




    private void GetDataFromFirebase() {
        DatabaseReference myRef;
        myRef = FirebaseDatabase.getInstance().getReference();

        Query query = myRef.child("message");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}