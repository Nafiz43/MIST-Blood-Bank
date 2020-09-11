package com.example.plasma_bank_cmh;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class donorlist extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    boolean successFlag =false;
    final int tableLimit=50;

    //***********************************THE VARIABLSE RELATED TO THE DATES*****************************//
   // TextView coronaNegativeBeforeTextView,
    TextView lastDonationBeforeTextView;
    //EditText coronaNegativeBeforeEditText,
    EditText lastDonationBeforeEditText;
    //***********************************THE VARIABLSE RELATED TO THE DATES DECLARED*****************************//

    String district_s;

    MyTable myTable;
    TableLayout tableLayout;

    EditText searchNameEditText, searchMobileNoEditText,searchAddressEditText,searchEmailAddressEditText, searchIdEditText;
    TextView searchNameTextView,searchMobileNoTextView,searchAddressTextView,searchEmailAddressTextView, searchIdTextView;
    Spinner searchBloodGroupSpinner;
    TextView searchBloodGroupTextView;

    TextView searchDistrictTextView,searchAreaTextView;

    Button searchButton,unLoadTableButton,reloadButton;
    RadioButton serialNoRadioButton,otherInfoRadioButton,districtAreaRadioButton;
    Spinner area,district;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donorlist);



        //Setting up the table frame work
        setUpTheTableLayoutObject();
        myTable.addTableHeaderContents(new String[]{"Reg. No","Name", "Address", "Blood Grp", "Contact","DOB","Mail","Med History"," Neg Date","Pos Date","Last Donation"},11);
        // Getting ref to the database and ading a listener to it for updating the tables
        final DatabaseReference myRef = FirebaseDatabase.getInstance().getReference().child("donor");
        final ValueEventListener valueEventListener= getAValueEventListenerForInsertingGrandChildren();


        myRef.addListenerForSingleValueEvent(valueEventListener);
        //LINKING THE INPUT VIEWS
        linkSearchInputViews();
        //This is just for a test
        settingUpTheTestButtons(myRef, valueEventListener);
        //********************MAKING THE RADIO BUTTONS FUNCTIONAL***********************//
        makeTheRadioButtonsWorkable();

        district=findViewById(R.id.district);
        district.setOnItemSelectedListener(this);


        //**********************MAKING THE SEARCH BUTTON WORKABLE*******************//
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(serialNoRadioButton.isChecked()){
                    String key= String.valueOf(searchIdEditText.getText());
                    if(!TextUtils.isEmpty(key)){
                        tryToGetDataUsingKey();
                    }

                }else if(otherInfoRadioButton.isChecked()){
                    String mysearchName= String.valueOf(searchNameEditText.getText());
                    String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
                    String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
                    String mysearchAddress= String.valueOf(searchAddressEditText.getText());
                    String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());
                    //the date related strings start
                    String negativeDaysString= String.valueOf(0).trim();
                    String lastDonationGapString= String.valueOf(lastDonationBeforeEditText.getText()).trim();
                    //the date related strings ends
                    //date related tests start

                    //date realted tests end
                    if(!TextUtils.isEmpty(negativeDaysString)){
                        Log.d("TAGZ","I TRIED WITH DATE");
                        tryToGetDataUsingNegativeDate();
                    } else if(!TextUtils.isEmpty(mysearchMobileNo)){

                        tryingToGetDataUsingMobileNo();
                    }else if(!TextUtils.isEmpty(mysearchEmail)){
                        tryingToGetDataUsingEmailAddress();
                    }else if(!mysearchBloodGroup.equals("None")){
                        tryingToGetDataUsingBloodGroup();
                    }else if(!TextUtils.isEmpty(mysearchAddress)){
                        tryingToGetDataUsingAddress();
                    }else {//with name
                        tryingToGetDataUsingName();
                    }

                }else if(districtAreaRadioButton.isChecked()){
                    //tryingToGetDataUsingDistrict();
                    tryToGetDataUsingCornaNegativeAndLastDonationDateWithDistrictAndArea();
                }

                //TEST

                //TEST ENDS



                //tryingToGetDataUsingMobileNo();
            }
        });




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

    private void makeTheRadioButtonsWorkable() {
        //For the first radio button
        serialNoRadioButton=findViewById(R.id.serialNoRadioButton);
        serialNoRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchIdTextView.setVisibility(View.VISIBLE);
                searchIdEditText.setVisibility(View.VISIBLE);

               // coronaNegativeBeforeTextView.setVisibility(View.GONE);
               // coronaNegativeBeforeEditText.setVisibility(View.GONE);

                lastDonationBeforeTextView.setVisibility(View.GONE);
                lastDonationBeforeEditText.setVisibility(View.GONE);

                area.setVisibility(View.GONE);
                searchAreaTextView.setVisibility(View.GONE);
                district.setVisibility(View.GONE);
                searchDistrictTextView.setVisibility(View.GONE);

                searchNameTextView.setVisibility(View.GONE);
                searchNameEditText.setVisibility(View.GONE);

                searchBloodGroupSpinner.setVisibility(View.GONE);
                searchBloodGroupTextView.setVisibility(View.GONE);

                searchMobileNoEditText.setVisibility(View.GONE);
                searchMobileNoTextView.setVisibility(View.GONE);

                searchAddressEditText.setVisibility(View.GONE);
                searchAddressTextView.setVisibility(View.GONE);

                searchEmailAddressEditText.setVisibility(View.GONE);
                searchEmailAddressTextView.setVisibility(View.GONE);


            }
        });
        //for the second radio  button
        otherInfoRadioButton=findViewById(R.id.otherInfoRadioButton);
        otherInfoRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                area.setVisibility(View.GONE);
                searchAreaTextView.setVisibility(View.GONE);
                district.setVisibility(View.GONE);
                searchDistrictTextView.setVisibility(View.GONE);

                searchIdTextView.setVisibility(View.GONE);
                searchIdEditText.setVisibility(View.GONE);

                //coronaNegativeBeforeTextView.setVisibility(View.VISIBLE);
              //  coronaNegativeBeforeEditText.setVisibility(View.VISIBLE);

                lastDonationBeforeTextView.setVisibility(View.VISIBLE);
                lastDonationBeforeEditText.setVisibility(View.VISIBLE);

                searchNameTextView.setVisibility(View.VISIBLE);
                searchNameEditText.setVisibility(View.VISIBLE);

                searchBloodGroupSpinner.setVisibility(View.VISIBLE);
                searchBloodGroupTextView.setVisibility(View.VISIBLE);

                searchMobileNoEditText.setVisibility(View.VISIBLE);
                searchMobileNoTextView.setVisibility(View.VISIBLE);

                searchAddressEditText.setVisibility(View.VISIBLE);
                searchAddressTextView.setVisibility(View.VISIBLE);

                searchEmailAddressEditText.setVisibility(View.VISIBLE);
                searchEmailAddressTextView.setVisibility(View.VISIBLE);

            }
        });
        districtAreaRadioButton=findViewById(R.id.districtAreaRadioButton);
        districtAreaRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // coronaNegativeBeforeTextView.setVisibility(View.VISIBLE);
               // coronaNegativeBeforeEditText.setVisibility(View.VISIBLE);

                lastDonationBeforeTextView.setVisibility(View.VISIBLE);
                lastDonationBeforeEditText.setVisibility(View.VISIBLE);


                area.setVisibility(View.VISIBLE);
                searchAreaTextView.setVisibility(View.VISIBLE);
                district.setVisibility(View.VISIBLE);
                searchDistrictTextView.setVisibility(View.VISIBLE);


                searchIdTextView.setVisibility(View.GONE);
                searchIdEditText.setVisibility(View.GONE);

                searchNameTextView.setVisibility(View.GONE);
                searchNameEditText.setVisibility(View.GONE);

                searchBloodGroupSpinner.setVisibility(View.VISIBLE);
                searchBloodGroupTextView.setVisibility(View.VISIBLE);

                searchMobileNoEditText.setVisibility(View.GONE);
                searchMobileNoTextView.setVisibility(View.GONE);

                searchAddressEditText.setVisibility(View.GONE);
                searchAddressTextView.setVisibility(View.GONE);

                searchEmailAddressEditText.setVisibility(View.GONE);
                searchEmailAddressTextView.setVisibility(View.GONE);
            }
        });

        districtAreaRadioButton.performClick();
    }

    private void settingUpTheTestButtons(final DatabaseReference myRef, final ValueEventListener valueEventListener) {
//        unLoadTableButton=findViewById(R.id.unloadTabbleButton);
//        unLoadTableButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (myTable.tableLayout.getRootView() != null) {
//                    int i = 1;
//                    while (myTable.tableLayout.getChildCount() != 1) {
//                        myTable.tableLayout.removeViewAt(i);
//                    }
//                }
//                Log.d("TAG1","Loop passed Unloaded Table");
//            }
//        });
//        reloadButton=findViewById(R.id.reloadTableButton);
//        reloadButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                myRef.removeEventListener(valueEventListener);
//                myRef.addListenerForSingleValueEvent(valueEventListener);
//            }
//        });
    }
    //all 3 ok
    private void tryingToGetDataUsingMobileNo() {

        //AT FIRST I COLLECT ALL THE STRINGS FROM THE INPUT SEARCH FORM
        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("contact").equalTo(mysearchMobileNo);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG1","Data Snapshot value is :"+dataSnapshot.getValue());
                // I immediately quit the function of the reference is null
                //else i continue
                //Either way the table gets empty
                //The successflag changes
                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with mobile no");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with mobile no");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table


                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }

                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))
                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))
                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))
                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))
                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))
                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //all 3 ok
    private void tryingToGetDataUsingEmailAddress() {

        //AT FIRST I COLLECT ALL THE STRINGS FROM THE INPUT SEARCH FORM
        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("mail").equalTo(mysearchEmail);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG1","Data Snapshot value is :"+dataSnapshot.getValue());
                // I immediately quit the function of the reference is null
                //else i continue
                //Either way the table gets empty
                //The successflag changes
                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with mail");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with mail");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table


                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }


                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))
                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))
                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))
                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))
                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))
                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //all 3 ok
    private void tryingToGetDataUsingAddress() {

        //AT FIRST I COLLECT ALL THE STRINGS FROM THE INPUT SEARCH FORM
        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("address").equalTo(mysearchAddress);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG1","Data Snapshot value is :"+dataSnapshot.getValue());
                // I immediately quit the function of the reference is null
                //else i continue
                //Either way the table gets empty
                //The successflag changes
                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with address");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with address");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table


                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }

                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))
                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))
                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))
                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))
                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))
                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    //all 3 ok
    private void tryingToGetDataUsingBloodGroup() {

        //AT FIRST I COLLECT ALL THE STRINGS FROM THE INPUT SEARCH FORM
        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("blood_group").equalTo(mysearchBloodGroup);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG1","Blood search done with:"+mysearchBloodGroup);
                Log.d("TAG1","Data Snapshot value is :"+dataSnapshot.getValue());
                // I immediately quit the function of the reference is null
                //else i continue
                //Either way the table gets empty
                //The successflag changes
                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with blood group");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with blood group");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table


                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }


                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))
                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))
                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))
                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))
                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))
                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //all 3 ok
    private void tryingToGetDataUsingName() {
        Log.d("TAG1","Trying to get data using Name");
        //AT FIRST I COLLECT ALL THE STRINGS FROM THE INPUT SEARCH FORM
        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("name").equalTo(mysearchName);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d("TAG1","Data Snapshot value is :"+dataSnapshot.getValue());
                // I immediately quit the function of the reference is null
                //else i continue
                //Either way the table gets empty
                //The successflag changes
                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with Name");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with Name");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table


                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();
                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }


                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))
                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))
                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))
                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))
                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))
                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //count adjusted
    //11 data added
    //lastdonation taken
    private void tryToGetDataUsingKey() {
        String id= String.valueOf(searchIdEditText.getText());
        if(!TextUtils.isEmpty(id) && searchIdEditText.getVisibility()== View.VISIBLE){//if the id field is not empty
            //*********************GETTING THE REFERENCE TO A PARTICULAR ID**************************************//

            DatabaseReference idReference= FirebaseDatabase.getInstance().getReference().child("donor").child(id);

            //*****************************EMPTYING THE TABLE OTHER THAN HEADER**********************************//
            if (myTable.tableLayout.getRootView() != null) {
                int i = 1;
                while (myTable.tableLayout.getChildCount() != 1) {
                    myTable.tableLayout.removeViewAt(i);
                }
            }
            Log.d("TAG1","Loop passed Unloaded Table");
            ValueEventListener valueEventListener2= getAValueEventListenerForKeyBasedSearch();
            idReference.addValueEventListener(valueEventListener2);


        }else{
            Log.d("TAG1","ID FIELD IS EMPTY or Invisible");

        }
    }


    //all 3  ok
    private void tryToGetDataUsingCornaNegativeAndLastDonationDateWithDistrictAndArea(){
        //I AM ALSO GETTING BLOOD GRP NOW
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();

        //At first I have to get the date controlling information from the esit texts
        String negativeDaysString= String.valueOf(0).trim();
        //negativeDaysString=negativeDaysString.replaceAll("\\s+","");
        String lastDonationGapString= String.valueOf(lastDonationBeforeEditText.getText()).trim();
        // lastDonationGapString=lastDonationGapString.replaceAll("\\s+","");
        String mysearchDistrict= String.valueOf(district.getSelectedItem().toString());
        //mysearchDistrict=mysearchDistrict.replaceAll("\\s+","");
        String mysearchArea= String.valueOf(area.getSelectedItem().toString());
        //mysearchArea=mysearchArea.replaceAll("\\s+","");
        //*************************I have to make sure that the corona negative field has some values****************//

        if(TextUtils.isEmpty(mysearchDistrict)){
            Toast.makeText(donorlist.this, "District Filed cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        //OK

        int negativeDays=0,lastDonationGap=0;
        if(TextUtils.isEmpty(negativeDaysString) ){
            //negative days will be zero
            //lastDonationDateGap will be zero
        }else{

            negativeDays=Integer.parseInt(negativeDaysString);
        }
        if(!TextUtils.isEmpty(lastDonationGapString)){
            lastDonationGap=Integer.parseInt(lastDonationGapString);
        }
        //I have collected the required fields
        //Now I have to make two dates one for corona negative and the other is last donation//
        //For the corona negative date threshold
        Log.d("TAGS","DEBUG");
        Calendar calender1=Calendar.getInstance();
        calender1.add(Calendar.DATE,-negativeDays);
        final Date thresholdNegativeDate=calender1.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        final String threshNegDateString=simpleDateFormat.format(thresholdNegativeDate);
        //for the donation date threshold
        Calendar calender2=Calendar.getInstance();
        calender2.add(Calendar.DATE,-lastDonationGap);
        Date thresholdLastDonationDate=calender2.getTime();
        final String threshDonateDateString=simpleDateFormat.format(thresholdLastDonationDate);


        Log.d("TAGZ","threshNegDate:"+threshNegDateString+",threshLastDonationDate:"+threshDonateDateString);
        //***************************I have got the two threshold dates in string format**************************//

        //*****************************Now I query the database based******************************************//
        //I MAKE A QUERY AS PER THE SPECIFIED DISTRICT
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("district").equalTo(mysearchDistrict);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        final String finalMysearchArea = mysearchArea;
        Log.d("TAGZ","Mysearch area="+mysearchArea);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()==null){
                    Log.d("TAGZ","There is no data when I am searching with district");
                    successFlag=false;
                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    successFlag=true;
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with address");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table
                Log.d("TAGZ","I came b4 count in distr area date");
                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String district=ds.child("district").getValue().toString();
                    String area=ds.child("area").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }

                    //I check if all the data satisfy my search conditions
                    Log.d("TAGZ","Came to the next filter");
                    Log.d("TAGZ","Comparing thresh negdate:"+threshNegDateString+", donor neg date"+neg_date);
                    Log.d("TAGZ","************Date comparison result"+(threshNegDateString.compareTo(neg_date)));

                    Log.d("TAGZ","Key"+key+",area="+area+"mySearchArea="+finalMysearchArea+" result:"+area.contains(finalMysearchArea));
                    if(
                            (area.contains(finalMysearchArea)||TextUtils.isEmpty(finalMysearchArea))
                                    &&((threshNegDateString.compareTo(neg_date))>=0)

                                    &&(
                                    (lastDonationDate.equals("empty"))||
                                            ( (!lastDonationDate.equals("empty"))  && ((threshDonateDateString.compareTo(lastDonationDate))>=0)  )
                            )
                                    &&(threshNegDateString.compareTo(threshDonateDateString)<=0)
                                    &&(bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))


                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        Log.d("TAGZ","("+key+","+name+","+address+","+bloodGroup+","+contact+","+dob+","+mail+","+med_history+","+neg_date+","+pos_date+")");
                        Log.d("TAGZ","DEBUG in if");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        Log.d("TAGZ","DEBUG in else");
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    //count adjusted
    //11 data inserted
    //lastdonation taken
    private  void tryToGetDataUsingNegativeDate(){

        final String mysearchName= String.valueOf(searchNameEditText.getText());
        final String mysearchBloodGroup=searchBloodGroupSpinner.getSelectedItem().toString();
        final String mysearchMobileNo= String.valueOf(searchMobileNoEditText.getText()).trim();
        final String mysearchAddress= String.valueOf(searchAddressEditText.getText());
        final String mysearchEmail= String.valueOf(searchEmailAddressEditText.getText());

        String negativeDaysString= String.valueOf(0).trim();
        String lastDonationGapString= String.valueOf(lastDonationBeforeEditText.getText()).trim();
        int negativeDays=0,lastDonationGap=0;
        if(TextUtils.isEmpty(negativeDaysString) ){
            //negative days will be zero
            //lastDonationDateGap will be zero
        }else{
            negativeDays=Integer.parseInt(negativeDaysString);

        }
        if(!TextUtils.isEmpty(lastDonationGapString)){
            lastDonationGap=Integer.parseInt(lastDonationGapString);
        }

        //******************I have collected the date related days as integers*************************//
        //**********************Now i am gonna meke the date strings**********************************//
        Log.d("TAGS","DEBUG");
        Calendar calender1=Calendar.getInstance();
        calender1.add(Calendar.DATE,-negativeDays);
        final Date thresholdNegativeDate=calender1.getTime();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");
        final String threshNegDateString=simpleDateFormat.format(thresholdNegativeDate);
        //final String threshNegDateString="0810";
        //for the donation date threshold
        Calendar calender2=Calendar.getInstance();
        calender2.add(Calendar.DATE,-lastDonationGap);
        Date thresholdLastDonationDate=calender2.getTime();
        final String threshDonateDateString=simpleDateFormat.format(thresholdLastDonationDate);
        //final String threshDonateDateString="0810";
        Log.d("TAGZ","threshNegDateString:"+threshNegDateString+",threshLastDonationDateString:"+threshDonateDateString);


        //************************The two thresh date strings have been made*********************************************//

        //*************************Now I am going to get the data using neg thresh date*********************************//
        //I MAKE A QUERY AS PER THE SPEICIFIED MOBILE NO
        Query query= FirebaseDatabase.getInstance().getReference().child("donor").orderByChild("neg_date").endAt(threshNegDateString);
        //I ADD LISTENER TO EXECUTE THE QUERY IMMEDIATELY
        query.addListenerForSingleValueEvent(new ValueEventListener() {

            //INSIDE THE LISTENER ONLY THIS DATA RUNS
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue()==null){
                    Log.d("TAG1","There is no data when I am searching with mobile no");

                    //EMptying the table
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    return;
                    //EMptying Done
                }else{
                    //EMptying the table
                    Log.d("TAG1","There is data when I am trying with mobile no");
                    if (myTable.tableLayout.getRootView() != null) {
                        int i = 1;
                        while (myTable.tableLayout.getChildCount() != 1) {
                            myTable.tableLayout.removeViewAt(i);
                        }
                    }
                    //EMptying Done
                }
                //I have not returned out. So my data reference was successful
                //I will now proceed to get the data into the empty table
                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){

                    //I get a row from database and store the data into strings
                    String key=ds.getKey();
                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String mail=ds.child("mail").getValue().toString();
                    String dob=ds.child("dob").getValue().toString();
                    String bloodGroup=ds.child("blood_group").getValue().toString();

                    String pos_date=ds.child("pos_date").getValue().toString();
                    String neg_date=ds.child("neg_date").getValue().toString();
                    String med_history=ds.child("med_history").getValue().toString();

                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }

                    //I check if all the data satisfy my search conditions
                    if(     (contact.contains(mysearchMobileNo)|| TextUtils.isEmpty(mysearchMobileNo))

                            &&(mail.contains(mysearchEmail)||TextUtils.isEmpty(mysearchEmail))

                            && (name.contains(mysearchName)||TextUtils.isEmpty(mysearchName))

                            && (address.contains(mysearchAddress)||TextUtils.isEmpty(mysearchAddress))

                            && (bloodGroup.equals(mysearchBloodGroup)||mysearchBloodGroup.contains("None"))

                            &&(
                            lastDonationDate.equals("empty")
                                    ||
                                    (( (!lastDonationDate.equals("empty")) ) && ((threshDonateDateString.compareTo(lastDonationDate))>=0) )
                    )

                            &&(threshNegDateString.compareTo(threshDonateDateString)<=0)

                    ){

                        //if conditions are satisfied i insert the row
                        //Log.d("TAG1","Data being inserted into the table");
                        count++;
                        myTable.addStringArrayContents(new String[]{key, name, address, bloodGroup, contact, dob, mail, med_history, neg_date, pos_date,lastDonationDate},11);
                    }else{
                        //else i don't insert the row
                        //Log.d("TAG1","Failed to insert data");
                    }

                    if(count==tableLimit)break;
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




    }

    private void linkSearchInputViews() {
        //the date related views
        //coronaNegativeBeforeTextView=findViewById(R.id.coronaNegativebeforeTextView);
        //coronaNegativeBeforeEditText=findViewById(R.id.coronaNegativebeforeEditText);
        lastDonationBeforeTextView=findViewById(R.id.lastDonationBeforeTextView);
        lastDonationBeforeEditText=findViewById(R.id.lastDonationBeforeEditText);

        //distric area related views
        district=findViewById(R.id.district);
        searchDistrictTextView=findViewById(R.id.searchDistrictTextView);
        area=findViewById(R.id.area);
        searchAreaTextView=findViewById(R.id.searchAreaTextView);

        searchButton=findViewById(R.id.searchButton);
        searchIdEditText =findViewById(R.id.idEditText);

        searchNameEditText=findViewById(R.id.searchNameEditText);
        searchBloodGroupSpinner=findViewById(R.id.searchBloodGroupSpinner);
        searchMobileNoEditText =findViewById(R.id.searchMobileNoEditText);

        searchAddressEditText=findViewById(R.id.searchAddressEditText);
        searchEmailAddressEditText=findViewById(R.id.searchEmailEditText);
        //**************Linking the textviews***************************//
        searchIdTextView=findViewById(R.id.searchSerialNoTextView);

        searchNameTextView=findViewById(R.id.searchNameTextView);
        searchBloodGroupTextView=findViewById(R.id.searchBloodGroupTextView);
        searchMobileNoTextView=findViewById(R.id.searchMobileNoTextView);

        searchAddressTextView=findViewById(R.id.searchAddressTextView);
        searchEmailAddressTextView=findViewById(R.id.searchEmailTextView);

    }

    void setUpTheTableLayoutObject(){
        tableLayout=findViewById(R.id.table_id);
        myTable=new MyTable(tableLayout,donorlist.this);
        Log.d("TAG1","I have made myTable");
    }

    //count adjusted
    //lastdonation placed
    //11 data inserted
    ValueEventListener getAValueEventListenerForKeyBasedSearch(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TAG1","In side on data change");

                DataSnapshot ds=dataSnapshot;
                if(ds.getValue()==null){

                    successFlag=false;
                    Log.d("TAG1","There IS NO such children when trying with key ,successful flag="+successFlag);
                    return;
                }else{

                    successFlag=true;

                    Log.d("TAG1","There IS  children when trying with key,successful flag="+successFlag);

                }
                String key=ds.getKey();

                String name=ds.child("name").getValue().toString();
                String address=ds.child("address").getValue().toString();
                String contact=ds.child("contact").getValue().toString();

                String dob=ds.child("dob").getValue().toString();
                String mail=ds.child("mail").getValue().toString();
                String medHistory=ds.child("med_history").getValue().toString();

                String bloodGroup=ds.child("blood_group").getValue().toString();
                String negDate=ds.child("neg_date").getValue().toString();
                String posDate=ds.child("pos_date").getValue().toString();
                String lastDonationDate="";
                if(ds.child("last_donation_date").getValue()==null){
                    lastDonationDate="empty";
                    Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                }else{
                    lastDonationDate=ds.child("last_donation_date").getValue().toString();
                    Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                }

                myTable.addStringArrayContents(new String[]{key,name, address, bloodGroup, contact, dob, mail, medHistory, negDate, posDate,lastDonationDate},11);

            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
            }
        };

    }

    //all 3 fixed
    ValueEventListener getAValueEventListenerForInsertingGrandChildren(){
        return new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Log.d("TAG1","In side on data change");
                int count=0;
                for(DataSnapshot ds:dataSnapshot.getChildren()){
                    count++;
                    String key=ds.getKey();

                    String name=ds.child("name").getValue().toString();
                    String address=ds.child("address").getValue().toString();
                    String contact=ds.child("contact").getValue().toString();

                    String dob=ds.child("dob").getValue().toString();
                    String mail=ds.child("mail").getValue().toString();
                    String medHistory=ds.child("med_history").getValue().toString();

                    String bloodGroup=ds.child("blood_group").getValue().toString();
                    String negDate=ds.child("neg_date").getValue().toString();
                    String posDate=ds.child("pos_date").getValue().toString();
                    String lastDonationDate="";
                    if(ds.child("last_donation_date").getValue()==null){
                        lastDonationDate="empty";
                        Log.d("TAGZ","This person never donated blood:"+lastDonationDate);
                    }else{
                        lastDonationDate=ds.child("last_donation_date").getValue().toString();
                        Log.d("TAGZ","This person  donated blood on:"+lastDonationDate);
                    }

                    myTable.addStringArrayContents(new String[]{key,name, address, bloodGroup, contact, dob, mail, medHistory, negDate, posDate,lastDonationDate},11);
                    if(count%tableLimit==0) break;
                }

                Log.d("TAG1","Loop passed Loaded Table");


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
        Intent intent=new Intent(getApplicationContext(),Home.class);
        //   intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}