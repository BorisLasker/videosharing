package com.example.videosharing;


import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class signupform extends AppCompatActivity {

    //mDatabase = FirebaseDatabase.getInstance().getReference();

    // Write a message to the database
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference();


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
        Log.i("abc", "here: ");
        User user = new User(firstName.toString(), email.toString(),password.toString());

        myRef.child("database").child("Users").child("username").setValue("name");
    }

}




































