package com.example.MediaShare;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SignUpForm extends AppCompatActivity {

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
    View view;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signupform);

        firstName = findViewById(R.id.editName);
        email = findViewById(R.id.editEmail);
        password = findViewById(R.id.editPass);

        register = findViewById(R.id.buttonAcount);

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

    //Check if the entered email is in the current format.
    boolean isEmail(EditText text) {
        CharSequence email = text.getText().toString();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    boolean isEmpty(EditText text) {
        CharSequence str = text.getText().toString();
        return TextUtils.isEmpty(str);
    }

    //Check if the entered fields are correct.
    void checkDataEntered() {
        if (isEmail(email) == false) {
            email.setError("Enter valid email!");
        }
        if (isEmpty(password)) {
            password.setError("You must enter password!");
        }
    }

    //Creating User object with 3 fields
    //Saving the User in the database
    public void writeNewUser() {
        User user = new User(firstName.getText().toString(), email.getText().toString(),password.getText().toString());
            int  id =   email.getText().toString().hashCode();
        databaseReference.child("User").child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // use "username" already exists
                // Let the user know he needs to pick another username.
                if(dataSnapshot.exists()){
                    Toast.makeText(SignUpForm.this, "user already exists", Toast.LENGTH_LONG).show();

                } else {
                    // User does not exist, Saving the user.
                    databaseReference.child("User").child(String.valueOf(id)).setValue(user);
                    //displaying a success toast
                    Toast.makeText(SignUpForm.this, "user added", Toast.LENGTH_LONG).show();

                    //After registeration, going back to login screen.
                    Intent intent = new Intent(view.getContext(), LoginForm.class);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) { }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                showDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MyAlertDialogFragment alertDialog =MyAlertDialogFragment.newInstance("Closing the application","Are you sure","Yes","No");
        alertDialog.show(fm, "fragment_alert");
    }
}




































