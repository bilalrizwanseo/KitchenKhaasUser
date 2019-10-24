package com.example.technohem.kitchenkhaasuser;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.technohem.kitchenkhaasuser.Model.GalleryImages;
import com.example.technohem.kitchenkhaasuser.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class GalleryActivity extends AppCompatActivity {

    // Recycler View code
    private DatabaseReference gallery_imagesRef;
    private RecyclerView gallery_recyclerView;
    RecyclerView.LayoutManager gallery_layoutManager;

    //toolbar
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        gallery_imagesRef = FirebaseDatabase.getInstance().getReference().child("GalleryImages");
        // to retrieve text in app offline
        gallery_imagesRef.keepSynced(true);

        //toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar_gallery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Gallery");//

        // Recycler View code
        gallery_recyclerView = findViewById(R.id.gallery_recycler_images);
        gallery_recyclerView.setHasFixedSize(true);
        gallery_layoutManager = new GridLayoutManager(this, 2);
        gallery_recyclerView.setLayoutManager(gallery_layoutManager);

    } // on create end

    // Recycler View code
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<GalleryImages> options =
                new FirebaseRecyclerOptions.Builder<GalleryImages>()
                        .setQuery(gallery_imagesRef, GalleryImages.class)
                        .build();

        FirebaseRecyclerAdapter<GalleryImages, MenuViewHolder> adapter =
                new FirebaseRecyclerAdapter<GalleryImages, MenuViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final MenuViewHolder holder, final int position, @NonNull final GalleryImages model) {

                        //Picasso.get().load(model.getG_image()).into(holder.imageView1);
                        // to retrieve image in app offline
                        Picasso.get().load(model.getG_image()).networkPolicy(NetworkPolicy.OFFLINE).into(holder.imageView1, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load(model.getG_image()).into(holder.imageView1);
                            }
                        });

                        // set click Listener
                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                Intent intent = new Intent(GalleryActivity.this, ImageViewActivity.class);
                                intent.putExtra("g_img_url",model.getG_image());
                                startActivity(intent);
                                finish();

                            }
                        });

                    }

                    @NonNull
                    @Override
                    public MenuViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gallery_image_layout, parent, false);
                        MenuViewHolder holder = new MenuViewHolder(view);
                        return holder;
                    }
                };
        gallery_recyclerView.setAdapter(adapter);
        adapter.startListening();

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(GalleryActivity.this,HomeActivity.class));
        finish();
    }
}
