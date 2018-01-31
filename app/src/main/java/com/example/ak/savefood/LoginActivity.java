package com.example.ak.savefood;

import android.app.ProgressDialog;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class LoginActivity extends AppCompatActivity {
    //for firebaseauth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseUser;
    //progreess
    private ProgressDialog mProg;
    //for login Variable
    private EditText loginName;
    private EditText loginPass;
    private Button loginButton;
    private Button gotoSignUpbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //progree
        mProg = new ProgressDialog(this);
        //for auth
        mAuth = FirebaseAuth.getInstance();
        mDatabaseUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUser.keepSynced(true);
        //for variable
        loginName = (EditText)findViewById(R.id.lemail);
        loginPass = (EditText)findViewById(R.id.lpass);
        loginButton = (Button) findViewById(R.id.lbtn);
        gotoSignUpbtn = (Button)findViewById(R.id.gotosignbtn);
        //btn goto sign in
        gotoSignUpbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
        //btn login click
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkLogin();
            }
        });
    }

    private void checkLogin() {
        String lEmail = loginName.getText().toString().trim();
        String lPass = loginPass.getText().toString().trim();
        if(!TextUtils.isEmpty(lEmail) && !TextUtils.isEmpty(lPass)){
            //progree
            mProg.setMessage("Checking Log In ...");
            mProg.show();
            mAuth.signInWithEmailAndPassword(lEmail,lPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        checkUserexist();
                        mProg.dismiss();
                    }
                    else {
                        Toast.makeText(LoginActivity.this,"Error Login..",Toast.LENGTH_LONG).show();
                        mProg.dismiss();
                    }
                }
            });
        }
        else {
            Toast.makeText(LoginActivity.this,"Fill the email & password..",Toast.LENGTH_LONG).show();
        }
    }
        // for user existance
    private void checkUserexist() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseUser.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                if(dataSnapshot.hasChild(user_id)){
                    Intent mainIntent = new Intent(LoginActivity.this,MainActivity.class);
                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(mainIntent);
                }
                else {
                    Toast.makeText(LoginActivity.this,"You need to setup your account..",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
