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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    //variable call
    private EditText mName;
    private EditText mEmail;
    private EditText mPass;
    private EditText mNumber;
    private EditText mAddress;
    private Button mRegister;
    //firebase auth
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    //progreess
    private ProgressDialog mProg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //progree
        mProg = new ProgressDialog(this);
        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users");
        //getting value
        mName = (EditText)findViewById(R.id.rname);
        mEmail = (EditText)findViewById(R.id.rEmail);
        mPass = (EditText)findViewById(R.id.rPass);
        mNumber = (EditText)findViewById(R.id.rPhone);
        mAddress = (EditText)findViewById(R.id.rAddress);
        mRegister = (Button)findViewById(R.id.rButton);

        //onclick
        mRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startRegister();
            }
            //register button method
            private void startRegister() {
                final String rName = mName.getText().toString().trim();
                String rEmail = mEmail.getText().toString().trim();
                String rPass = mPass.getText().toString().trim();
                final String rNumber = mNumber.getText().toString().trim();
                final String rAddress = mAddress.getText().toString().trim();
                if(!TextUtils.isEmpty(rName) && !TextUtils.isEmpty(rEmail) && !TextUtils.isEmpty(rPass) && !TextUtils.isEmpty(rAddress) && !TextUtils.isEmpty(rNumber) ){
                    //progree
                    mProg.setMessage("Signing Up...");
                    mProg.show();
                    //authing
                    mAuth.createUserWithEmailAndPassword(rEmail,rPass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    //getdata
                                    String user_id = mAuth.getCurrentUser().getUid();
                                    DatabaseReference current_user_db = mDatabase.child(user_id);
                                    current_user_db.child("Name").setValue(rName);
                                    current_user_db.child("Number").setValue(rNumber);
                                    current_user_db.child("Address").setValue(rAddress);
                                    mProg.dismiss();
                                    //goint main intent
                                    Intent mainIntent = new Intent(RegisterActivity.this,MainActivity.class);
                                    mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(mainIntent);
                                }
                                else {
                                    Toast.makeText(RegisterActivity.this,"Error Signing Up..",Toast.LENGTH_LONG).show();
                                    mProg.dismiss();
                                }
                        }
                    });
                }
                else {
                    Toast.makeText(RegisterActivity.this,"Fill All the requirement",Toast.LENGTH_LONG).show();
                }


            }

        });
    }
}
