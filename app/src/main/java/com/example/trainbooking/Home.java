package com.example.trainbooking;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Home extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener setListener;
    EditText etDate,from,to;
    Button tvDate,search,cbook,tripp;
    TextView strt,end,scount,tfare;
    TextView item;
    Double fare,updfare;
    EditText stbook;
    DatabaseReference databaseReference,reff,reff1,reff2;
    FirebaseDatabase firebaseDatabase;
    int counter=1,savail1,totseats,basepriceseats,noofseats,nooftrips;
    String doj,departure,arrival,nooftickets,startp,stp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        cbook=(Button)findViewById(R.id.cbook);


        strt=(TextView)findViewById(R.id.strt);
        end=(TextView)findViewById(R.id.end);
        scount=(TextView)findViewById(R.id.scount);
        stbook=(EditText) findViewById(R.id.stbook);
        tfare=(TextView)findViewById(R.id.tfare);


        from=(EditText)findViewById(R.id.frm);
        to=(EditText)findViewById(R.id.too);
        search=findViewById(R.id.search);
        tvDate=findViewById(R.id.tvDate);
        etDate=findViewById(R.id.mydate);


        Calendar calendar=Calendar.getInstance();
        final int year= calendar.get(Calendar.YEAR);
        final int month=calendar.get(Calendar.MONTH);
        final int day= calendar.get(Calendar.DAY_OF_MONTH);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String fromloc = from.getText().toString().trim();
                final String toloc = to.getText().toString().trim();
                final String dattechck = etDate.getText().toString().trim();
                if(TextUtils.isEmpty(fromloc)){
                    Toast.makeText(Home.this,"Please Enter Source Location!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(toloc)){
                    Toast.makeText(Home.this,"Please Enter Destination Location!",Toast.LENGTH_SHORT).show();
                    return;
                }

                if(TextUtils.isEmpty(dattechck)){
                    Toast.makeText(Home.this,"Please Enter Date!",Toast.LENGTH_SHORT).show();
                    return;
                }





                reff = FirebaseDatabase.getInstance().getReference().child("locations").child("from");
                reff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if(dataSnapshot.hasChild(fromloc)){
                            reff1=FirebaseDatabase.getInstance().getReference().child("locations").child("from").child(fromloc).child(toloc);
                            reff1.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    doj=dataSnapshot.child("dateofjourney").getValue().toString().trim();
                                    String value = etDate.getText().toString();
                                    if(doj.equals(value)) {
                                        departure = dataSnapshot.child("departure").getValue().toString().trim();
                                        arrival = dataSnapshot.child("arrival").getValue().toString().trim();
                                        startp=dataSnapshot.child("from").getValue().toString().trim();
                                        stp=dataSnapshot.child("to").getValue().toString().trim();

                                        String savail = dataSnapshot.child("savail").getValue().toString().trim();
                                        String fare1 = dataSnapshot.child("updfare").getValue().toString().trim();

                                        totseats=dataSnapshot.child("totalseats").getValue(Integer.class);
                                        savail1=dataSnapshot.child("savail").getValue(Integer.class);
                                        fare = dataSnapshot.child("fare").getValue(Double.class);
                                        updfare = dataSnapshot.child("updfare").getValue(Double.class);
                                        basepriceseats = dataSnapshot.child("baseprseats").getValue(Integer.class);
                                        strt.setText(departure);
                                        end.setText(arrival);
                                        scount.setText(savail);
                                        tfare.setText(fare1);
                                    }
                                    else {
                                        Toast.makeText(Home.this,"Sorry ! No trains currently available on this day !",Toast.LENGTH_SHORT).show();
                                        return;
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Home.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth,setListener,year,month,day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();

            }
        });




        setListener=new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month=month+1;
                String date=dayOfMonth+"/"+month+"/"+year;

                etDate.setText(date);
            }
        };
        etDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog=new DatePickerDialog(Home.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month=month+1;
                        String date=day+"/"+month+"/"+year;

                        etDate.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
    }



    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager=(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(),0);
    }


    public void confirmBooking(View view) {
        if(savail1<0){
            Toast.makeText(Home.this,"Sorry, Bookings Closed for this train!",Toast.LENGTH_SHORT).show();
        }
        final String noofSeat = stbook.getText().toString();
        noofseats = Integer.parseInt(stbook.getText().toString());

        if(TextUtils.isEmpty(noofSeat)){
            Toast.makeText(Home.this,"Please Enter number of seats",Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(noofSeat)) {
            Toast.makeText(Home.this, "This item cannot be left Empty!", Toast.LENGTH_SHORT).show();
        }
        if (noofseats > 0 && noofseats < 6) {
            AlertDialog.Builder builder = new AlertDialog.Builder(Home.this);

            builder.setMessage("Click Yes to pay Rs." + (float)noofseats * updfare);
            builder.setCancelable(true);
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (savail1 < (totseats - basepriceseats)) {
                        reff1.child("updfare").setValue(updfare + 0.1 * updfare);
                    }
                    reff1.child("savail").setValue(savail1 - noofseats);
                    final String username = getIntent().getStringExtra("user");
                    if (username != null) {
                        reff2 = FirebaseDatabase.getInstance().getReference().child("users").child(username);
                        reff2.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                Integer nooftrips = snapshot.child("nooftrips").getValue(Integer.class);
                                if (nooftrips == null) {
                                    Toast.makeText(Home.this,"No of trips Empty Error",Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        reff2.child("Journey-" + nooftrips).child("dateofjourney").setValue(doj);
                        reff2.child("Journey-" + nooftrips).child("from").setValue(startp);

                        reff2.child("Journey-" + nooftrips).child("nooftickets").setValue(noofseats);
                        reff2.child("Journey-" + nooftrips).child("to").setValue(stp);
                        nooftrips++;
                        Toast.makeText(Home.this, "" + nooftrips, Toast.LENGTH_SHORT).show();
                        reff2.child("nooftrips").setValue(nooftrips);
                        startActivity(new Intent(Home.this, Home.class));
                        Toast.makeText(Home.this, "Successfully Booked", Toast.LENGTH_SHORT).show();

                    }
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        } else {
            Toast.makeText(Home.this, "No of seats should be minimum 1 and must not exceed 5", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onBackPressed(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Home.this);
        builder.setMessage("Click Yes to Logout!");
        builder.setCancelable(true);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Home.this.finish();
                startActivity(new Intent(Home.this,UserLogin.class));
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

}
