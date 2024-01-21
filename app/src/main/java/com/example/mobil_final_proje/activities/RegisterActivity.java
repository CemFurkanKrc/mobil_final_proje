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
import com.example.mobil_final_proje.model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    EditText ad,soyad,mail,password;
    Button login ,signup;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ad=findViewById(R.id.sgn_etx_nm);
        soyad=findViewById(R.id.sgn_etx_syd);
        mail=findViewById(R.id.sgn_etx_mail);
        password=findViewById(R.id.sgn_etx_psw);
        login=findViewById(R.id.sgn_btn_lgn);
        signup=findViewById(R.id.sgn_btn_sgn);
        auth=FirebaseAuth.getInstance();

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LoginActivity.class) );
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strname= ad.getText().toString();
                String strlastname=soyad.getText().toString();
                String stremail= mail.getText().toString();
                String strpaswrd=password.getText().toString();
                if (strname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "ad alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strlastname.isEmpty()){
                    Toast.makeText(getApplicationContext(), "soyad alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (stremail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "email alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (strpaswrd.isEmpty()){
                    Toast.makeText(getApplicationContext(), "password alanı boş olamaz", Toast.LENGTH_SHORT).show();
                    return;
                }
                auth.createUserWithEmailAndPassword(stremail,strpaswrd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            String UİD=task.getResult().getUser().getUid();

                            Toast.makeText(getApplicationContext(), "Kayıt Başarılı", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(),LoginActivity.class) );

                            FirebaseFirestore db= FirebaseFirestore.getInstance();
                            CollectionReference ref = db.collection("USERS").document(UİD).collection(UİD);

                            UserModel user = new UserModel(strname,strlastname,stremail,UİD);
                            ref.add(user);
                        }else {
                            Toast.makeText(getApplicationContext(), "Kayıt Başarısız", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}