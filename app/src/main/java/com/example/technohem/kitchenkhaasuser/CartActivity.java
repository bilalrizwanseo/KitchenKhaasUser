package com.example.technohem.kitchenkhaasuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import com.example.technohem.kitchenkhaasuser.Model.Cart;
import com.example.technohem.kitchenkhaasuser.Prevalent.Prevalent;
import com.example.technohem.kitchenkhaasuser.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button nextProcessBtn;
    private TextView txtTotalPrice;
    private TextView txtMsg1;

    public int overAllTotalPrice = 0;

    // check recyclerView is empty
    int sizeOfRecycler = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        nextProcessBtn = (Button) findViewById(R.id.next_process_btn);
        txtTotalPrice = (TextView) findViewById(R.id.total_price);
        txtMsg1 = (TextView) findViewById(R.id.msg1);

        nextProcessBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // check internet status
                Boolean internetStatus = checkInternet();

                if (internetStatus == true)
                {
                    if (sizeOfRecycler == 0)
                    {
                        Toast.makeText(CartActivity.this, "first select menu", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent (CartActivity.this , ReservationActivity.class);
                        intent.putExtra("Total Price", String.valueOf(overAllTotalPrice));
                        startActivity(intent);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(CartActivity.this, "No Internet! Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }// on create end

    @Override
    protected void onStart() {
        super.onStart();

        // to check state of the order
        CheckOrderState();

        final DatabaseReference cartListRef;
        cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");

        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone()).child("Products"), Cart.class).build();

        final FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CartViewHolder holder, int position, @NonNull final Cart model) {

                // check recyclerView is empty
                sizeOfRecycler = getItemCount();

                holder.txtMenuGuests.setText("Number of people : " + model.getGuests());
                holder.txtMenuName.setText("Name : " +model.getPname());
                holder.txtMenuPrice.setText("Price : " + model.getPrice() +" Rs");
                //holder.txtProductPrice.setText(model.getPrice());

                //Picasso.get().load(model.getImage()).into(holder.menuImage);
                // to retrieve image in app offline
                Picasso.get().load(model.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.menuImage, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError(Exception e) {

                        Picasso.get().load(model.getImage()).into(holder.menuImage);
                    }
                });

                //Calculate Total Price
                int OneTypeProductTPrice = (Integer.parseInt(model.getPrice().replaceAll("\\D+","")))*Integer.parseInt(model.getGuests()) ;
                overAllTotalPrice += OneTypeProductTPrice;

                txtTotalPrice.setText("Total Price = " + String.valueOf(overAllTotalPrice) + " Rs");

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CharSequence option[] = new CharSequence[]
                                {
                                        "Edit",
                                        "Remove"
                                };
                        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
                        builder.setTitle("Cart Options :");

                        builder.setItems(option, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {

                                if( i == 0 )
                                {
                                    Intent intent = new Intent(CartActivity.this, MenuDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    intent.putExtra("img_url", model.getImage());
                                    startActivity(intent);
                                }
                                if(i == 1) {
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {

                                                    if(task.isSuccessful()) {

                                                        ///
                                                        cartListRef.child("Admin View")
                                                                .child(Prevalent.currentOnlineUser.getPhone())
                                                                .child("Products")
                                                                .child(model.getPid())
                                                                .removeValue()
                                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                    @Override
                                                                    public void onComplete(@NonNull Task<Void> task) {

                                                                        if(task.isSuccessful()) {


                                                                            Toast.makeText(CartActivity.this, "item removed successfully...", Toast.LENGTH_SHORT).show();

                                                                            Intent i = new Intent(CartActivity.this, HomeActivity.class);
                                                                            startActivity(i);
                                                                        }
                                                                    }
                                                                });
                                                        ///
                                                    }
                                                }
                                            });

                                }
                            }
                        });

                        builder.show();
                    }
                });

            }

            @Override
            public int getItemCount() {
                return super.getItemCount();
            }

            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i)
            {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return  holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    private void CheckOrderState() {

        DatabaseReference orderRef;
        orderRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());

        orderRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists()) {
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("name").getValue().toString();

                    if (shippingState.equals("shipped")) {
                        txtTotalPrice.setText("Dear" + userName + "\n your previous event is done successfully..");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, Your Event has been done successfully.");

                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can place more order, once your first event is done", Toast.LENGTH_SHORT).show();

                    } else if (shippingState.equals("Not Shipped")) {


                        txtTotalPrice.setText("State = Pending... ");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);

                        nextProcessBtn.setVisibility(View.GONE);

                        Toast.makeText(CartActivity.this, "you can place more order, once your first event is done", Toast.LENGTH_SHORT).show();

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
        Intent intent = new Intent(CartActivity.this,HomeActivity.class);
        startActivity(intent);
        finish();
    }
}
