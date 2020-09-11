package com.example.plasma_bank_cmh;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class statistics extends AppCompatActivity {
    TextView o,o_,a,a_,b,b_,ab,ab_,total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        o=findViewById(R.id.bloodGroupCountOp);
        o_=findViewById(R.id.bloodGroupCountOn);
        a=findViewById(R.id.bloodGroupCountAp);
        a_=findViewById(R.id.bloodGroupCountAn);
        b=findViewById(R.id.bloodGroupCountBp);
        b_=findViewById(R.id.bloodGroupCountBn);
        ab=findViewById(R.id.bloodGroupCountABp);
        ab_=findViewById(R.id.bloodGroupCountABn);
        total=findViewById(R.id.total);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("statistics");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                //blood_group bd_grp=snapshot.getValue(blood_group.class);


//                o.setText("O+                            "+snapshot.child("O+").getValue());
//                o_.setText("O-                            "+snapshot.child("O-").getValue());
//                a.setText("A+                           "+snapshot.child("A+").getValue());
//                a_.setText("A-                            "+snapshot.child("A-").getValue());
//                b.setText("B+                           "+snapshot.child("B+").getValue());
//                b_.setText("B-                            "+snapshot.child("B-").getValue());
//                ab.setText("AB+                        "+snapshot.child("AB+").getValue());
//                ab_.setText("AB-                         "+snapshot.child("AB-").getValue());
//                total.setText("Total Donor:"+snapshot.child("count").getValue());

                o.setText(""+snapshot.child("O+").getValue());
                o_.setText(""+snapshot.child("O-").getValue());
                a.setText(""+snapshot.child("A+").getValue());
                a_.setText(""+snapshot.child("A-").getValue());
                b.setText(""+snapshot.child("B+").getValue());
                b_.setText(""+snapshot.child("B-").getValue());
                ab.setText(""+snapshot.child("AB+").getValue());
                ab_.setText(""+snapshot.child("AB-").getValue());
                total.setText("Total Donor Available: "+snapshot.child("count").getValue());

                BarChart barChart = (BarChart) findViewById(R.id.barchart);
             //   barChart.getRendererLeftYAxis().setValueFormatter(new IntegerFormatter());

                ArrayList<BarEntry> entries = new ArrayList<>();
               // barChart.getYAxis().setValueFormatter(new IntegerFormatter());

                entries.add(new BarEntry(Integer.parseInt(snapshot.child("A+").getValue().toString()), 0));
                entries.add(new BarEntry(Integer.parseInt(snapshot.child("A-").getValue().toString()), 1));
                entries.add(new BarEntry(Integer.parseInt(snapshot.child("B+").getValue().toString()), 2));
                entries.add(new BarEntry(Integer.parseInt(snapshot.child("B+").getValue().toString()), 3));

                entries.add(new BarEntry(Integer.parseInt(snapshot.child("O+").getValue().toString()), 4));
                entries.add(new BarEntry(Integer.parseInt(snapshot.child("O-").getValue().toString()), 5));

                entries.add(new BarEntry(Integer.parseInt(snapshot.child("AB+").getValue().toString()), 6));
                entries.add(new BarEntry(Integer.parseInt(snapshot.child("AB-").getValue().toString()), 7));
//

//                entries.add(new BarEntry(5f, 2));
//                entries.add(new BarEntry(20f, 3));
//                entries.add(new BarEntry(15f, 4));
//                entries.add(new BarEntry(19f, 5));

                BarDataSet bardataset = new BarDataSet(entries, "Cells");

                ArrayList<String> labels = new ArrayList<String>();
                labels.add("A+");
                labels.add("A-");
                labels.add("B+");
                labels.add("B-");
                labels.add("O+");
                labels.add("O-");
                labels.add("AB+");
                labels.add("AB-");

                BarData data = new BarData(labels, bardataset);
                barChart.setData(data); // set the data and list of labels into chart
                barChart.setDescription("Blood-group wise available donors");  // set the description
                bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
                barChart.animateY(5000);







                //    o.setText(snapshot.getValue("O+").toString());




            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



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