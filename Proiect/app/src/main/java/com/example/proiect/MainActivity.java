package com.example.proiect;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.Models.Apartment;
import com.example.proiect.Models.Tenant;
import com.example.proiect.Models.User;
import com.example.proiect.fragments.ApartmentsFragment;
import com.example.proiect.fragments.DetailsFragment;
import com.example.proiect.fragments.TenantsFragment;
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
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    private TextView tv_owner;
    private TextView tv_email;
    private FirebaseUser user;
    private DatabaseReference db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    View nav_layout;

    private ImageView imageMan;
    private TextView motto;

    private Fragment currentFragment;
    private ArrayList<Apartment> apartments = new ArrayList<>();
    private ArrayList<Tenant> tenants = new ArrayList<>();
    Animation Coming;
    Animation ComeToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configNavig();
        addApartments(apartments);
        //openDefaultFragment(savedInstanceState);
    }


    private NavigationView.OnNavigationItemSelectedListener addNavigationMenuItemSelectedEvent() {


        return new NavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                    setVisibilityImage(item.getItemId());
                    if (item.getItemId() == R.id.androidele_apartments) {

                        //deschid fragment de apartaments
                        currentFragment = ApartmentsFragment.newInstance(apartments);
                        //navigationView.setCheckedItem(R.id.androidele_apartments);
                    }

                    if (item.getItemId() == R.id.androidele_chiriasi) {
                        //deschid fragment de chiriasi
                        currentFragment = TenantsFragment.newInstance(tenants,apartments);
                        //navigationView.setCheckedItem(R.id.androidele_chiriasi);
                    }

                    if(item.getItemId() == R.id.androidele_details){
                        imageMan.setVisibility(View.INVISIBLE);
                        motto.setVisibility(View.INVISIBLE);
                        currentFragment = DetailsFragment.newInstance();
                    }
                    Toast.makeText(getApplicationContext(), getString(R.string.show_pressed_option, item.getTitle()), Toast.LENGTH_SHORT).show();
                    openFragment();
                    drawerLayout.closeDrawer(GravityCompat.START);
                    return true;
                }
        };
    }

    private void configNavig(){

        Toolbar toolbar = findViewById(R.id.androidele_toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        initializeComponents();
        //metoda pentru a scoate din baza de date atributele userului si a crea obiect de tip user
        //pentru a popula cele 2 textView-uri cu valorile atributelor
        settingTexts();

    }

   public void setVisibilityImage(int id){
        if(id ==R.id.androidele_apartments){
            if(apartments.size()==0){
                imageMan.setVisibility(View.VISIBLE);
                motto.setVisibility(View.VISIBLE);
            }else
            {
                imageMan.setVisibility(View.INVISIBLE);
                motto.setVisibility(View.INVISIBLE);

            }
        }else
        {
            if(tenants.size()==0){
                imageMan.setVisibility(View.VISIBLE);
                motto.setVisibility(View.VISIBLE);

            }else
            {
                imageMan.setVisibility(View.INVISIBLE);
                motto.setVisibility(View.INVISIBLE);

            }
        }
   }

    private void initializeComponents() {
        navigationView = findViewById(R.id.androidele_navView);
        navigationView.setNavigationItemSelectedListener(addNavigationMenuItemSelectedEvent());
        nav_layout = navigationView.getHeaderView(0);
        tv_owner = nav_layout.findViewById(R.id.androidele_tvMenu);
        tv_email = nav_layout.findViewById(R.id.androidele_tvMenu2);
        motto = findViewById(R.id.androidele_tvMotto);
        imageMan = findViewById(R.id.androidele_ownerImg);
        imageMan.setVisibility(View.VISIBLE);
        //asociem obiectului animatie contextul si fisierul xml come_up
        Coming = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.come_up);
        ComeToLeft = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.come_left);
        //imaginea incepe sa realizeze animatia
        imageMan.startAnimation(Coming);
        motto.startAnimation(ComeToLeft);


        //pentru baza de date luam userul curent si referinta pentru tabelul Users
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Users");
    }

    public void settingTexts() {

        String userID = user.getUid();
        db.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userOn = dataSnapshot.getValue(User.class);

                if(userOn != null) {
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


    private void openFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.androidele_frameLayout, currentFragment)
                .commit();
    }

    private void openDefaultFragment(Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            currentFragment = ApartmentsFragment.newInstance(apartments);
            openFragment();
            navigationView.setCheckedItem(R.id.androidele_apartments);
        }
    }

    private void addApartments(List<Apartment> apartments) {
        Tenant tenant = new Tenant(1,"Georgescu Mihai", "+40751348966");
        tenants.add(tenant);
        apartments.add(new Apartment("ap1", 2, 350, "Dristor 97-119", "Complet mobilat si utilat", false, new Date(), tenant));
        apartments.add(new Apartment("ap2", 1, 290, "Trapezului 56", "Bucatarie open-space", true, new Date(), new Tenant(2, "Radu", "0723456789")));
        apartments.add(new Apartment("ap3", 3, 420, "Pipera", "Apartament lux prima inchiriere", false, new Date(), null));
    }

}