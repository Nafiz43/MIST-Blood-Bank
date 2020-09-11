package com.example.plasma_bank_cmh;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MyTable {
    TableLayout tableLayout;
    Context context;
    public MyTable(TableLayout tableLayout, Context context){
        this.tableLayout=tableLayout;
        this.context=context;
    }

    void addTableHeaderContents(String [] stringArray,int stringArrayLength){
        //making the table row
        TableRow tableRow=new TableRow(tableLayout.getContext());
        tableRow.setOrientation(LinearLayout.HORIZONTAL);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            tableRow.setPaddingRelative(10,10,10,10);
        }

        //making the textview array
        TextView[] textView=new TextView[stringArrayLength];

        //setting each of the strings in textviews
        for(int i=0;i<stringArrayLength;i++){
            textView[i]=new TextView(tableLayout.getContext());
            textView[i].setText(stringArray[i]);
            textView[i].setTextColor(Color.WHITE);
            textView[i].setPadding(10,10,10,10);
            textView[i].setAllCaps(true);
            textView[i].setBackgroundColor(Color.BLACK);
        }
        //adding the textviews to the tablerow
        for(int i=0;i<stringArrayLength;i++){
            tableRow.addView(textView[i]);
        }
        //finally adding the table row to the table layout
        tableLayout.addView(tableRow);
    }



    void addStringArrayContents(final String [] stringArray, int stringArrayLength){
        boolean greenColor=false;
        if(stringArrayLength==11){//it means there is the last donation date
            //I take in the date strings
            String negDateOfTheRow=stringArray[8];

            //at first i deal with the megdate
            SimpleDateFormat simpleDateFormat=new SimpleDateFormat("yyyy/MM/dd");

            try{
                //we now get the negative date object
                Date negDate=simpleDateFormat.parse(negDateOfTheRow);
                //we now get the present date object
                Calendar calender1=Calendar.getInstance();
                calender1.add(Calendar.DATE,-28);
                Date thresholdNegativeDate=calender1.getTime();
                //the thresholdNegative Date should be after the neg date of the row in order for the row to be green
                if(negDate.compareTo(thresholdNegativeDate)<=0){
                    greenColor=true;
                    Log.d("TAGTable","Key="+stringArray[0]+" is green");
                }else{
                    greenColor=false;
                    Log.d("TAGTable","Key="+stringArray[0]+" is red");
                }

                Log.d("TAGTable","Successfully got the date object made");
            }catch (Exception e){
                Log.d("TAGTable","Could not get the date object made");
            };

            String lastDonationDateOfTheRow=stringArray[10];
            try{
                //we now get the negative date object
                Date lastDonationDate=simpleDateFormat.parse(lastDonationDateOfTheRow);
                //we now get the present date object
                Calendar calender1=Calendar.getInstance();
                calender1.add(Calendar.DATE,-90);
                Date thresholdLastDonationDate=calender1.getTime();
                //the thresholdNegative Date should be after the neg date of the row in order for the row to be green

                if(lastDonationDate.equals("empty")){
                    //no change in the decision.Prev decision stands
                }
                else if(lastDonationDate.compareTo(thresholdLastDonationDate)<=0){
                    greenColor=true;
                    Log.d("TAGTable","Key="+stringArray[0]+" is green");
                }else{
                    greenColor=false;
                    Log.d("TAGTable","Key="+stringArray[0]+" is red");
                }

                Log.d("TAGTable","Successfully got the date object made");
            }catch (Exception e){
                Log.d("TAGTable","Could not get the date object made");
            };


        }



        //making the table row
        final TableRow tableRow=new TableRow(tableLayout.getContext());
        tableRow.setOrientation(LinearLayout.HORIZONTAL);
        tableRow.setPadding(10,10,10,10);


        //making the textview array
        final TextView[] textView=new TextView[stringArrayLength];

        //setting each of the strings in textviews
        for(int i=0;i<stringArrayLength;i++){
            textView[i]=new TextView(tableLayout.getContext());
            textView[i].setText(stringArray[i]);
            textView[i].setTextColor(Color.BLACK);
            textView[i].setMinimumHeight(80);
            if(greenColor==true){
                textView[i].setBackgroundColor(Color.MAGENTA);
            }


            if(i%2==0){//even textview or cell
                if(greenColor==true){
                    textView[i].setBackgroundColor(0X85629540);
                }else{
                    textView[i].setBackgroundColor(0X85ED0909);
                }

            }else{
                if(this.tableLayout.getChildCount()%2==0){
                    textView[i].setBackgroundColor(0X851589ED);
                }else{
                    textView[i].setBackgroundColor(0XFFCBE2F6);
                }

            }
        }
        //adding the textviews to the tablerow
        for(int i=0;i<stringArrayLength;i++){
            tableRow.addView(textView[i]);
        }

        //Adding the long press listener to the table row
        final boolean finalGreenColor = greenColor;
        tableRow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                //I am making the row show an alert dialog box when it is long pressed
                //It will deal with calling and updating options
                AlertDialog.Builder myAlertDialogBuilder = new AlertDialog.Builder(context);
                myAlertDialogBuilder.setMessage("You Selected \n"+
                        "\nKey: "+textView[0].getText()+
                        "\nName: "+textView[1].getText()+
                        "\nAddress: "+textView[2].getText()+
                        "\nblood: "+textView[3].getText()+
                        "\ncontact: "+textView[4].getText()+
                        "\nDOB: "+textView[5].getText()+
                        "\nmail: "+textView[6].getText()+
                        "\nhistory: "+textView[7].getText()+
                        "\npositive date: "+textView[8].getText()+
                        "\nnegative date: "+textView[9].getText()+
                        "\nlast donation date: "+textView[10].getText()
                );
                myAlertDialogBuilder.setCancelable(true);

                myAlertDialogBuilder.setPositiveButton(
                        "Profile",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent intent=new Intent(context,update_donor.class);

                                //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtra("rowContentsString",stringArray);
                                context.startActivity(intent);
                                // Toast.makeText(context,"Updating",Toast.LENGTH_SHORT).show();
                            }
                        });

                myAlertDialogBuilder.setNegativeButton(
                        "Call",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
//                                Intent callIntent = new Intent(Intent.ACTION_CALL);
//                                callIntent.setData(Uri.parse("tel:"+textView[4].getText()));//change the number
//                                context.startActivity(callIntent);

                                Uri u = Uri.parse("tel:" + textView[4].getText().toString());
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                context.startActivity(i);
                            }
                        });
                myAlertDialogBuilder.setNeutralButton(
                        "Record Donation",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(finalGreenColor ==true){
                                    Intent intent=new Intent(context,record_donation.class);
                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("rowContentsString",stringArray);
                                    context.startActivity(intent);

                                    Toast.makeText(context,"Going to record activity",Toast.LENGTH_SHORT).show();
                                }else{
                                    //If the donor is red I am gonna give a warning
                                    dialog.dismiss();
                                    AlertDialog.Builder alertDialogBuilder= new AlertDialog.Builder(context);
                                    alertDialogBuilder.setMessage("This donor is red. Are you sure you want to record donation?");
                                    alertDialogBuilder.setCancelable(true);
                                    alertDialogBuilder.setTitle("Confirmation");
                                    alertDialogBuilder.setPositiveButton(
                                            "Yes",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    Intent intent=new Intent(context,record_donation.class);
                                                    //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                    intent.putExtra("rowContentsString",stringArray);
                                                    context.startActivity(intent);
                                                    // Toast.makeText(context,"Updating",Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                    alertDialogBuilder.setNegativeButton(
                                            "No",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                }
                                            });

                                    Toast.makeText(context,"This donor is RED ",Toast.LENGTH_SHORT).show();
                                    AlertDialog alertDialog1 = alertDialogBuilder.create();
                                    alertDialog1.show();
                                }


                            }
                        });

                AlertDialog alertDialog = myAlertDialogBuilder.create();
                alertDialog.show();
                return true;
                //I have completed the alert dialogoue box creation with all its functionalities
            }
        });

        //Listener has been added to this row for update and calling

        //finally adding the table row to the table layout
        tableLayout.addView(tableRow);
    }

}
