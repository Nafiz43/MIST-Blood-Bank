package com.example.plasma_bank_cmh;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SeeDonationsActivity extends AppCompatActivity {
    ListView seeDonationsListView;
    TextView totalDonationsTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_donations);


        Intent intent=getIntent();
        String key=intent.getStringExtra("Key");
        Toast.makeText(SeeDonationsActivity.this,"I came with key="+key,Toast.LENGTH_SHORT).show();
        //*********************************GETTING DATA INTO THE LISTVIEW********************************************//
        final ArrayList<String> arrayList=new ArrayList();
        final int[] c = {1};

        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("donor").child(key).child("donationDates");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Log.d("TAGZ","This user made no donations");
                    return;
                }else{
                    Log.d("TAGZ","This user MADE donations");
                    for(DataSnapshot ds:dataSnapshot.getChildren()){

                        arrayList.add("Donation number "+ c[0] +" at date :"+String.valueOf(ds.getValue()));
                        c[0] = c[0] +1;
                    }
                    insertDataIntoTheListView(arrayList);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        //*********************************GOT DATA INTO LISTVIEW********************************************//



    }

    private void insertDataIntoTheListView(ArrayList<String> arrayList) {
        //**************************************THE ARRAY LIST IS READY********************************************//
        //*****************************************GETTING DATA INTO STRING ARRAY*************************************//
        String[] stringArray=new String[arrayList.size()];

        for(int i=0;i<arrayList.size();i++ ){
            stringArray[i]=arrayList.get(i);
        }
        //*******************************I AM GOING TO USE THE ARRAYLIST NOW FOR LISTVIEW*******************************//
        seeDonationsListView=findViewById(R.id.seeDonationListView);
        ArrayAdapter arrayAdapter=new ArrayAdapter(SeeDonationsActivity.this,R.layout.list_view_box,R.id.listViewBoxTextView);
        arrayAdapter.addAll(stringArray);
        seeDonationsListView.setAdapter(arrayAdapter);
        //*********************************I AM SETTING THE TOTAL NO. OF DONATIONS TO TEXT VIEW*************************//
        totalDonationsTextView=findViewById(R.id.totalNoOfDonations);
        totalDonationsTextView.setText("The Total No Of Donations:"+arrayList.size());
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
}