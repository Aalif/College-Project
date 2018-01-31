package com.example.ak.savefood;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.net.URL;
import java.security.PrivateKey;

public class PostActivity extends AppCompatActivity {
    //image
    private ImageButton pImage;
    private static final int GALLERY_INTENT = 2;
    private Uri Pimageuri = null;
    private FirebaseAuth mAuth;
    private FirebaseUser mcurrent_user;
   //declare post variable

    private EditText pTitle;
    private EditText pDiscription;
    private EditText pAddress;
    private EditText pNumber;
    private Button pSubmit;
    //for storage reference
    private StorageReference mStorage;
    //for database reference
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    //for progree bar
    private ProgressDialog mProgress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);
        pImage = (ImageButton)findViewById(R.id.donationimage);
        pTitle = (EditText)findViewById(R.id.donationtitle);
        pDiscription = (EditText)findViewById(R.id.donationdiscription);
        pAddress = (EditText)findViewById(R.id.donationaddress);
        pNumber = (EditText)findViewById(R.id.donationphonenumber);
        pSubmit =(Button)findViewById(R.id.btn_donation);
        //storage
        mStorage = FirebaseStorage.getInstance().getReference();
        //auth
        mAuth = FirebaseAuth.getInstance();
        mcurrent_user = mAuth.getCurrentUser();
        //database
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users").child(mcurrent_user.getUid());
        //progress bar
        mProgress = new ProgressDialog(this);
        //for image
        pImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,GALLERY_INTENT);
            }
        });


        //btn onclik
        pSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             start_Posting() ;

            }
        });
    }
//for image

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == GALLERY_INTENT && resultCode == RESULT_OK){
            Pimageuri = data.getData();
            pImage.setImageURI(Pimageuri);
        }
    }
//for posting data
    private void start_Posting() {

        final String title_post = pTitle.getText().toString().trim();
        final String discription_post = pDiscription.getText().toString().trim();
        final String address_post = pAddress.getText().toString().trim();
        final String number_post = pNumber.getText().toString().trim();
        if(!TextUtils.isEmpty(title_post) && !TextUtils.isEmpty(discription_post) && !TextUtils.isEmpty(address_post) && !TextUtils.isEmpty(number_post) && pImage !=null){
            mProgress.setMessage("Posting Your Donation...");
            mProgress.show();
            //for image
            StorageReference filepath = mStorage.child("Donation_Image").child(Pimageuri.getLastPathSegment());
            filepath.putFile(Pimageuri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                    @SuppressWarnings("VisibleForTests") final Uri downloadUrl = taskSnapshot.getDownloadUrl();
                    //posting
                    final DatabaseReference newpost = mDatabase.push();

                    mDatabaseUsers.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            newpost.child("Title").setValue(title_post);
                            newpost.child("Discription").setValue(discription_post);
                            newpost.child("Address").setValue(address_post);
                            newpost.child("Number").setValue(number_post);
                            newpost.child("Image").setValue(downloadUrl.toString());
                            newpost.child("uId").setValue(mcurrent_user.getUid());
                            newpost.child("User_name").setValue(dataSnapshot.child("Name").getValue()).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    if(task.isSuccessful()){
                                       startActivity(new Intent(PostActivity.this,MainActivity.class));
                                    }
                                    else {
                                        Toast.makeText(PostActivity.this,"Post Failed. Try Again..",Toast.LENGTH_LONG).show();
                                        mProgress.dismiss();
                                    }
                                }
                            });
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                    mProgress.dismiss();


                }
            });

        }
        else {
            Toast.makeText(PostActivity.this,"Fill All the requirement",Toast.LENGTH_LONG).show();
        }
    }
}
