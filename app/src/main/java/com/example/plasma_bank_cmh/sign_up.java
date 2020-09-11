package com.example.plasma_bank_cmh;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class sign_up extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String name_s;
    String address_s;
    String phone_s;
    String dob_s;
    String role_s;
    String mail_s;
    String password_s;
    String confirm_passwword_s;
    String description_s;

    Button sign_up;
    Spinner role;
    EditText name,address,phone,mail,dob,password,confirm_password,description;
    DatePickerDialog.OnDateSetListener mDateSetListener_dob_date;
    //private AdapterView adapterView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.contact);
        mail=findViewById(R.id.mail);
        description=findViewById(R.id.description);

        dob=findViewById(R.id.dob);
        password=findViewById(R.id.password);
        confirm_password=findViewById(R.id.confirm_password);
        description=findViewById(R.id.description);

        sign_up=findViewById(R.id.sign_up_main);

        //*********Spinner Code Starts**********//

        role=findViewById(R.id.role);
        // Spinner click listener
        role.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("Staff");
        categories.add("Admin");
        categories.add("Doctor");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        role.setAdapter(dataAdapter);

        //*********Spinner Code Ends**********//

        //*******Date picker code starts********//

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        sign_up.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener_dob_date,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener_dob_date= new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = day + "/" + month + "/" + year;
                dob.setText(date);
            }
        };

        //******Date picker code ends*********///





        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_s=name.getText().toString();
                address_s=address.getText().toString();
                phone_s=phone.getText().toString();
                mail_s=mail.getText().toString();
                password_s=password.getText().toString();
                confirm_passwword_s=confirm_password.getText().toString();


                dob_s=dob.getText().toString();
                description_s=description.getText().toString();
                if (name_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your name", Toast.LENGTH_LONG).show();
                }
                else if(address_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter your address", Toast.LENGTH_LONG).show();
                }
                else if(phone_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter phone number", Toast.LENGTH_LONG).show();
                }
                else if(!(phone_s.length()==11))
                {
                    Toast.makeText(getApplicationContext(),"Contact number must be 11 digit.", Toast.LENGTH_LONG).show();
                }
                else if (mail_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter e-mail id", Toast.LENGTH_LONG).show();
                }
                else if (!isValidEmail(mail_s))
                {
                    Toast.makeText(getApplicationContext(),"Enter a valid e-mail id", Toast.LENGTH_LONG).show();
                }
                else if(password_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter password", Toast.LENGTH_LONG).show();
                }
                else if(confirm_passwword_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Confirm your password", Toast.LENGTH_LONG).show();
                }
                else if(dob_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter date of birth", Toast.LENGTH_LONG).show();
                }
//                else if (description_s.isEmpty())
//                {
//
//                }
                else if(!password_s.equals(confirm_passwword_s))
                {
                    Toast.makeText(getApplicationContext(),"password and confirm password did not match", Toast.LENGTH_LONG).show();
                }
                else
                {
                    FirebaseDatabase database;
                    final DatabaseReference myRef;
                    //String nam=name.getText().toString();
                    //String ag=age.getText().toString();

                    final user s1=new user(name_s,address_s,phone_s,mail_s,password_s,dob_s,role_s,"des :"+description_s,"pending");

                    database = FirebaseDatabase.getInstance();
                    myRef = FirebaseDatabase.getInstance().getReference("user");

                   // String key=myRef.push().getKey();

                    //myRef.setValue(s1);
                    String str = mail_s;
                    final String strNew = str.replace(".", "");



                    DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference("user");
                    rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            if (!snapshot.hasChild(strNew)) {

                                myRef.child(strNew).setValue(s1);
                                Toast.makeText(getApplicationContext(),"Successfully signed up",Toast.LENGTH_LONG).show();

                                Intent intent=new Intent(sign_up.this,sign_up_confirm.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);

                            }
                            else
                            {
                                Toast.makeText(getApplicationContext(),"Same email already used in the system. Use another email",Toast.LENGTH_LONG).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }



            }
        });







    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        role_s=item;

        // Showing selected spinner item
        // Toast.makeText(adapterView.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
            Intent intent=new Intent(getApplicationContext(),MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }
}