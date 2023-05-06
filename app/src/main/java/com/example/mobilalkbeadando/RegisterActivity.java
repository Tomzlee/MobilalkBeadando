package com.example.mobilalkbeadando;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final  int SECRET_KEY = 69;
    EditText userNameET;
    EditText emailET;
    EditText passwordET;
    EditText passwordVerifyET;
    TextView felirat;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Bundle bundle = getIntent().getExtras();
        int secret_key = getIntent().getIntExtra("SECRET_KEY",0);

        if(secret_key != 69){
            finish();
        }

        userNameET = findViewById(R.id.userNameEditText);
        emailET = findViewById(R.id.emailEditText);
        passwordET = findViewById(R.id.passwordEditText);
        passwordVerifyET = findViewById(R.id.passwordVerifyEditText);
        felirat = (TextView) findViewById(R.id.registrationTextView);

        mAuth = FirebaseAuth.getInstance();

    }

    public void register(View view) {
        String userName = userNameET.getText().toString();
        String email = emailET.getText().toString();
        String password = passwordET.getText().toString();
        String passwordVerify = passwordVerifyET.getText().toString();

        if (!password.equals(passwordVerify)){
            felirat.setText("Nem egyeznek a jelszavak");
            return;
        }

        //startShopping();

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    startShopping();
                }else{
                    //valami
                }
            }
        });

    }

    public void cancel(View view) {
        finish();
    }

    private void startShopping(/* registered user data */) {
        Intent intent = new Intent(this,FoOldalActivity.class);
        //intent.putExtra("SECRET_KEY",SECRET_KEY);
        startActivity(intent);
    }
}