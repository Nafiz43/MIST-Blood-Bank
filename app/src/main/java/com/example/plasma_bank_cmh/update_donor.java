package com.example.plasma_bank_cmh;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class update_donor extends AppCompatActivity {
    TextView keyTextView, dobTextView;
            //positiveDateTextView, negativeDateTextView;
    EditText nameEditText,addressEditText,contactEditText,eMailEditText,medicalHistoryEditText;
    Spinner bloodGroupSpinner;
    Button updateButton,edit, seeDonationsButton;
    String key;
    Button remove;

    String name_s;
    String address_s;
    String mail_s;
    String blood_group_s;
    String contact_s;
    String pos_s;
    String neg_s;
    String med_s;
    String dob_s;

    ListView donationDateListView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_donor);

        View b = findViewById(R.id.updateButton);
        b.setVisibility(View.GONE);




        //********************I COLLECT THE INFO SENT TO ME*****************************//
        final String[] stringArray=getIntent().getStringArrayExtra("rowContentsString");

        //I collect key
        key=stringArray[0];

        //SETTING UP THE FORM
        initializeTheViewsInUpdateActivity();
        insertDataIntoViews(stringArray);
        //Updating with the new data
        enableTheUpdateButton();

        edit=findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyTextView.setEnabled(true);
                nameEditText.setEnabled(true);
                bloodGroupSpinner.setEnabled(true);
                contactEditText.setEnabled(true);
                dobTextView.setEnabled(true);
                eMailEditText.setEnabled(true);
                medicalHistoryEditText.setEnabled(true);
               // positiveDateTextView.setEnabled(true);
               // negativeDateTextView.setEnabled(true);
                addressEditText.setEnabled(true);

                View b = findViewById(R.id.updateButton);
                b.setVisibility(View.VISIBLE);

                View b2= findViewById(R.id.edit);
                b2.setVisibility(View.GONE);





            }
        });

        //*****************************SETTING UP THE BUTTON FOR SEEING DONATIONS****************************//
        seeDonationsButton=findViewById(R.id.seeDonationsButton);
        seeDonationsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(update_donor.this,SeeDonationsActivity.class);
                intent.putExtra("Key",key);
                startActivity(intent);
            }
        });



        /*donationDateListView=findViewById(R.id.profileDonationDatesListView);
        String [] dateStringArray=new String[200];
        for(int i=0;i<200;i++){
            dateStringArray[i]=i+"";
        }
        ArrayAdapter arrayAdapter=new ArrayAdapter(update_donor.this,R.layout.list_view_box,R.id.listViewBoxTextView);
        arrayAdapter.addAll(dateStringArray);
        donationDateListView.setAdapter(arrayAdapter);*/


        remove=findViewById(R.id.remove);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(update_donor.this);
                myAlertDialogBuilder.setMessage("This donor will be completely removed from the system. Are you sure to remove this user?");
                myAlertDialogBuilder.setCancelable(true);
                myAlertDialogBuilder.setTitle("Confirmation");

                myAlertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
                                DatabaseReference myRefetence=firebaseDatabase.getReference("donor").child(key);
                                myRefetence.removeValue();


                                String toEmails = mail_s;
                                List<String> toEmailList = Arrays.asList(toEmails
                                        .split("\\s*,\\s*"));
                                new SendMailTask(update_donor.this).execute("cmhplasmatherapy@gmail.com",
                                        "CSE-0101", toEmailList, "Unenrollment from donor list", "You are no longer a donor in CMH Plasma Bank. For any inconvenience, you may email at this Email-ID:cmhplasmatherapy@gmail.com ");
