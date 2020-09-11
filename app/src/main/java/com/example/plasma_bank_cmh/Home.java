package com.example.plasma_bank_cmh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public  static String role;
    Button enroll,list,pending,stat;
    //THESE ARE JUST THE NECCESSARY VARIABLES
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    NavigationView navigationView;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pending=findViewById(R.id.pending);
        stat=findViewById(R.id.stat);

        SharedPreferences sharedPreferences=getSharedPreferences("rememberFile",MODE_PRIVATE);
        String role=sharedPreferences.getString("role","");




        if (role.equals("Staff")  || role.equals("Doctor"))
        {
            //+pending.setEnabled(false);
            View b = findViewById(R.id.pending);
            b.setVisibility(View.GONE);

        }

        //THESE LINES OF CODE JUST SET UP THE NAVIGATION DRAWER WITH ITS FUNCTIONALITY
        drawerLayout=findViewById(R.id.drawerLayout);
        actionBarDrawerToggle=new ActionBarDrawerToggle(this,drawerLayout,R.string.toggle_open,R.string.toggle_close);
        navigationView=findViewById(R.id.navigationView);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(this);
        //NAVIGATION DRAWER SET UP

        enroll=findViewById(R.id.enroll_id);
        list=findViewById(R.id.list);


        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getApplicationContext(),"Clicked on sign up", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Home.this,enroll.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"Clicked on sign up", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Home.this,donorlist.class);
                 //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                }
        });
        stat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Toast.makeText(getApplicationContext(),"Clicked on sign up", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Home.this,statistics.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });

        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  Toast.makeText(getApplicationContext(),"Clicked on sign up", Toast.LENGTH_LONG).show();
                Intent intent=new Intent(Home.this,pending_req.class);
                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
            }
        });



    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if(menuItem.getItemId()==R.id.message){
            Intent intent=new Intent(Home.this,message.class);
           // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else if(menuItem.getItemId()==R.id.aboutus){
            Intent intent=new Intent(Home.this,about_us.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        }
        else if(menuItem.getItemId()==R.id.acc_info)
        {
            Intent intent=new Intent(Home.this,acc_info.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.change_pass)
        {
            Intent intent=new Intent(Home.this,password_change.class);
            //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
        else if(menuItem.getItemId()==R.id.log_out)
        {

            AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(Home.this);
            myAlertDialogBuilder.setMessage("Are you sure to log out?");
            myAlertDialogBuilder.setCancelable(true);
            myAlertDialogBuilder.setTitle("Confirmation");

            myAlertDialogBuilder.setPositiveButton(
                    "Yes",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //The app will store the value of remember as false and hence the user is not remembered anymore
                            SharedPreferences sharedPreferences1=getSharedPreferences("rememberFile",MODE_PRIVATE);
                            SharedPreferences.Editor editor=sharedPreferences1.edit();
                            editor.putString("remember","false");
                            editor.apply();
                            //The user is successfully forgotten
                            Intent intent=new Intent(Home.this,MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

                            startActivity(intent);

                        }
                    });

            myAlertDialogBuilder.setNegativeButton(
                    "No",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                        }
                    });



            AlertDialog alertDialog = myAlertDialogBuilder.create();
            alertDialog.show();






        }

        return  false;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Confirmation");
        alertDialogBuilder.setMessage("Are you sure you want to exit ?");
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {


            }
        });
        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Do nothing
                Intent intent=new Intent(Home.this,MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });

        alertDialogBuilder.show();
    }
}