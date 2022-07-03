package com.example.videosharing;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class signupform extends AppCompatActivity {

    // creating a variable for our
    // Firebase Database.
    FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    DatabaseReference databaseReference;

    EditText firstName;
    EditText password;
    EditText email;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupform);

        firstName = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPass);

        register = findViewById(R.id.buttonAcount);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                checkDataEntered();
                writeNewUser();
            }
        });
    }

    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    void checkDataEntered() {

        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
        }

        if (isEmpty(password)) {
            password.setError("You must enter password!");

        }
    }
    public void writeNewUser() {
        User user = new User(firstName.getText().toString(), email.getText().toString(),password.getText().toString());


            int  id =   email.getText().toString().hashCode();

        databaseReference.child("User").child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()){
                    Toast.makeText(signupform.this, "user already exists", Toast.LENGTH_LONG).show();
                    // use "username" already exists
                    // Let the user know he needs to pick another username.
                } else {
                    // User does not exist. NOW call createUserWithEmailAndPassword

                    //Saving the user
                    databaseReference.child("User").child(String.valueOf(id)).setValue(user);
                    //displaying a success toast
                    Toast.makeText(signupform.this, "user added", Toast.LENGTH_LONG).show();


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });








    }



}




































