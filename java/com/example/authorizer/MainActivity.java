package com.example.authorizer;

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
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    TextInputEditText email;
    TextInputEditText password;
    private Button signin,signup;
    private TextView register;
    private FirebaseAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fAuth = FirebaseAuth.getInstance();
        email = (TextInputEditText)findViewById(R.id.emaill);
        password = (TextInputEditText)findViewById(R.id.passs);
        signin = (Button) findViewById(R.id.b1);
        signin.setOnClickListener(this);
        signup = (Button) findViewById(R.id.B5);
        signup.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.B5:
                startActivity(new Intent(this,SIGNUP.class));
                break;
            case R.id.b1:
                Login();
                break;
        }
    }

    private void Login() {

        String e = email.getText().toString().trim();
        String p = password.getText().toString().trim();

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

        fAuth.signInWithEmailAndPassword(e,p).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()){
                    startActivity(new Intent(MainActivity.this,Home.class));
                }
                else{
                    Toast.makeText(MainActivity.this,"Check your email and Password",Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}