//                                Toast.makeText(getApplicationContext(),"Recovery code is sent to your e-mail",Toast.LENGTH_LONG).show();
//                                Intent intent=new Intent(forgot_pass.this,recovery_code.class);
//                                startActivity(intent);

                                ///Next code will update the count//////////////////////
                                final int[] total = new int[1];
                                final DatabaseReference statisticsNodeReference=FirebaseDatabase.getInstance().getReference("statistics");
                                //I use listener to get the value of the count varaible
                                final int[] count = {0};
                                final int[] particularBloodCount={0};
                               /// address_s=address_s+","+area_s+","+district_s;

                                //if (mail_s.isEmpty())
                                {

                                   // final donor d1=new donor(name_s,address_s,phone_s,mail_s,dob_s,blood_group_s,pos_date_s,neg_date_s,med_history_s);

                                    statisticsNodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            //The code below increases the number of doners available in the system
                                            DataSnapshot dataSnapshotFromCount=dataSnapshot.child("count");
                                            String countString=dataSnapshotFromCount.getValue().toString();


                                            count[0] =Integer.parseInt(countString);
                                            //   Log.d("TAGX","Count value from the database is:"+count[0]);
                                            count[0]=count[0]-1;

                                            DatabaseReference myRef;
                                            myRef = FirebaseDatabase.getInstance().getReference("donor");

                                            String key= String.valueOf(count[0]);
                                          //  Toast.makeText(getApplicationContext(),"Total Donor in the system :"+key,Toast.LENGTH_LONG).show();

                                           // myRef.child(key).setValue(d1);
                                           // myRef.child(key).child("district").setValue(district_s);
                                           // myRef.child(key).child("area").setValue(area_s);

                                            statisticsNodeReference.child("count").setValue(count[0]+"");
                                            //The code below increases the blood grp count to which the doner belongs to
                                            DataSnapshot dataSnapshotFromParticularBlood=dataSnapshot.child(blood_group_s+"");
                                            countString=dataSnapshotFromParticularBlood.getValue().toString();
                                            count[0] =Integer.parseInt(countString);
                                            //Log.d("TAGX","Blood Count value from the database is:"+count[0]);
                                            count[0]=count[0]-1;
                                            statisticsNodeReference.child(blood_group_s).setValue(count[0]+"");
                                        }
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                                    //**********************************SUCCESSFULLY PUT DATA INTO STATISTICS*****************//


                                    Toast.makeText(getApplicationContext(),"Donor removed from the system.", Toast.LENGTH_LONG).show();

                                    View b=findViewById(R.id.remove);
                                    b.setVisibility(View.INVISIBLE);

                                    View b2=findViewById(R.id.edit);
                                    b2.setVisibility(View.INVISIBLE);

                                    View b3=findViewById(R.id.seeDonationsButton);
                                    b3.setVisibility(View.INVISIBLE);

                                    View b4=findViewById(R.id.updateButton);
                                    b4.setVisibility(View.INVISIBLE);

                                    //Intent intent=new Intent(update_donor.this,Home.class);
                                   // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                  //  startActivity(intent);

                                }
                                ///////////update count comp
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




    }

    void enableTheUpdateButton(){
        updateButton=findViewById(R.id.updateButton);
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(update_donor.this);
                myAlertDialogBuilder.setMessage("Are you sure to update user information?");
                myAlertDialogBuilder.setCancelable(true);

                myAlertDialogBuilder.setPositiveButton(
                        "Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {


                                String name= String.valueOf(nameEditText.getText());
                                String address= String.valueOf(addressEditText.getText());
                                String bloodGroup= bloodGroupSpinner.getSelectedItem().toString();

                                String contact= String.valueOf(contactEditText.getText());
                                String dob= String.valueOf(dobTextView.getText());
                                String email= String.valueOf(eMailEditText.getText());

                                String medHistory= String.valueOf(medicalHistoryEditText.getText());
                                String posDate= "2018/01/01";
                                String negDate= "2018/02/02";

                                FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();

                                DatabaseReference myRefetence=firebaseDatabase.getReference("donor").child(key);
                                donor mydonor=new donor(
                                        name,address,contact,
                                        email,dob,bloodGroup,
                                        posDate,negDate,medHistory);

                                myRefetence.child("name").setValue(name);
                                myRefetence.child("address").setValue(address);
                                myRefetence.child("contact").setValue(contact);
                                myRefetence.child("dob").setValue(dob);
                                myRefetence.child("mail").setValue(email);
                                myRefetence.child("blood_group").setValue(bloodGroup);
                                myRefetence.child("med_history").setValue(medHistory);
                                myRefetence.child("pos_date").setValue(posDate);
                                myRefetence.child("neg_date").setValue(negDate);

                                String toEmails = email;
                                List<String> toEmailList = Arrays.asList(toEmails
                                        .split("\\s*,\\s*"));
                                new SendMailTask(update_donor.this).execute("cmhplasmatherapy@gmail.com",
                                        "CSE-0101", toEmailList, "Information updated", "It seems some of your information in our system is updated by admin.\n" +
                                                "Your current informations in the system is given below;" +
                                                "Name : " +name+" , "+
                                                "Address :"+address+" , "+
                                                "Contact : "+bloodGroup+" , "+
                                                "Date of Birth : "+dob+" , "+
                                                "Blood Group : "+bloodGroup+" , "+
                                                "COVID-19 positive date : "+posDate+" , "+
                                                "COVID-19 negative date : "+negDate+" , "+
                                                "Medical history : "+medHistory+" , "+
                                                "For any inconvenience, you may email at this Email-ID : cmhplasmatherapy@gmail.com ");

                                keyTextView.setEnabled(false);
                                nameEditText.setEnabled(false);
                                bloodGroupSpinner.setEnabled(false);
                                contactEditText.setEnabled(false);
                                dobTextView.setEnabled(false);
                                eMailEditText.setEnabled(false);
                                medicalHistoryEditText.setEnabled(false);
                               // positiveDateTextView.setEnabled(false);
                               // negativeDateTextView.setEnabled(false);
                                addressEditText.setEnabled(false);

                                View b1=findViewById(R.id.updateButton);
                                b1.setVisibility(View.GONE);
                                View b2= findViewById(R.id.edit);
                                b2.setVisibility(View.VISIBLE);
                                Toast.makeText(getApplicationContext(),"Donor information updated in the system.",Toast.LENGTH_LONG).show();
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

    }

    private void insertDataIntoViews(String[] stringArray) {
        //I insert the strings into the views
        keyTextView.append(stringArray[0]);
        nameEditText.setText(stringArray[1]);
        name_s=stringArray[1];
        addressEditText.setText(stringArray[2]);
        address_s=stringArray[2];

        HashMap<String, Integer> bloodToIndex = new HashMap<String, Integer>();
        bloodToIndex.put("None", 0);
        bloodToIndex.put("A+", 1);bloodToIndex.put("A-", 2);bloodToIndex.put("B+", 3);bloodToIndex.put("B-", 4);
        bloodToIndex.put("O+", 5);bloodToIndex.put("O-", 6);bloodToIndex.put("AB+", 7);bloodToIndex.put("AB-", 8);
        bloodGroupSpinner.setSelection(bloodToIndex.get(stringArray[3]));
        blood_group_s=stringArray[3];
        contactEditText.setText(stringArray[4]);
        contact_s=stringArray[4];
        dobTextView.setText(stringArray[5]);
        dob_s=stringArray[5];
        eMailEditText.setText(stringArray[6]);
        mail_s=stringArray[6];


        medicalHistoryEditText.setText(stringArray[7]);
        med_s=stringArray[7];
       // positiveDateTextView.setText(stringArray[8]);
        pos_s=stringArray[8];
        //negativeDateTextView.setText(stringArray[9]);
        neg_s=stringArray[9];
    }

    void initializeTheViewsInUpdateActivity(){
        keyTextView=findViewById(R.id.keyTextView);

        nameEditText=findViewById(R.id.nameEditText);
        addressEditText=findViewById(R.id.addressEditText);
        bloodGroupSpinner =findViewById(R.id.updateBloodGroupSpinner);

        contactEditText=findViewById(R.id.contactEditText);
        dobTextView =findViewById(R.id.dobEditText);
        eMailEditText=findViewById(R.id.eMailEditText);

        medicalHistoryEditText=findViewById(R.id.medicalHistoryEditText);
       // positiveDateTextView =findViewById(R.id.posDateEditText);
       // negativeDateTextView =findViewById(R.id.negDateEditText);

        keyTextView.setEnabled(false);
        nameEditText.setEnabled(false);
        bloodGroupSpinner.setEnabled(false);
        contactEditText.setEnabled(false);
        dobTextView.setEnabled(false);
        eMailEditText.setEnabled(false);
        medicalHistoryEditText.setEnabled(false);
       // positiveDateTextView.setEnabled(false);
       // negativeDateTextView.setEnabled(false);
        addressEditText.setEnabled(false);

        //*********************************SETTING UP THE BIRTH DATE WITH DATE PICKER************************************//
        final DatePickerDialog.OnDateSetListener birthDateTextViewListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);

                String date = year + "/" + month + "/" + day;

                String year_s=String.valueOf(year);
                String month_s=String.valueOf(month);
                String day_s=String.valueOf(day);

                if (day_s.length()==1)
                {
                    day_s="0"+day_s;
                }
                if (month_s.length()==1)
                {
                    month_s="0"+month_s;
                }



                String date_s=year_s+"/"+month_s+"/"+day_s;

                dobTextView.setText(date_s);
            }
        };
        dobTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        update_donor.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        birthDateTextViewListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        //*********************************I HAVE SET UP THE BIRTH DATE WITH DATE PICKER************************************//

