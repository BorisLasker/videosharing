package com.example.videosharing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginForm extends AppCompatActivity {
    private EditText EmailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;


    private FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EmailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // below line is used to get the
        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // below line is used to get reference for our database.
        databaseReference = firebaseDatabase.getReference();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (EmailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {


                    int id = EmailEditText.getText().toString().hashCode();
                    databaseReference.child("User").child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.child("password").getValue().equals(passwordEditText.getText().toString())) {
                                    String toastMessage = "Username: " + EmailEditText.getText().toString() + " \nPassword: " + passwordEditText.getText().toString();
                                    Toast.makeText(getApplicationContext(), toastMessage + "\nYou are logged in!", Toast.LENGTH_SHORT).show();
                                    // user logged in


                                    Intent intent = new Intent(v.getContext(), Main.class);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(getApplicationContext(), "password is incorrect", Toast.LENGTH_SHORT).show();

                                }

                            } else {
                                // User does not exist. NOW call createUserWithEmailAndPassword
                                Toast.makeText(LoginForm.this, "You need to register first!", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


                } else {
                    String toastMessage = "Email or Password are not populated";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }


            }
        });


        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), SignUpForm.class);
                startActivity(intent);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.exit,menu);
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