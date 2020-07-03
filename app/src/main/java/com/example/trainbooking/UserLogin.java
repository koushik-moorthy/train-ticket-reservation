package com.example.trainbooking;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserLogin extends AppCompatActivity {
    EditText usrname,password;
    Button login;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    DatabaseReference reff;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_login);

        usrname=findViewById(R.id.email);
        password=findViewById(R.id.pass);
        login=findViewById(R.id.log);

        firebaseAuth=FirebaseAuth.getInstance();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if(firebaseAuth.getCurrentUser()!=null)
//                {
//                    startActivity(new Intent(UserLogin.this,UserLogin.class));
//                }
//            }
//        };




        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email=usrname.getText().toString().trim();
                final String pass=password.getText().toString().trim();
                final String email1=usrname.getText().toString().trim();
                final String pass1=password.getText().toString().trim();

                if(TextUtils.isEmpty(email)){
                    Toast.makeText(UserLogin.this,"Please Enter Email",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(pass)){
                    Toast.makeText(UserLogin.this,"Please Enter Password",Toast.LENGTH_SHORT).show();
                    return;
                }

//                firebaseAuth.createUserWithEmailAndPassword(email, pass)
//                        .addOnCompleteListener(UserLogin.this, new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    startActivity(new Intent(UserLogin.this,BookingActivity.class));
//
//                                } else {
//                                    Toast.makeText(UserLogin.this,email+pass,Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                            }
//                        });

                reff = FirebaseDatabase.getInstance().getReference().child("users").child(email);
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String password = snapshot.child("password").getValue().toString().trim();
                            if (pass1.equals(password)) {
                                Intent transfer = new Intent(UserLogin.this, Home.class);

                                transfer.putExtra("user", email);

                                startActivity(transfer);

//                                Toast.makeText(UserLogin.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(UserLogin.this, "Password Incorrect!", Toast.LENGTH_SHORT).show();
                            }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });













//                            firebaseAuth.signInWithEmailAndPassword(email, pass)
//                        .addOnCompleteListener(UserLogin.this
//                                , new OnCompleteListener<AuthResult>() {
//                            @Override
//                            public void onComplete(@NonNull Task<AuthResult> task) {
//                                if (task.isSuccessful()) {
//                                    startActivity(new Intent(UserLogin.this,Home.class));
//                                } else {
//                                    Toast.makeText(UserLogin.this,"Invalid Email/Password !",Toast.LENGTH_SHORT).show();
//
//                                }
//
//
//                            }
//                        });
            }
        });
    }

    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(UserLogin.this);
        builder.setMessage("Click Yes to Quit!");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishAffinity();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }
}
