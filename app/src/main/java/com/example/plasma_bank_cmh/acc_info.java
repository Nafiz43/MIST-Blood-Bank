package com.example.plasma_bank_cmh;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class acc_info extends AppCompatActivity {
    Button edit,save;
    EditText name,address,contact,mail,dob,role;

    public static String name_s;
    public static String address_s;
    public static String contact_s;
    public static String mail_s;
    public static String password_s;
    public static String description_s;
    public static String dob_s;
    public static String role_s;
    public static String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acc_info);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        contact=findViewById(R.id.contact);
        mail=findViewById(R.id.mail);
        dob=findViewById(R.id.dob);
        role=findViewById(R.id.role);
        edit=findViewById(R.id.edit);
        save=findViewById(R.id.save);

        SharedPreferences sharedPreferences=getSharedPreferences("rememberFile",MODE_PRIVATE);

         name_s =sharedPreferences.getString("name","");
         address_s=sharedPreferences.getString("address","");
         contact_s=sharedPreferences.getString("contact","");
         mail_s=sharedPreferences.getString("mail","");
         dob_s=sharedPreferences.getString("dob","");
         role_s=sharedPreferences.getString("role","");
         password_s=sharedPreferences.getString("password","");
        key=sharedPreferences.getString("key","");

        name.setText(name_s);
        address.setText(address_s);
        contact.setText(contact_s);
        mail.setText(mail_s);
        dob.setText(dob_s);
        role.setText(role_s);

        View b=findViewById(R.id.save);
        b.setVisibility(View.GONE);





        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               name.setEnabled(true);
               address.setEnabled(true);
               contact.setEnabled(true);
               //mail.setEnabled(true);
              // dob.setEnabled(true);
              // role.setEnabled(true);
               save.setEnabled(true);
                View b=findViewById(R.id.save);
                b.setVisibility(View.VISIBLE);

                View b2=findViewById(R.id.edit);
                b2.setVisibility(View.GONE);

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(acc_info.this);
                myAlertDialogBuilder.setMessage("Are you sure to update your information?");
                myAlertDialogBuilder.setCancelable(true);
                myAlertDialogBuilder.setTitle("Confirmation");

                myAlertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {




                                name_s=name.getText().toString();
                                address_s=address.getText().toString();
                                contact_s=contact.getText().toString();
                                mail_s=mail.getText().toString();
                                dob_s=dob.getText().toString();
                                role_s=role.getText().toString();
                                String dum="approved";
                                //public static String key


                                SharedPreferences sharedPreferences3=getSharedPreferences("rememberFile",MODE_PRIVATE);
                                SharedPreferences.Editor editor2=sharedPreferences3.edit();
                                editor2.putString("name",name_s);
                                editor2.putString("address",address_s);
                                editor2.putString("contact",contact_s);
                                //editor2.putString("mail",mail_s);



                                editor2.apply();


                                user s2=new user(name_s,address_s,contact_s,mail_s,password_s,dob_s,role_s,description_s,dum);


                                    FirebaseDatabase database;
                                    DatabaseReference myRef;


                                    database = FirebaseDatabase.getInstance();
                                    myRef = FirebaseDatabase.getInstance().getReference("user");


                                    myRef.child(key).child("name").setValue(name_s);
                                    myRef.child(key).child("address").setValue(address_s);
                                    myRef.child(key).child("contact").setValue(contact_s);
                                  // myRef.child(key).child("mail").setValue(mail_s);



                                Toast.makeText(getApplicationContext(),"Information successfully updated", Toast.LENGTH_LONG).show();

                                name.setEnabled(false);
                                address.setEnabled(false);
                                contact.setEnabled(false);
                                //mail.setEnabled(false);
                                //dob.setEnabled(false);
                                // role.setEnabled(false);
                                save.setEnabled(false);

                                View b=findViewById(R.id.save);
                                b.setVisibility(View.GONE);

                                View b2=findViewById(R.id.edit);
                                b2.setVisibility(View.VISIBLE);
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


       // Toast.makeText(getApplicationContext(),acc_info.email+"   "+acc_info.dob, Toast.LENGTH_LONG).show();

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