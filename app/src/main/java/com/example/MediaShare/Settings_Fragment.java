package com.example.MediaShare;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Date;

public class Settings_Fragment extends Fragment {

    private static Button uploadBtn;
    private ImageView imageView;
    private ProgressBar progressBar;

    private Uri image;

    private DatabaseReference root = FirebaseDatabase.getInstance().getReference("message");
    private StorageReference reference = FirebaseStorage.getInstance().getReference();
    String email;
    String username;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings_image, container, false);
        //email = getArguments().getString("email");
         //username = getArguments().getString("username");
        Bundle message = getArguments();
        if (message != null){
            email = message.getString("email");
            username = message.getString("username");
        }else {
        }
       return view;
    }

    @Override
    public void onViewCreated(@NonNull View v, @Nullable Bundle savedInstanceState) {
        super.onViewCreated( v, savedInstanceState);


        uploadBtn = v.findViewById(R.id.upload_btn);
        progressBar = v.findViewById(R.id.progressBar);
        imageView = v.findViewById(R.id.imageView);

        progressBar.setVisibility(View.INVISIBLE);


        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent();
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                galleryIntent.setType("image/* video/*");
                startActivityForResult(galleryIntent , 2);
            }
        });


        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (image != null){
                    uploadToFirebase(image);
                }else{
                    Toast.makeText(getActivity(), "Please Select Image", Toast.LENGTH_SHORT).show();
                }
            }


        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode ==2 && resultCode == Activity.RESULT_OK && data != null){

            image = data.getData();
            imageView.setImageURI(image);

        }
    }




    private void uploadToFirebase(Uri uri) {

        final StorageReference  fileRef = reference.child(System.currentTimeMillis() + "." + getFileExtension(uri));
        fileRef.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        String currentDateTime = java.text.DateFormat.getDateTimeInstance().format(new Date());



                        Messages model = new Messages(uri.toString(),currentDateTime,email,username);
                        //int  id =   email.toString().hashCode();
                        String modelId = root.push().getKey();
                        root.child(modelId).setValue(model);

                        progressBar.setVisibility(View.INVISIBLE);
                        Toast.makeText(getContext(), "Uploaded Successfully", Toast.LENGTH_SHORT).show();
                        imageView.setImageResource(R.drawable.ic_baseline_add_photo_alternate_24);
                    }
                });
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                progressBar.setVisibility(View.VISIBLE);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(), "Uploading Failed !!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri mUri){

        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));

    }
    public static void dialog(boolean value){

        if(!value) {
            uploadBtn.setEnabled(false);
            Toast.makeText(uploadBtn.getContext(), "You are offline, You CAN'T UPLOAD A MEDIA", Toast.LENGTH_LONG).show();

        }
        else   uploadBtn.setEnabled(true);
    }
}