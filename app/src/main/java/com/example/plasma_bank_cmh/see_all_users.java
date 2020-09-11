package com.example.plasma_bank_cmh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class see_all_users extends AppCompatActivity {
    MyTable3 myTable;
    TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_all_users);


        //**************************CREATING SPACE IN MEMORY FOR THE TABLE*************************//
        setUpTheTableLayoutObject();

        myTable.addTableHeaderContents(new String[]{"Name", "Contact","E-mail","Role"},4);
        //*************************INSERTING TABLE DATA******************************************//
        // Write a message to the database

        DatabaseReference myRef = FirebaseDatabase.getInstance().getReference("user");

        // Read from the database
        //ValueEventListener valueEventListener=getAValueEventListener();
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.child("request_status").getValue().toString().equals("approved"))
                    {
                        String name=ds.child("name").getValue().toString();
                        String contact=ds.child("contact").getValue().toString();
                        String mail=ds.child("mail").getValue().toString();
                        String role=ds.child("role").getValue().toString();
                        myTable.addStringArrayContents(new String[]{name, contact, mail,role},4);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });







    }
    void setUpTheTableLayoutObject(){
        tableLayout=findViewById(R.id.table_id);
        myTable= new MyTable3(tableLayout,see_all_users.this);
        Log.d("TAG1","I have made myTable");
    }

    ValueEventListener getAValueEventListener(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TAG1","In side on data change");
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    if (ds.child("request_status").getValue().toString().equals("approved"))
                    {
                        String name=ds.child("name").getValue().toString();
                        String contact=ds.child("contact").getValue().toString();
                        String mail=ds.child("mail").getValue().toString();
                        String role=ds.child("role").getValue().toString();
                        myTable.addStringArrayContents(new String[]{name, contact, mail,role},4);
                    }

                }
                Log.d("TAG1","Loop passed");

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        };

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.home_button, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            Intent intent=new Intent(getApplicationContext(),Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onBackPressed() {
        Intent intent=new Intent(getApplicationContext(),pending_req.class);
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}
