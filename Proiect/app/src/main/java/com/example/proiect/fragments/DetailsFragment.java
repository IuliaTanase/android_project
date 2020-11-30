package com.example.proiect.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.proiect.LoginActivity;
import com.example.proiect.MainActivity;
import com.example.proiect.Models.User;
import com.example.proiect.R;
import com.example.proiect.asyncTask.AsyncTaskRunner;
import com.example.proiect.asyncTask.Callback;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.Callable;


public class DetailsFragment extends Fragment {


    EditText etFullName;
    EditText etEmail;
    DatabaseReference db;
    FirebaseAuth fab_auth;
    FirebaseUser user;
    Button btnUpdate;
    Button btnLogout;
    TextView tv_owner;



    public DetailsFragment() {
       
    }

  
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

        etFullName = view.findViewById(R.id.androidele_etDetailsFullName);
        etEmail = view.findViewById(R.id.androidele_etDetailsEmail);
        btnUpdate = view.findViewById(R.id.androidele_btnUpdateDetails);
        btnLogout = view.findViewById(R.id.androidele_btnLogout);
        fab_auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance().getReference("Users");
        user = FirebaseAuth.getInstance().getCurrentUser();
        btnUpdate.setOnClickListener(update());
        btnLogout.setOnClickListener(logout());
        settingTexts();
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

    private void settingTexts(User result) {

        if(result != null) {
            etFullName.setText(result.getFullName());
            etEmail.setText(result.getEmail());
        }
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
                }else
                {
                    Toast.makeText(getContext(),"No update has been done.",Toast.LENGTH_SHORT).show();
                }
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