package com.example.ahar.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ahar.Model.User;
import com.example.ahar.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistationActivity extends AppCompatActivity {

    private TextView signin;
    private TextInputEditText fullName,email,phone,password,passwordRepeat;
    private Button registrationButton;
    private FirebaseAuth firebaseAuth;

    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registation);

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");

        fullName = findViewById(R.id.name);
        email = findViewById(R.id.eamil);
        password = findViewById(R.id.pass);
        passwordRepeat = findViewById(R.id.passretype);
        registrationButton = findViewById(R.id.reg_button);
        registrationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Register();
                }
        });

        //intent Login page
        signin = findViewById(R.id.signinactivity);
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegistationActivity.this, LogInActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void Register() {
        String FullName = fullName.getText().toString();
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Password_re = passwordRepeat.getText().toString();
//        String uid = firebaseAuth.getCurrentUser().getUid();
//        databaseReference = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        String isUser = "User";

        if (TextUtils.isEmpty(FullName)){
            Toast.makeText(RegistationActivity.this, "please enter your Name", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Email)){
            Toast.makeText(RegistationActivity.this, "please enter Email", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Password)){
            Toast.makeText(RegistationActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (TextUtils.isEmpty(Password_re)){
            Toast.makeText(RegistationActivity.this, "please enter password", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (!Password.equals(Password_re)){
            passwordRepeat.setError("password not match");
            return;
        }
        else if (Password.length()<8){
            Toast.makeText(RegistationActivity.this, "password week", Toast.LENGTH_SHORT).show();
            return;
        }
        else if(!isVallidEmail(Email)){
            email.setError("Envalid email");
            return;
        }

        firebaseAuth.createUserWithEmailAndPassword(Email,Password).addOnCompleteListener(RegistationActivity.this, new OnCompleteListener<AuthResult>() {
//            FirebaseUser user = firebaseAuth.getCurrentUser();
//            String uid = user.getUid();
            @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            User userhelp = new User(FullName,Email,null,isUser,"","","");
                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userhelp);
                            Toast.makeText(RegistationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistationActivity.this,HomeActivity.class);
                            intent.putExtra("email",Email);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(RegistationActivity.this, "Authentication Failed "+task.getException().toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(RegistationActivity.this, RegistationActivity.class);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
    }
    private Boolean isVallidEmail(CharSequence target){
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }
}