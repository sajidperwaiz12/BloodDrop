package com.example.bloodbankprojecthit;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.bloodbankprojecthit.Models.User;
import com.example.bloodbankprojecthit.databinding.ActivitySignupBinding;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;

    private FirebaseAuth mAuth;
    FirebaseDatabase database;
    ProgressDialog pd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String st[]= getResources().getStringArray(R.array.blood);
        ArrayAdapter<String> ap=new ArrayAdapter<>(this,R.layout.blood_dropdown,st);
        binding.bloodGroup.setAdapter(ap);

        binding.already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });


        // Progress Dialog
        pd = new ProgressDialog(this);
        pd.setTitle("Creating Account");
        pd.setMessage("We are creating your account");
        pd.setCancelable(true);
        pd.setCanceledOnTouchOutside(false);

        // Firebase
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        binding.signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pd.show();
                mAuth.createUserWithEmailAndPassword
                        (binding.email.getText().toString(), binding.setpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        pd.dismiss();
                        if (task.isSuccessful()) {
                            //collect user data
                            User user = collectUserData();
                            String id = task.getResult().getUser().getUid();
                            database.getReference().child("Users").child(id).setValue(user);
                            Toast.makeText(SignupActivity.this, "User Created Successfully", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    // Collect user data
    private User collectUserData() {
        User user = new User();
        user.setName(binding.name.getText().toString());
        user.setContact(binding.phone.getText().toString());
        user.setEmail(binding.email.getText().toString());
        user.setAddress(binding.address.getText().toString());
        user.setBloodGroup(binding.bloodGroup.getText().toString());
        user.setDob(binding.dob.getText().toString());
        user.setGender(binding.gender.getCheckedRadioButtonId());
        user.setPassword(binding.setpassword.getText().toString());
        return user;
    }
}