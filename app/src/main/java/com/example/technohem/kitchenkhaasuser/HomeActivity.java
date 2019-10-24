package com.example.technohem.kitchenkhaasuser;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.technohem.kitchenkhaasuser.Model.Menus;
import com.example.technohem.kitchenkhaasuser.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import io.paperdb.Paper;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    // Recycler View code
    private DatabaseReference menusRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    //toolbar
    Toolbar toolbar;
    //navigation drawer
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    //custom dialog
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation_drawer); //navigation_drawer

        menusRef = FirebaseDatabase.getInstance().getReference().child("Menus");
        // to retrieve text in app offline
        menusRef.keepSynced(true);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);//

        //navigation drawer
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        //navigation drawer
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        // Recycler View code
        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

    }//on create end

    // Recycler View code
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Menus> options =
                new FirebaseRecyclerOptions.Builder<Menus>()
                        .setQuery(menusRef, Menus.class)
                        .build();

        FirebaseRecyclerAdapter<Menus, MenuViewHolder> adapter =
                new FirebaseRecyclerAdapter<Menus, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MenuViewHolder holder, int position, @NonNull final Menus model) {

                        //Picasso.get().load(model.getImage()).into(holder.imageView);
                        // to retrieve image in app offline
                        Picasso.get().load(model.getImage()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load(model.getImage()).into(holder.imageView);
                            }
                        });

                        // set click Listener
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v)
                            {
                                // check internet status
                                Boolean internetStatus = checkInternet();

                                if (internetStatus == true)
                                {
                                    Intent intent = new Intent(HomeActivity.this, MenuDetailsActivity.class);
                                    intent.putExtra("pid",model.getPid());
                                    intent.putExtra("img_url",model.getImage());
                                    startActivity(intent);
                                    finish();
                                }
                                else 
                                {
                                    Toast.makeText(HomeActivity.this, "No Internet! Please Check Your Internet Connection", Toast.LENGTH_SHORT).show();
                                }
                                
                            }
                        });
                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout, parent, false);
                        MenuViewHolder holder = new MenuViewHolder(view);
                        return holder;
                    }
                };
        recyclerView.setAdapter(adapter);
        adapter.startListening();

    }// Recycler View code

    //popUp menu (Ctrl+O)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }//

    //popUp menu
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        switch (id) {
            case R.id.logout_id:
                //Remember me code
                Paper.book().destroy();

                startActivity(new Intent(HomeActivity.this, MainActivity.class));
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }//

    //navigation drawer
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

        int id = menuItem.getItemId();

        switch (id) {
            case R.id.nav_order:
                //Toast.makeText(this, "Order", Toast.LENGTH_SHORT).show();
                Intent intento = new Intent(HomeActivity.this,MyOrdersActivity.class);
                startActivity(intento);
                finish();
                break;
            case R.id.nav_gallery:
                //Toast.makeText(this, "Starred Clicked", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(HomeActivity.this,GalleryActivity.class);
                startActivity(i);
                finish();
                break;
            case R.id.nav_cart:
                //Toast.makeText(this, "Cart Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(HomeActivity.this,CartActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.nav_contact_us:
                //Toast.makeText(this, "All Email Clicked", Toast.LENGTH_SHORT).show();
                customDialogContactUs();
                break;
            case R.id.nav_about_us:
                //Toast.makeText(this, "Trash Clicked", Toast.LENGTH_SHORT).show();
                customDialogAboutUs();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }//

    // Contact Us Code
    private void customDialogContactUs() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_contact_us);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button callbtn = (Button) dialog.findViewById(R.id.buttonCall);
        Button emailbtn = (Button) dialog.findViewById(R.id.buttonEmail);
        Button webbtn = (Button) dialog.findViewById(R.id.buttonWeb);
        Button facebookbtn = (Button) dialog.findViewById(R.id.buttonFB);

        callbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent call = new Intent(Intent.ACTION_VIEW, Uri.parse("tel:03364497488"));
                startActivity(call);
            }
        });

        emailbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //email code
                Log.i("Seand email","");
                String[] TO = {"kitchen.khaas@gmail.com"};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                emailIntent.setData(Uri.parse("mailto:"));
                emailIntent.setType("text/plain");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
                emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"subject");
                emailIntent.putExtra(Intent.EXTRA_TEXT,"Write Here..");

                try{
                    startActivity(Intent.createChooser(emailIntent,"Send mail..."));
                    finish();
                }catch (android.content.ActivityNotFoundException ex){
                    Toast.makeText(getApplicationContext(), "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }

            }
        });

        webbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("http://kitchenkhaas.com/"));
                startActivity(i);
            }
        });

        facebookbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(Intent.ACTION_VIEW,Uri.parse("https://www.facebook.com/kitchenkhaas/"));
                startActivity(i);
            }
        });

        dialog.show();
    }

    // custom dialog about us
    private void customDialogAboutUs() {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.custom_dialog_about_us);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        dialog.show();
    }
    //

    //navigation drawer (to first close the navigation drawer then Application)
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }//

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

}