//        //*********************************SETTING UP THE POS DATE WITH DATE PICKER************************************//
//        final DatePickerDialog.OnDateSetListener posDateTextViewListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
//
//                String date = year + "/" + month + "/" + day;
//
//                String year_s=String.valueOf(year);
//                String month_s=String.valueOf(month);
//                String day_s=String.valueOf(day);
//
//                if (day_s.length()==1)
//                {
//                    day_s="0"+day_s;
//                }
//                if (month_s.length()==1)
//                {
//                    month_s="0"+month_s;
//                }
//
//
//                String date_s=year_s+"/"+month_s+"/"+day_s;
//                positiveDateTextView.setText(date_s);
//            }
//        };
//        positiveDateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        update_donor.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        posDateTextViewListener,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//        //*********************************I HAVE SET UP THE POS DATE WITH DATE PICKER************************************//

//        //*********************************SETTING UP THE NEG DATE WITH DATE PICKER************************************//
//        final DatePickerDialog.OnDateSetListener negDateTextViewListener = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
//
//                String date = year + "/" + month + "/" + day;
//                String year_s=String.valueOf(year);
//                String month_s=String.valueOf(month);
//                String day_s=String.valueOf(day);
//                if (day_s.length()==1)
//                {
//                    day_s="0"+day_s;
//                }
//                if (month_s.length()==1)
//                {
//                    month_s="0"+month_s;
//                }
//                String date_s=year_s+"/"+month_s+"/"+day_s;
//
//                negativeDateTextView.setText(date_s);
//            }
//        };
//        negativeDateTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        update_donor.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        negDateTextViewListener,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//        //*********************************I HAVE SET UP THE NEG DATE WITH DATE PICKER************************************//


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
        Intent intent=new Intent(getApplicationContext(),donorlist.class);
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}