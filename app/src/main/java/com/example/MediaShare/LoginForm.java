package com.example.MediaShare;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class LoginForm extends AppCompatActivity {
    private EditText EmailEditText;
    private EditText passwordEditText;
    private CheckBox checkbox;
    private static Button loginButton;
    private Button registerButton;
    public Context context;

    private static ArrayList<String> RememberMe;
    private static String file_path;
    public static final String FILE_NAME="RememberMe.txt";
    private BroadcastReceiver mNetworkReceiver;

    private FirebaseDatabase firebaseDatabase;

    // creating a variable for our Database
    // Reference for Firebase.
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.context = getApplicationContext();

        //Creating intent of the foreground service
        Intent serviceIntent = new Intent(this, ForegroundService.class);
         ContextCompat.startForegroundService(this, serviceIntent);

        //Creating new instance of network reciever which extends broadcast reciever.
        mNetworkReceiver = new NetworkReceiver();
        registerReceiver(mNetworkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));


        checkbox = findViewById(R.id.checkBox);
        EmailEditText = findViewById(R.id.email);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);

        // instance of our Firebase database.
        firebaseDatabase = FirebaseDatabase.getInstance();

        // get reference for our database.
        databaseReference = firebaseDatabase.getReference();

        // Raw file innitiation for RememberMe feature.
        file_path=context.getFilesDir().getAbsolutePath();
        set_fields();



        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (EmailEditText.getText().length() > 0 && passwordEditText.getText().length() > 0) {

                    int id = EmailEditText.getText().toString().hashCode();

                    //search the entered email in the database.
                    databaseReference.child("User").child(String.valueOf(id)).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            //Entered email exists in database.
                            if (dataSnapshot.exists()) {
                                if (dataSnapshot.child("password").getValue().equals(passwordEditText.getText().toString())) {
                                    String toastMessage = "Email: " + EmailEditText.getText().toString() + " \nPassword: " + passwordEditText.getText().toString();
                                    Toast.makeText(getApplicationContext(), toastMessage + "\nYou are logged in!", Toast.LENGTH_SHORT).show();
                                    // user logged in

                                    String username = dataSnapshot.child("username").getValue().toString();

                                    //Overriding the existing data in the raw file with the current email and password
                                    if (checkbox.isChecked()){
                                        writeData(EmailEditText.getText() + "\n" + passwordEditText.getText());
                                    }
                                    else writeData("");


                                    //After logging in, passing the email and username to the main activity.
                                    Intent intent = new Intent(v.getContext(), Main.class);
                                    intent.putExtra("email", EmailEditText.getText().toString());
                                    intent.putExtra("username", username);
                                    startActivity(intent);


                                } else {
                                    Toast.makeText(getApplicationContext(), "password is incorrect", Toast.LENGTH_SHORT).show();

                                }

                            //Email was not found in database
                            } else {
                                Toast.makeText(LoginForm.this, "You need to register first!", Toast.LENGTH_LONG).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) { }
                    });


                } else {
                    String toastMessage = "Email or Password are not populated";
                    Toast.makeText(getApplicationContext(), toastMessage, Toast.LENGTH_SHORT).show();
                }


            }
        });

        //Changes the raw file according to the checkbox
        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(((CompoundButton) view).isChecked()){
                    writeData(EmailEditText.getText() + "\n" + passwordEditText.getText());
                } else {
                    writeData("");
                }
            }
        });

        //Moving to the register activity
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SignUpForm.class);
                startActivity(intent);
            }
        });
    }

    //If RememberMe was checked, sets the email and password with the file's content.
    private void set_fields() {
        RememberMe = new ArrayList<String>(Arrays.asList(readFile().split("\n")));
        if(RememberMe.size()>1){
            EmailEditText.setText(RememberMe.get(0));
            passwordEditText.setText(RememberMe.get(1));
            checkbox.setChecked(true);
        }
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

    //On selecting exit option
    private void showDialog() {
        FragmentManager fm = getSupportFragmentManager();
        MyAlertDialogFragment alertDialog = MyAlertDialogFragment.newInstance("Closing the application","Are you sure","Yes","No");
        alertDialog.show(fm, "fragment_alert");
    }

    //Reading and returning lines from the file.
    @NonNull
    private String readFile() {
        StringBuilder text= null;
        String line;
        //Get the text file
        File file = new File(file_path,File.separator+FILE_NAME);
        try {
            if(!file.exists())
                file.createNewFile();

            //Read text from file
            InputStream inputStream=new FileInputStream(file);
            text = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }

            inputStream.close();
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return text.toString();
    }


    //function for raw file////
    public void writeData(String data) {
        File directory = new File(file_path);
        if(!directory.exists())
            directory.mkdir();

        File newFile = new File(file_path,File.separator+FILE_NAME);
        try  {
            if(!newFile.exists())
                newFile.createNewFile();

            FileOutputStream fOut = new FileOutputStream(newFile,false);
            OutputStreamWriter outputWriter=new OutputStreamWriter(fOut);
            outputWriter.write(data+"\n");
            outputWriter.close();

        }catch (Exception e){
            e.printStackTrace();
        }
    }


}