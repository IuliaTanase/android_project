package com.example.proiect.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.LoginActivity;
import com.example.proiect.MainActivity;
import com.example.proiect.R;
import com.example.proiect.utils.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DetailsFragment extends Fragment {

    public static final String STARS = "stars";
    public static final String RATING_FOR_THIS_APP = "ratingForThisApp";
    private EditText etFullName;
    private EditText etEmail;
    private DatabaseReference db;
    private FirebaseAuth fab_auth;
    private FirebaseUser user;
    private Button btnUpdate;
    private Button btnLogout;

    private TextView tvRateApp;
    private TextView tvDetails;
    private RatingBar ratingBar;

    private TextView tvFullName;
    private TextView tvEmail;

    private SharedPreferences preferences;
    private SharedPreferences prefs;

    public DetailsFragment() { }

  
    public static DetailsFragment newInstance() {
        DetailsFragment fragment = new DetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        
        View view= inflater.inflate(R.layout.fragment_details, container, false);
        initialize(view);

        return view;
    }

    private void initialize(View view) {
        preferences = requireContext().getSharedPreferences(RATING_FOR_THIS_APP, Context.MODE_PRIVATE);
        prefs = requireContext().getSharedPreferences(MainActivity.THEME_PREF, Context.MODE_PRIVATE);


        etFullName = view.findViewById(R.id.androidele_etDetailsFullName);
        etEmail = view.findViewById(R.id.androidele_etDetailsEmail);
        btnUpdate = view.findViewById(R.id.androidele_btnUpdateDetails);
        btnLogout = view.findViewById(R.id.androidele_btnLogout);

        tvRateApp = view.findViewById(R.id.androidele_tvRateApp);
        tvDetails = view.findViewById(R.id.androidele_tvDetailsIntro);
        ratingBar = view.findViewById(R.id.androidele_ratingBar);

        tvFullName = view.findViewById(R.id.androidele_tvDetailsFullName);
        tvEmail = view.findViewById(R.id.androidele_tvDetailsEmail);

        fab_auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();

        btnUpdate.setOnClickListener(update());
        btnLogout.setOnClickListener(logout());
        tvRateApp.setOnClickListener(rateThisApp());
        settingTexts();

        loadFromSharedPreferences();
        loadFromPref(view);
    }


    private void loadFromSharedPreferences() {
        float stars = preferences.getFloat(STARS, 0);
        ratingBar.setRating(stars);
    }

    private void loadFromPref(View view) {
        boolean switchChecked = prefs.getBoolean(MainActivity.CHECKED, false);
        if(switchChecked) {
            view.setBackground(getResources().getDrawable(R.drawable.black_background));
            setTextColorDarkTheme();
        } else {
            view.setBackground(getResources().getDrawable(R.drawable.app_background));
            setTextColorLightTheme();
        }
    }

    private void setTextColorDarkTheme() {
        etFullName.setTextColor(getResources().getColor(R.color.colorAccent));
        etEmail.setTextColor(getResources().getColor(R.color.colorAccent));

        tvFullName.setTextColor(getResources().getColor(R.color.colorAccent));
        tvEmail.setTextColor(getResources().getColor(R.color.colorAccent));
        tvDetails.setTextColor(getResources().getColor(R.color.colorAccent));
    }

    private void setTextColorLightTheme() {
        etFullName.setTextColor(getResources().getColor(R.color.colorBlack));
        etEmail.setTextColor(getResources().getColor(R.color.colorBlack));

        tvFullName.setTextColor(getResources().getColor(R.color.colorBlack));
        tvEmail.setTextColor(getResources().getColor(R.color.colorBlack));
        tvDetails.setTextColor(getResources().getColor(R.color.colorBlack));
    }

    private void settingTexts() {

        String userID = user.getUid();
        db.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User userOn = dataSnapshot.getValue(User.class);

                if(userOn != null) {
                    etFullName.setText(userOn.getFullName());
                    etEmail.setText(userOn.getEmail());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getView().getContext(),getString(R.string.androidele_errLoadingData),Toast.LENGTH_SHORT).show();
            }
        });
    }


    private View.OnClickListener logout() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fab_auth.signOut();
                Toast.makeText(getContext(),"Logout successfully!",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), LoginActivity.class);
                startActivity(intent);
            }
        };
    }

    private View.OnClickListener update() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()) {
                    String newName = etFullName.getText().toString().trim();
                    String newEmail = etEmail.getText().toString().trim();
                    db.child(user.getUid()).child("fullName").setValue(newName);
                    db.child(user.getUid()).child("email").setValue(newEmail);
                    user.updateEmail(newEmail);

                    Toast.makeText(getContext(), getString(R.string.androidele_updatedSuccess), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getContext(),"No update has been done.",Toast.LENGTH_SHORT).show();
                }
            }
        };
    }

    private View.OnClickListener rateThisApp() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float stars = ratingBar.getRating();

                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat(STARS, stars);
                editor.apply();

                Toast.makeText(getContext(), getString(R.string.androidele_you_rated_app), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        };
    }

    private boolean validate() {

        String fullName = etFullName.getText().toString().trim();
        if(fullName.isEmpty()) {
            etFullName.setError(getString(R.string.androidele_FullNameRequired));
            etFullName.requestFocus();
            return false;
        }

        final String email = etEmail.getText().toString().trim();
        if(email.isEmpty()) {
            etEmail.setError(getString(R.string.androidele_emailRequired));
            etEmail.requestFocus();
            return false;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError(getString(R.string.androidele_invalidEmail));
            etEmail.requestFocus();
            return false;
        }

        return true;
    }
}