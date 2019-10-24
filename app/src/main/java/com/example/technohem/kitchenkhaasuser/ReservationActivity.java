package com.example.technohem.kitchenkhaasuser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.technohem.kitchenkhaasuser.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;

public class ReservationActivity extends AppCompatActivity {

    private EditText nameEditText, phoneEditText, addressEditText, eventTypeEditText, descriptionEditText;
    private Button confirmOrderBtn;
    TextView dateTextView, timeTextView;

    private String totalAmount = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reservation);

        totalAmount = getIntent().getStringExtra("Total Price");
        Toast.makeText(this, "Total Price =  " + totalAmount + " Rs", Toast.LENGTH_SHORT).show();

        confirmOrderBtn = (Button)findViewById(R.id.confirm_reservation_btn);
        nameEditText = (EditText)findViewById(R.id.reservation_name);
        phoneEditText = (EditText)findViewById(R.id.reservation_phone_number);
        addressEditText = (EditText)findViewById(R.id.reservation_address);
        eventTypeEditText = (EditText)findViewById(R.id.reservation_event_type);
        descriptionEditText = (EditText)findViewById(R.id.reservation_description);

        dateTextView = (TextView)findViewById(R.id.txt_selectDate);
        timeTextView = (TextView)findViewById(R.id.txt_selectTime);

        dateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleDateButton();
            }
        });

        timeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleTimeButton();
            }
        });

        confirmOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check internet status
                Boolean internetStatus = checkInternet();

                if (internetStatus == true)
                {
                    Check();
                }
                else
                {
                    Toast.makeText(ReservationActivity.this, "No Internet! Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Check() {

        if(TextUtils.isEmpty(nameEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your full name.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(phoneEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Mobile NumberParty", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(eventTypeEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter event Type.", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(addressEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter your full address.", Toast.LENGTH_SHORT).show();
        }
        else if(dateTextView.getText().toString().equals("Select Date..."))
        {
            Toast.makeText(this, "Please Select date of event", Toast.LENGTH_SHORT).show();
        }
        else if(timeTextView.getText().toString().equals("Select Time..."))
        {
            Toast.makeText(this, "Please Select time of event", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(descriptionEditText.getText().toString()))
        {
            Toast.makeText(this, "Please Enter descrpition about event.", Toast.LENGTH_SHORT).show();
        }
        else
        {
            ConfirmOrder();
        }

    }

    private void ConfirmOrder() {

        final DatabaseReference ordersRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders")
                .child(Prevalent.currentOnlineUser.getPhone());

        final HashMap<String, Object> orderMap = new HashMap<>();
        orderMap.put("totalAmount",totalAmount);
        orderMap.put("name",nameEditText.getText().toString());
        orderMap.put("phone",phoneEditText.getText().toString());
        orderMap.put("event",eventTypeEditText.getText().toString());
        orderMap.put("address",addressEditText .getText().toString());
        orderMap.put("description",descriptionEditText.getText().toString());
        orderMap.put("date",dateTextView.getText().toString());
        orderMap.put("time",timeTextView.getText().toString());
        orderMap.put("state","Not Shipped");
        //orderMap.put("state","Pending...");

        ordersRef.updateChildren(orderMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if(task.isSuccessful())
                {
                    FirebaseDatabase.getInstance().getReference().child("Cart List")
                            .child("User View")
                            .child(Prevalent.currentOnlineUser.getPhone())
                            .removeValue()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(ReservationActivity.this, "Your final order has been placed successfully..", Toast.LENGTH_SHORT).show();

                                        Intent intent = new Intent(ReservationActivity.this, HomeActivity.class);
                                        // that user not abel to come back in this activity
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                }
            }
        });
    }

    private void handleTimeButton() {
        Calendar calendar = Calendar.getInstance();
        int HOUR = calendar.get(Calendar.HOUR);
        int MINUTE = calendar.get(Calendar.MINUTE);
        boolean is24HourFormat = DateFormat.is24HourFormat(this);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                //Log.i(TAG, "onTimeSet: " + hour + minute);
                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.HOUR, hour);
                calendar1.set(Calendar.MINUTE, minute);
                String dateText = DateFormat.format("h:mm a", calendar1).toString();
                timeTextView.setText(dateText);
            }
        }, HOUR, MINUTE, is24HourFormat);

        timePickerDialog.show();
    }

    private void handleDateButton() {
        Calendar calendar = Calendar.getInstance();
        int YEAR = calendar.get(Calendar.YEAR);
        int MONTH = calendar.get(Calendar.MONTH);
        int DATE = calendar.get(Calendar.DATE);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {

                Calendar calendar1 = Calendar.getInstance();
                calendar1.set(Calendar.YEAR, year);
                calendar1.set(Calendar.MONTH, month);
                calendar1.set(Calendar.DATE, date);
                String dateText = DateFormat.format("EEEE, MMM d, yyyy", calendar1).toString();

                dateTextView.setText(dateText);
            }
        }, YEAR, MONTH, DATE);

        datePickerDialog.show();
    }

    // check internet status
    public boolean checkInternet() {

        ConnectivityManager cm = (ConnectivityManager)getSystemService(this.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected())
        {
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent (ReservationActivity.this , CartActivity.class);
        startActivity(intent);
        finish();
    }

}
