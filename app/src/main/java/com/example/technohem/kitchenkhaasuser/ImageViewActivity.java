package com.example.technohem.kitchenkhaasuser;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

public class ImageViewActivity extends AppCompatActivity {

    private PhotoView photoGallery;

    private String g_imageUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);

        g_imageUrl = getIntent().getStringExtra("g_img_url");

        photoGallery = (PhotoView)findViewById(R.id.imageGallery);

        Picasso.get().load(g_imageUrl).into(photoGallery);

    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ImageViewActivity.this,GalleryActivity.class));
        finish();
    }
}
