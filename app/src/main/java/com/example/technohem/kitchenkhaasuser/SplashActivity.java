package com.example.technohem.kitchenkhaasuser;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.technohem.kitchenkhaasuser.Model.Users;
import com.example.technohem.kitchenkhaasuser.Prevalent.Prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {

    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        // Splash Screen
        final ImageView imageView = (ImageView) findViewById(R.id.imageView_logo);
        loadingBar = new ProgressDialog(this);

        //Remember me code
        Paper.init(this);

        // Splash Screen
        final Animation fade_in_anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_in);
        final Animation fade_out_anim = AnimationUtils.loadAnimation(getBaseContext(), R.anim.fade_out);

        imageView.startAnimation(fade_in_anim);
        fade_in_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(fade_out_anim);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        fade_out_anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {

                //Remember me code
                String UserPhoneKey = Paper.book().read(Prevalent.UserPhoneKey);
                String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

                //Remember me code
                if (UserPhoneKey != "" && UserPasswordKey != "") {
                    if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                        AllowAccess(UserPhoneKey, UserPasswordKey);

                        loadingBar.setTitle("Already Logged in");
                        loadingBar.setMessage("Please Wait.....");
                        loadingBar.setCanceledOnTouchOutside(false);
                        loadingBar.show();

                    } else {
                        finish();
                        Intent i = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(i);
                    }
                } else {
                    finish();
                    Intent i = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(i);
                }

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        }); // Splash Screen

    } // on create end

    //Remember me code
    private void AllowAccess(final String phone, final String password) {

        final DatabaseReference RootRef;
        RootRef = FirebaseDatabase.getInstance().getReference();

        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.child("Users").child(phone).exists()) {
                    Users usersData = dataSnapshot.child("Users").child(phone).getValue(Users.class);

                    if (usersData.getPhone().equals(phone)) {
                        if (usersData.getPassword().equals(password)) {
                            Toast.makeText(SplashActivity.this, "You are already logged in...", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();

                            Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
                            //Prevalent.currentOnlineUser = usersData;
                            Prevalent.currentOnlineUser = usersData;
                            startActivity(intent);
                            finish();

                        } else {
                            loadingBar.dismiss();
                            Toast.makeText(SplashActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(SplashActivity.this, "Account with this " + phone + " number do not exists.", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
