package com.example.proiect;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.Models.User;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class MenuActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView tv_owner;
    private TextView tv_email;
    private FirebaseUser user;
    private DatabaseReference db;
    NavigationView navigation;
    View nav_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        configNavig();
    }

    private void configNavig(){

        Toolbar toolbar = findViewById(R.id.androidele_toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();


        navigation = findViewById(R.id.androidele_navView);
        nav_layout = navigation.getHeaderView(0);
        tv_owner = nav_layout.findViewById(R.id.androidele_tvMenu);
        tv_email = nav_layout.findViewById(R.id.androidele_tvMenu2);

        //pentru baza de date luam userul curent si referinta pentru tabelul Users
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Users");

        //metoda pentru a scoate din baza de date atributele userului si a crea obiect de tip user
        //pentru a popula cele 2 textView-uri cu valorile atributelor
        settingTexts();

    }

    private void settingTexts() {

        String userID = user.getUid();
        db.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userOn = dataSnapshot.getValue(User.class);

                if(userOn!=null){
                    String name = getString(R.string.androidele_owner,userOn.getFullName());
                    tv_owner.setText(name);
                    tv_email.setText(userOn.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getApplicationContext(),getString(R.string.androidele_errLoadingData),Toast.LENGTH_SHORT).show();
            }
        });
    }

}