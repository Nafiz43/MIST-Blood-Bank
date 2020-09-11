package com.example.plasma_bank_cmh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

import java.util.Arrays;
import java.util.List;

public class pending_approval extends AppCompatActivity {

    String name_s;
    String address_s;
    String contact_s;
    String dob_s;
    String role_s;
    String mail_s;
    String password_s;
    String confirm_passwword_s;
    String description_s;
    public static String key;

    Button approve,reject;
    EditText name,address,phone,mail,dob,description,role;
    //DatePickerDialog.OnDateSetListener mDateSetListener_dob_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_approval);

        String[] stringArray=getIntent().getStringArrayExtra("rowContentsString");

        //I collect key
        key=stringArray[2];

        String str = key;
        final String strNew = str.replace(".", "");
        key=strNew;

        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.contact);
        mail=findViewById(R.id.mail);
        description=findViewById(R.id.description);
        dob=findViewById(R.id.dob);
        approve=findViewById(R.id.approve);
        role=findViewById(R.id.role);
        reject=findViewById(R.id.reject);


        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("user");
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(strNew)) {

                    user u1=snapshot.child(strNew).getValue(user.class);

                        name_s=u1.name;
                        mail_s=u1.mail;
                        address_s=u1.address;
                        dob_s=u1.dob;
                        contact_s=u1.contact;
                        role_s=u1.role;
                        description_s=u1.description;
                        password_s=u1.password;


                    name.setText(name_s);
                    address.setText(address_s);
                    phone.setText(contact_s);
                    mail.setText(mail_s);
                    description.setText(description_s);
                    dob.setText(dob_s);
                    role.setText(role_s);

                    name.setEnabled(false);
                    address.setEnabled(false);
                    phone.setEnabled(false);
                    mail.setEnabled(false);
                    description.setEnabled(false);
                    dob.setEnabled(false);
                    role.setEnabled(false);
                //    Toast.makeText(getApplicationContext(),key, Toast.LENGTH_LONG).show();

                }
                else
                {
                    Toast.makeText(getApplicationContext(),"not found",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(pending_approval.this);
                myAlertDialogBuilder.setMessage("Are you sure to approve this user?");
                myAlertDialogBuilder.setCancelable(true);
                myAlertDialogBuilder.setTitle("Confirmation");

                myAlertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference myRefetence=firebaseDatabase.getReference("user").child(key);
                                user s1=new user(name_s,address_s,contact_s,mail_s,password_s,dob_s,role_s,description_s,"approved");

                                myRefetence.setValue(s1);
                                //  Log.d("TAG1","DONE UPDATING");

                                String toEmails = mail_s;
                                List<String> toEmailList = Arrays.asList(toEmails
                                        .split("\\s*,\\s*"));
                                new SendMailTask(pending_approval.this).execute("cmhplasmatherapy@gmail.com",
                                        "CSE-0101", toEmailList, "Account Approval", "Congratulations! Your account is approved. Now you have access to CMH Plasma Bank. Use your given credentials to log in.");

                                Toast.makeText(getApplicationContext(),"User approved", Toast.LENGTH_LONG).show();

                                View b=findViewById(R.id.approve);
                                b.setVisibility(View.INVISIBLE);

                                View b2=findViewById(R.id.reject);
                                b2.setVisibility(View.INVISIBLE);
                                //Intent intent=new Intent(pending_approval.this,pending_req.class);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                               // startActivity(intent);

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
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(pending_approval.this);
                myAlertDialogBuilder.setMessage("Are you sure to remove this user?");
                myAlertDialogBuilder.setCancelable(true);
                myAlertDialogBuilder.setTitle("Confirmation");

                myAlertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference myRefetence=firebaseDatabase.getReference("user").child(key);
                                myRefetence.removeValue();

                                String toEmails = mail_s;
                                List<String> toEmailList = Arrays.asList(toEmails
                                        .split("\\s*,\\s*"));
                                new SendMailTask(pending_approval.this).execute("cmhplasmatherapy@gmail.com",
                                        "CSE-0101", toEmailList, "Request Rejection", "Your account request is rejected by admin. For any inconvenience, you may contact using E-mail: cmhplasmatherapy@gmail.com");


                                Toast.makeText(getApplicationContext(),"Request rejected ", Toast.LENGTH_LONG).show();

                                View b=findViewById(R.id.approve);
                                b.setVisibility(View.INVISIBLE);

                                View b2=findViewById(R.id.reject);
                                b2.setVisibility(View.INVISIBLE);

                                //Intent intent=new Intent(pending_approval.this,pending_req.class);
                                // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                              //  startActivity(intent);

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
        });



      //  Toast.makeText(getApplicationContext(),name_s+""+address_s, Toast.LENGTH_LONG).show();







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