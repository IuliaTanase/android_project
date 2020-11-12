package com.example.proiect;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText et_email, et_password;
    private Button btn_login;
    private TextView tv_register;
    FirebaseAuth fb_authentication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initialize();
    }

    private void initialize() {

        et_email = findViewById(R.id.androidele_etEmail);
        et_password = findViewById(R.id.androidele_etPassword);
        btn_login = findViewById(R.id.androidele_btnLogin);
        fb_authentication = FirebaseAuth.getInstance();
        tv_register = findViewById(R.id.androidele_tvRegister);
        tv_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(i);
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
    }

    private void login() {

        String email = et_email.getText().toString();
        String pass = et_password.getText().toString();

        if(validation(email,pass)){
            fb_authentication.signInWithEmailAndPassword(email,pass)
                    .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(getApplicationContext(),MenuActivity.class);
                                startActivity(i);
                            }else
                            {
                                Toast.makeText(getApplicationContext(), R.string.androidele_loginFailed,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }

    private boolean validation(String email, String pass){
        if(email.isEmpty()){
            et_email.setError(getString(R.string.androidele_emailRequried));
            et_email.requestFocus();
            return false;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError(getString(R.string.androidele_invalidEmail));
            et_email.requestFocus();
            return false;
        }

        if(pass.isEmpty()){
            et_password.setError(getString(R.string.androidele_completePass));
            et_password.requestFocus();
            return false;
        }
        if(pass.length() < 8){
            et_password.setError("Password should has at least 8 characters!");
            et_password.requestFocus();
            return false;
        }
        return true;
    }
}