package com.example.authorizer;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;


public class SIGNUP extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText username;
    TextInputEditText email;
    TextInputEditText password;
    Button signup;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_s_i_g_n_u_p);

        fAuth = FirebaseAuth.getInstance();
        signup = (Button) findViewById(R.id.b4);
        signup.setOnClickListener(this);

        username = (TextInputEditText) findViewById(R.id.one);
        email = (TextInputEditText) findViewById(R.id.two);
        password = (TextInputEditText) findViewById(R.id.three);


    }

    @Override
    public void onClick(View v) {

       switch (v.getId()){
           case R.id.b4:
               RegisterUser();
               break;
       }

    }

    private void RegisterUser() {

        String n = username.getText().toString().trim();
        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();


        if (n.isEmpty()) {
            password.setError("Username is Required");
            password.requestFocus();
            return;
        }

        if (e.isEmpty()) {
            email.setError("E-Mail Required");
            email.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(e).matches()) {
            email.setError("Enter Valid E-MAIL ");
            email.requestFocus();
            return;
        }

        if (p.isEmpty()) {
            password.setError("Password is Required");
            password.requestFocus();
            return;
        }

        if (p.length() < 6) {
            password.setError("Password must be >=6 characters");
            password.requestFocus();
        }
        fAuth.createUserWithEmailAndPassword(e, p)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            User user = new User(n, e);

                            FirebaseDatabase.getInstance().getReference("User")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if (task.isSuccessful()) {
                                        Toast.makeText(SIGNUP.this, "User Registered", Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(SIGNUP.this,MainActivity.class));
                                    } else {
                                        Toast.makeText(SIGNUP.this, "USER  *** NOT REGISTERED", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(SIGNUP.this, "USER NOT REGISTERED", Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

}