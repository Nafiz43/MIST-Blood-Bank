package com.example.plasma_bank_cmh;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class enroll extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String name_s;
    String address_s;
    String phone_s;
    String dob_s;
    String blood_group_s;
    String pos_date_s;
    String neg_date_s;
    String med_history_s;
    String mail_s;
    String district_s;
    String area_s;

    Button enroll;
    Spinner blood_group,area,district;
    EditText name,address,phone,dob,pos_date,neg_date,med_history,mail;
    DatePickerDialog.OnDateSetListener mDateSetListener_pos_date;
    DatePickerDialog.OnDateSetListener mDateSetListener_neg_date;
    DatePickerDialog.OnDateSetListener mDateSetListener_dob_date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enroll);



        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        phone=findViewById(R.id.contact);
        med_history=findViewById(R.id.med_history);

        dob=findViewById(R.id.dob);
//        pos_date=findViewById(R.id.pos_date);
//        neg_date=findViewById(R.id.neg_date);
        mail=findViewById(R.id.mail);
        enroll=findViewById(R.id.sign_up);




        //*********Spinner Code Starts for blood grp**********//

        blood_group=findViewById(R.id.blood_group);
        district=findViewById(R.id.district);
        area=findViewById(R.id.area);


        // Spinner click listener
        district.setOnItemSelectedListener(this);


//        //Date picker starts
//
//        pos_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        enroll.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        mDateSetListener_pos_date,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//        mDateSetListener_pos_date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
//
//               String month_s=String.valueOf(month);
//               String day_s=String.valueOf(day);
//               if (month_s.length()==1)
//               {
//                   month_s="0"+month_s;
//               }
//               if (day_s.length()==1)
//               {
//                   day_s="0"+day_s;
//               }
//                String date = year + "/" + month_s + "/" + day_s;
//
//                pos_date.setText(date);
//            }
//        };
//
//        neg_date.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar cal = Calendar.getInstance();
//                int year = cal.get(Calendar.YEAR);
//                int month = cal.get(Calendar.MONTH);
//                int day = cal.get(Calendar.DAY_OF_MONTH);
//
//                DatePickerDialog dialog = new DatePickerDialog(
//                        enroll.this,
//                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
//                        mDateSetListener_neg_date,
//                        year,month,day);
//                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//                dialog.show();
//            }
//        });
//
//        mDateSetListener_neg_date = new DatePickerDialog.OnDateSetListener() {
//            @Override
//            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
//                month = month + 1;
//                // Log.d(String.valueOf(enroll), "onDateSet: mm/dd/yyy: " + month + "/" + day + "/" + year);
//                String month_s=String.valueOf(month);
//                String day_s=String.valueOf(day);
//                if (month_s.length()==1)
//                {
//                    month_s="0"+month_s;
//                }
//                if (day_s.length()==1)
//                {
//                    day_s="0"+day_s;
//                }
//                String date = year + "/" + month_s + "/" + day_s;
//                neg_date.setText(date);
//            }
//        };

        dob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        enroll.this,
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

                String month_s=String.valueOf(month);
                String day_s=String.valueOf(day);
                if (month_s.length()==1)
                {
                    month_s="0"+month_s;
                }
                if (day_s.length()==1)
                {
                    day_s="0"+day_s;
                }
                String date = year + "/" + month_s + "/" + day_s;
                dob.setText(date);
            }
        };



        //Date picker ends

        enroll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                name_s=name.getText().toString();
                address_s=address.getText().toString();
                phone_s=phone.getText().toString();
                med_history_s=med_history.getText().toString();
                dob_s=dob.getText().toString();
//                pos_date_s=pos_date.getText().toString();
//                neg_date_s=neg_date.getText().toString();
                mail_s=mail.getText().toString();
                blood_group_s=blood_group.getSelectedItem().toString();
                area_s=area.getSelectedItem().toString();
              //  Toast.makeText(getApplicationContext(),area_s+" "+district_s+blood_group_s, Toast.LENGTH_LONG).show();


                if (name_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter name", Toast.LENGTH_LONG).show();
                }
                else if(address_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter address",Toast.LENGTH_LONG).show();
                }
                else if (district_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Select district",Toast.LENGTH_LONG).show();
                }
                else if(area_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Select area",Toast.LENGTH_LONG).show();
                }
                else if(phone_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter phone number",Toast.LENGTH_LONG).show();
                }


                else if(dob_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Enter date-of-birth",Toast.LENGTH_LONG).show();
                }
