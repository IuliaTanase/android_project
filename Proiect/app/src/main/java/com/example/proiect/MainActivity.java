package com.example.proiect;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.fragments.ApartmentsFragment;
import com.example.proiect.fragments.DetailsFragment;
import com.example.proiect.fragments.InvoiceFragment;
import com.example.proiect.fragments.LocationsFragment;
import com.example.proiect.fragments.UtilityFragment;
import com.example.proiect.utils.Apartment;
import com.example.proiect.utils.Location;
import com.example.proiect.utils.Tenant;
import com.example.proiect.utils.User;

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
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String CHECKED = "CHECKED";
    private SharedPreferences preferences;
    public static final String THEME_PREF = "themePref";

    private TextView tv_owner;
    private TextView tv_email;

    private FirebaseUser user;
    private DatabaseReference db;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    View nav_layout;

    private ImageView imageMan;
    private TextView motto;

    private SwitchCompat sw;
    private Fragment currentFragment;
    private FrameLayout frameLayout;

    private ArrayList<Apartment> apartments = new ArrayList<>();
    private ArrayList<Tenant> tenants = new ArrayList<>();
    private ArrayList<Location> locations = new ArrayList<>();

    Animation Coming;
    Animation ComeToLeft;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        configNavig();

        sw.setOnCheckedChangeListener(switchSetTheme());
        loadFromSharedPreferences();
    }

    private void configNavig() {

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

    private void initializeComponents() {
        sw = findViewById(R.id.androidele_buttonSwitch);

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

        frameLayout = findViewById(R.id.androidele_frameLayout);

        //pentru baza de date luam userul curent si referinta pentru tabelul Users
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseDatabase.getInstance().getReference("Users");

        preferences = getSharedPreferences(THEME_PREF, MODE_PRIVATE);
    }

    private NavigationView.OnNavigationItemSelectedListener addNavigationMenuItemSelectedEvent() {
        return new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected (@NonNull MenuItem item) {
                setVisibilityImage(item.getItemId());
                if (item.getItemId() == R.id.androidele_apartments) {
                    currentFragment = ApartmentsFragment.newInstance(apartments, tenants);
                }

                if (item.getItemId() == R.id.androidele_chiriasi) {
                    currentFragment = TenantsFragment.newInstance(tenants, apartments);
                }

                if(item.getItemId() == R.id.androidele_details){
                    setViewsInvisible();
                    currentFragment = DetailsFragment.newInstance();
                }

                if (item.getItemId() == R.id.androidele_availableLocations) {
                    currentFragment = LocationsFragment.newInstance(locations);
                }

                if(item.getItemId() == R.id.androidele_utilities){
                    setViewsInvisible();
                    currentFragment = UtilityFragment.newInstance();
                }

                if(item.getItemId() == R.id.androidele_invoices){
                    setViewsInvisible();
                    currentFragment = InvoiceFragment.newInstance();
                }



                Toast.makeText(getApplicationContext(), getString(R.string.show_pressed_option, item.getTitle()), Toast.LENGTH_SHORT).show();
                openFragment();
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        };
    }

    private CompoundButton.OnCheckedChangeListener switchSetTheme() {
        return new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    setDarkTheme();
                    Toast.makeText(getApplicationContext(), R.string.androidele_dark_mode_activated, Toast.LENGTH_SHORT).show();
                } else {
                    setLightTheme();
                    Toast.makeText(getApplicationContext(), R.string.androidele_light_mode_activated, Toast.LENGTH_SHORT).show();
                }
                saveToSharedPreferences();
            }
        };
    }

    private void setDarkTheme() {
        frameLayout.setBackground(getResources().getDrawable(R.drawable.black_background));
        motto.setTextColor(getResources().getColor(R.color.colorAccent));
        navigationView.setBackground(getResources().getDrawable(R.drawable.black_background));
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
        Toast.makeText(getApplicationContext(), R.string.androidele_dark_mode_activated, Toast.LENGTH_SHORT).show();
    }

    private void setLightTheme() {
        frameLayout.setBackground(getResources().getDrawable(R.drawable.app_background));
        motto.setTextColor(getResources().getColor(R.color.colorBlack));
        navigationView.setBackground(getResources().getDrawable(R.drawable.app_background));
        navigationView.setItemTextColor(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
        navigationView.setItemIconTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorBlack)));
        Toast.makeText(getApplicationContext(), R.string.androidele_light_mode_activated, Toast.LENGTH_SHORT).show();
    }

    public void setVisibilityImage(int id) {
        if(id == R.id.androidele_apartments) {
            if(apartments.size() == 0) {
                setViewsVisible();
            } else {
                setViewsInvisible();
            }
        } else {
            if(tenants.size() == 0) {
                setViewsVisible();
            } else {
                setViewsInvisible();
            }
        }
   }


   private void setViewsVisible() {
       imageMan.setVisibility(View.VISIBLE);
       motto.setVisibility(View.VISIBLE);
   }

    private void setViewsInvisible() {
        imageMan.setVisibility(View.INVISIBLE);
        motto.setVisibility(View.INVISIBLE);
    }

    private void loadFromSharedPreferences() {
        boolean switchChecked = preferences.getBoolean(CHECKED, false);
        sw.setChecked(switchChecked);
    }

    private void saveToSharedPreferences() {
        boolean switchChecked = sw.isChecked();

        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(CHECKED, switchChecked);
        editor.apply();
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
    
}