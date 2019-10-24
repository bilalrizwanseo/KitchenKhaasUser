package com.example.technohem.kitchenkhaasuser;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.technohem.kitchenkhaasuser.Model.Menus;
import com.example.technohem.kitchenkhaasuser.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class MenuDetailsActivity extends AppCompatActivity {

    private Button addToCartButton;
    private ImageView menuImage;
    private TextView menuName, menuPrice;
    private EditText editText_People_Number;
    private String menuID = "";
    private String imageUrl = "";

    private String state = "Normal";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_details);

        menuID = getIntent().getStringExtra("pid");
        imageUrl = getIntent().getStringExtra("img_url");


        addToCartButton = (Button)findViewById(R.id.md_add_to_cart_button);
        menuImage = (ImageView)findViewById(R.id.menu_image_details);
        menuName = (TextView) findViewById(R.id.menu_name_details);
        menuPrice = (TextView) findViewById(R.id.menu_price_details);
        editText_People_Number = (EditText) findViewById(R.id.editText_NoOfPeople);

        getMenuDetails(menuID);

        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {

                // check internet status
                Boolean internetStatus = checkInternet();

                if (internetStatus == true)
                {
                    String guests = editText_People_Number.getText().toString();

                    if (TextUtils.isEmpty(guests))
                    {
                        Toast.makeText(MenuDetailsActivity.this, "Please Enter No. Of People", Toast.LENGTH_SHORT).show();
                    }
                    else {

                        if (state.equals("Order Placed") || (state.equals("Order Shipped")))
                        {
                            Toast.makeText(MenuDetailsActivity.this, "you can place more orders, once your previous event is done...", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            addingToCartList();
                        }
                    }
                }
                else
                {
                    Toast.makeText(MenuDetailsActivity.this, "No Internet! Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }// on create end

    @Override
    protected void onStart() {
        super.onStart();

        CheckOrderState();
    }

    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;

        Calendar calForDate = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM, dd , yyyy");
        saveCurrentDate = currentDate.format(calForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH: mm : ss a");
        saveCurrentTime = currentTime.format(calForDate.getTime());


        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid",menuID);
        cartMap.put("pname",menuName.getText().toString());
        cartMap.put("price",menuPrice.getText().toString());
        cartMap.put("date",saveCurrentDate);
        cartMap.put("time",saveCurrentTime);
        cartMap.put("image",imageUrl);
        cartMap.put("guests",editText_People_Number.getText().toString());

        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(menuID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            cartListRef.child("Admin View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(menuID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                Toast.makeText(MenuDetailsActivity.this, "Added to Cart List", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(MenuDetailsActivity.this, HomeActivity.class);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                    });

                        }
                    }
                });


    }

    private void getMenuDetails(String menuID)
    {
        DatabaseReference menuRef = FirebaseDatabase.getInstance().getReference().child("Menus");

        menuRef.child(menuID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    Menus menus = dataSnapshot.getValue(Menus.class);

                    menuName.setText(menus.getPname());
                    menuPrice.setText(menus.getPrice());
                    Picasso.get().load(menus.getImage()).into(menuImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void CheckOrderState()
    {
        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    String shippingState = dataSnapshot.child("state").getValue().toString();

                    if (shippingState.equals("shipped"))
                    {
                        state = "Order Shipped";

                    }else if (shippingState.equals("Not Shipped"))
                    {
                        state = "Order Placed";

                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        Intent intent = new Intent(MenuDetailsActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }

}