//                else if(pos_date_s .isEmpty())
//                {
//                    Toast.makeText(getApplicationContext(),"Enter COVID-19 positive date",Toast.LENGTH_LONG).show();
//                }
//                else if(neg_date_s.isEmpty())
//                {
//                    Toast.makeText(getApplicationContext(),"Enter COVID-19 negative date",Toast.LENGTH_LONG).show();
//                }
                else if (blood_group_s.isEmpty())
                {
                    Toast.makeText(getApplicationContext(),"Select a blood group",Toast.LENGTH_LONG).show();
                }
                else
                {
                    pos_date_s="2018/01/01";
                    neg_date_s="2018/02/01";
                    final int[] reg_count = {0};
                    final int[] total = new int[1];
                    final DatabaseReference statisticsNodeReference=FirebaseDatabase.getInstance().getReference("statistics");
                    //I use listener to get the value of the count varaible
                    final int[] count = {0};
                    final int[] particularBloodCount={0};
                    address_s=address_s+","+area_s+","+district_s;

                    if (mail_s.isEmpty())
                    {

                        final donor d1=new donor(name_s,address_s,phone_s,mail_s,dob_s,blood_group_s,pos_date_s,neg_date_s,med_history_s);

                        statisticsNodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                //The code below increases the number of doners available in the system
                                DataSnapshot dataSnapshotFromCount=dataSnapshot.child("count");
                                String countString=dataSnapshotFromCount.getValue().toString();


                                reg_count[0] =Integer.parseInt(dataSnapshot.child("reg_count").getValue().toString());
                                reg_count[0]=reg_count[0]+1;

                                count[0] =Integer.parseInt(countString);
                                //   Log.d("TAGX","Count value from the database is:"+count[0]);
                                count[0]=count[0]+1;

                                DatabaseReference myRef;
                                myRef = FirebaseDatabase.getInstance().getReference("donor");

                                String key= String.valueOf(reg_count[0]);
                                Toast.makeText(getApplicationContext(),"Total Donor in the system :"+count[0],Toast.LENGTH_LONG).show();

                                myRef.child(key).setValue(d1);
                                myRef.child(key).child("district").setValue(district_s);
                                myRef.child(key).child("area").setValue(area_s);

                                statisticsNodeReference.child("count").setValue(count[0]+"");
                                statisticsNodeReference.child("reg_count").setValue(reg_count[0]+"");
                                //The code below increases the blood grp count to which the doner belongs to
                                DataSnapshot dataSnapshotFromParticularBlood=dataSnapshot.child(d1.blood_group+"");
                                countString=dataSnapshotFromParticularBlood.getValue().toString();
                                count[0] =Integer.parseInt(countString);
                                //Log.d("TAGX","Blood Count value from the database is:"+count[0]);
                                count[0]=count[0]+1;
                                statisticsNodeReference.child(d1.blood_group).setValue(count[0]+"");
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        //**********************************SUCCESSFULLY PUT DATA INTO STATISTICS*****************//


                        Toast.makeText(getApplicationContext(),"Doner Successfully added to the system",Toast.LENGTH_LONG).show();


                        Intent intent=new Intent(enroll.this,Home.class);
                        startActivity(intent);

                    }
                    else
                    {
                        if (isValidEmail(mail_s))
                        {
                            String toEmails = mail_s;
                            List<String> toEmailList = Arrays.asList(toEmails
                                    .split("\\s*,\\s*"));
                            new SendMailTask(enroll.this).execute("cmhplasmatherapy@gmail.com",
                                    "CSE-0101", toEmailList, "Donor Registration", "Congratulations! You have been registered as a donor in MIST Blood Bank. For any need, you will be contacted. Thanks for becoming a donor.");
                            final donor d1=new donor(name_s,address_s,phone_s,mail_s,dob_s,blood_group_s,pos_date_s,neg_date_s,med_history_s);

                            statisticsNodeReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    //The code below increases the number of doners available in the system
                                    DataSnapshot dataSnapshotFromCount=dataSnapshot.child("count");
                                    String countString=dataSnapshotFromCount.getValue().toString();


                                    count[0] =Integer.parseInt(countString);
                                    //   Log.d("TAGX","Count value from the database is:"+count[0]);
                                    count[0]=count[0]+1;

                                    reg_count[0] =Integer.parseInt(dataSnapshot.child("reg_count").getValue().toString());
                                    reg_count[0]=reg_count[0]+1;

                                    DatabaseReference myRef;
                                    myRef = FirebaseDatabase.getInstance().getReference("donor");

                                    String key= String.valueOf(reg_count[0]);
                                    Toast.makeText(getApplicationContext(),"Total Donor in the system :"+count[0],Toast.LENGTH_LONG).show();

                                    myRef.child(key).setValue(d1);
                                    myRef.child(key).child("district").setValue(district_s);
                                    myRef.child(key).child("area").setValue(area_s);

                                    statisticsNodeReference.child("count").setValue(count[0]+"");
                                    statisticsNodeReference.child("reg_count").setValue(reg_count[0]+"");
                                    //The code below increases the blood grp count to which the doner belongs to
                                    DataSnapshot dataSnapshotFromParticularBlood=dataSnapshot.child(d1.blood_group+"");
                                    countString=dataSnapshotFromParticularBlood.getValue().toString();
                                    count[0] =Integer.parseInt(countString);
                                    //Log.d("TAGX","Blood Count value from the database is:"+count[0]);
                                    count[0]=count[0]+1;
                                    statisticsNodeReference.child(d1.blood_group).setValue(count[0]+"");
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                            //**********************************SUCCESSFULLY PUT DATA INTO STATISTICS*****************//


                            Toast.makeText(getApplicationContext(),"Doner Successfully added to the system",Toast.LENGTH_LONG).show();


                            Intent intent=new Intent(enroll.this,Home.class);
                            startActivity(intent);
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"E-mail is not valid. If not applicable, you may keep it blank.",Toast.LENGTH_LONG).show();

                        }
                    }

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
        district_s=item;

        if(item.equals("Dhaka"))
        {
          //  Toast.makeText(getApplicationContext(),"Dhaka selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Dhaka, android.R.layout.simple_spinner_item);
                // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                // Apply the adapter to the spinner
            area.setAdapter(adapter);

        }
        else if(district_s.equals("Faridpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Faridpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Bagerhat"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Bagerhat, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Bandarban"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Bandarban, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Barguna"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Barguna, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Barisal"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Barisal, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Bhola"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Bhola, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Bogra"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Bogra, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Brahmanbaria"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Brahmanbaria, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Chandpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Chandpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Chapainawabganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Chapainawabganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Chittagong"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Chittagong, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Chuadanga"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Chuadanga, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Comilla"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Comilla, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("CoxsBazar"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.CoxsBazar, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Dinajpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Dinajpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Feni"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Feni, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Gaibandha"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Gaibandha, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Gazipur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Gazipur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Gopalganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Gopalganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Habiganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Habiganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Jamalpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Jamalpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Jessore"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Jessore, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Jhalokati"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Jhalokati, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Jhenaidah"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Jhenaidah, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Joypurhat"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Joypurhat, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Khagrachhari"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Khagrachhari, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Khulna"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Khulna, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Kishoreganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Kishoreganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Kurigram"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Kurigram, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Kushtia"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Kushtia, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Lakshmipur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Lakshmipur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Lalmonirhat"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Lalmonirhat, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Madaripur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Madaripur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Magura"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Magura, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Manikganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Manikganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Meherpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Meherpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Moulvibazar"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Moulvibazar, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Munshiganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Munshiganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Mymensingh"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Mymensingh, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Naogaon"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Naogaon, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Narail"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Narail, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Narayanganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Narayanganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Narsingdi"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Narsingdi, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Natore"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Natore, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Netrokona"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Netrokona, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Nilphamari"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Nilphamari, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Noakhali"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Noakhali, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Pabna"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Pabna, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Panchagarh"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Panchagarh, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Patuakhali"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Patuakhali, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Pirojpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Pirojpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Rajbari"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Rajbari, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Rajshahi"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Rajshahi, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Rangamati"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Rangamati, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Rangpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Rangpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Satkhira"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Satkhira, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Shariatpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Shariatpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Sherpur"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Sherpur, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Sirajganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Sirajganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Sunamganj"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Sunamganj, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        } else if(district_s.equals("Sylhet"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Sylhet, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Tangail"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Tangail, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }
        else if(district_s.equals("Thakurgaon"))
        {
            // Toast.makeText(getApplicationContext(),"Faridpur selected",Toast.LENGTH_LONG).show();
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                    R.array.Thakurgaon, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            area.setAdapter(adapter);
        }


















      //  blood_group_s=item;

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
            Intent intent=new Intent(getApplicationContext(),Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            // do something here
        }
        return super.onOptionsItemSelected(item);
    }
}