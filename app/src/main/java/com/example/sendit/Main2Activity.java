
package com.example.sendit;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.sendit.Fragments.AboutFragment;
import com.example.sendit.Fragments.RecentFragment;
import com.example.sendit.Fragments.RoomFragment;
import com.example.sendit.Fragments.SettingActivity;
import com.example.sendit.Fragments.UsersFragment;
import com.example.sendit.Model.UserData;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class Main2Activity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

        DatabaseReference reference;
        FirebaseUser firebaseUser;
       static TextView nav_username,nav_email;
       static CircleImageView profile_image;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Chat Box");
        setSupportActionBar(toolbar);

        //Firebase Data Retrieving
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nav_email = findViewById(R.id.email_id_nav);
                profile_image = findViewById(R.id.image_user_name);
                nav_username = findViewById(R.id.username_nav);
                UserData user = dataSnapshot.getValue(UserData.class);

                assert user != null;
                nav_username.setText(user.getUsername());
                nav_email.setText(firebaseUser.getEmail());
                if (user.getImageURL().equals("default")) {
                    profile_image.setImageResource(R.mipmap.ic_launcher);
                } else {

                    Glide.with(getApplicationContext()).load(user.getImageURL()).into(profile_image);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}

        });
        

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(savedInstanceState==null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecentFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_recents);

        }

    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logout) {

            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(Main2Activity.this,MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            return true;
        }
        if(id==R.id.settings){
            Intent intent = new Intent(Main2Activity.this, SettingActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_room) {
            toolbar.setTitle("Global Room");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RoomFragment()).commit();
        } else if (id == R.id.nav_recents) {
            toolbar.setTitle("Recents");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecentFragment()).commit();
        } else if (id == R.id.nav_users) {
            toolbar.setTitle("Users");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new UsersFragment()).commit();
        } else if (id == R.id.nav_about) {
            toolbar.setTitle("About us");
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new AboutFragment()).commit();
        } else if (id == R.id.nav_share) {

            ApplicationInfo app = getApplicationContext().getApplicationInfo();
            String filePath = app.publicSourceDir;
            Intent intent  = new Intent(android.content.Intent.ACTION_SEND);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
            Uri uri = Uri.parse(filePath);
            intent.setType("*/*");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent,"Share Using"));
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void status(String status){
        reference = FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
        HashMap<String,Object> hashMap = new HashMap<>();

        hashMap.put("status",status);
        reference.updateChildren(hashMap);
    }
    @Override
    protected void onResume() {

        super.onResume();
        status("online");
    }
    @Override
    protected void onPause() {

        super.onPause();
        status("offline");
    }

}
