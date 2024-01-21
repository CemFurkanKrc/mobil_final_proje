package com.example.mobil_final_proje.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mobil_final_proje.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText mail ,pasword;
    Button login,signup;
    FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        mail=findViewById(R.id.lgn_etx_mail);
        pasword=findViewById(R.id.lgn_etx_psw);
        login=findViewById(R.id.lgn_btn_lgn);
        signup=findViewById(R.id.lgn_btn_sgn);
        auth=FirebaseAuth.getInstance();

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RegisterActivity.class) );
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String stremail= mail.getText().toString();
                String strpaswrd=pasword.getText().toString();

                if (stremail.isEmpty()){
                    Toast.makeText(LoginActivity.this, "email alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strpaswrd.isEmpty()){
                    Toast.makeText(LoginActivity.this, "password alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.signInWithEmailAndPassword(stremail,strpaswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            startActivity(new Intent(getApplicationContext(), MainActivity.class) );
                        }else {
                            Toast.makeText(LoginActivity.this, "email ya da password yanlış", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}