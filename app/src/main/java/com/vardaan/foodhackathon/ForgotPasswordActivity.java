package com.vardaan.foodhackathon;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.Bind;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener{

    EditText _emailText;
    Button _verificationLink;
    // @Bind(R.id.btn_verification) Button _verificationLink;

    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;

    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        progressDialog = new ProgressDialog(this);


        _emailText = (EditText) findViewById(R.id.input_email);
        _verificationLink = (Button) findViewById(R.id.btn_verification);
        _verificationLink.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if (v  == _verificationLink){

            verification();
        }
    }

    private void verification() {

        String email = _emailText.getText().toString();
        if (TextUtils.isEmpty(email)){

            _emailText.setError("Enter email");
            return;
        }

        progressDialog.setMessage("Please wait..");
        progressDialog.show();

        firebaseAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        progressDialog.dismiss();

                        if (task.isSuccessful()){

                            startActivity(new Intent(getApplicationContext(), LoginActivity.class));

                            Toast.makeText(ForgotPasswordActivity.this, "Email Sent", Toast.LENGTH_SHORT).show();
                        }
                        else {

                            try {
                                throw task.getException();
                            }
                            catch (FirebaseAuthInvalidUserException e){

                                _emailText.setError("Email not Registered");
                                _emailText.requestFocus();
                            }
                            catch (Exception e){

                                Toast.makeText(ForgotPasswordActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                            }
                        }

                    }
                });

    }
}
