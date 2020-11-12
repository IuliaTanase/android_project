package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PatternMatcher;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.proiect.Models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.sql.SQLOutput;

import static android.widget.Toast.*;

public class RegisterActivity extends AppCompatActivity {

    private EditText et_fullName;
    private EditText et_email;
    private EditText et_password;
    private Button register;
    private FirebaseAuth fb_atuthentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initialize();
    }

    private void initialize() {
        et_fullName = findViewById(R.id.androidele_etFullName);
        et_email = findViewById(R.id.androidele_etEmail_register);
        et_password = findViewById(R.id.androidele_etPassword_register);
        register = findViewById(R.id.androidele_btnRegister);
        fb_atuthentication = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation_register();
            }
        });
    }

    private void validation_register(){
        final String fullName = et_fullName.getText().toString();
        if( fullName.isEmpty()){
            et_fullName.setError(getString(R.string.androidele_FullNameRequired));
            et_fullName.requestFocus();
            return;
        }

        final String email = et_email.getText().toString().trim();
        if(email.isEmpty()){
            et_email.setError(getString(R.string.androidele_emailRequried));
            et_email.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError(getString(R.string.androidele_invalidEmail));
            et_email.requestFocus();
            return;
        }

        final String pass = et_password.getText().toString();
        if(pass.isEmpty()){
            et_password.setError(getString(R.string.androidele_completePass));
            et_password.requestFocus();
            return;
        }
        if(pass.length() < 8){
            et_password.setError("Password should has at least 8 characters!");
            et_password.requestFocus();
            return;
        }

        //in cazul in care toate datele au fost introduse in campuri
        fb_atuthentication.createUserWithEmailAndPassword(email,pass)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        System.out.println(fullName);
                        if (task.isSuccessful()) {
                            User user = new User(fullName, email, pass);

                            System.out.println("user");;
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(addUser(user));
                        } else {
                            Toast.makeText(getApplicationContext(), R.string.androide_registreFailed, LENGTH_SHORT).show();

                        }

                    }
                    });
    }

    private OnCompleteListener<Void> addUser(final User user){
        return new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    String message = getString(R.string.androide_paramSuccessfully,user.getFullName());
                    Toast.makeText(getApplicationContext(), message, LENGTH_SHORT).show();
                    System.out.println(user.getFullName() + " has been added");
                    Intent i = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                } else {
                    String message = getString(R.string.androide_paramFailed,user.getFullName());
                    Toast.makeText(getApplicationContext(), message, LENGTH_SHORT).show();
                    System.out.println(user.getFullName() + " failed registration");
                }
            }
        };
    }

}



