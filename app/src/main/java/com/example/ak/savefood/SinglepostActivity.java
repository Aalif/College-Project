package com.example.ak.savefood;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class SinglepostActivity extends AppCompatActivity {

    private String mPost_key = null;
    //database reference
    private DatabaseReference mDatabase;
    //for variable
    private ImageView mSingleimage;
    private TextView mSingletitle;
    private TextView mSinglediscrption;
    private TextView mSingleaddress;
    private TextView mSinglephone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlepost);
        //fireasedata
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        //data variable refernce
        mSingleimage =(ImageView)findViewById(R.id.singledonationimage);
        mSingletitle = (TextView)findViewById(R.id.singledonationtitle);
        mSinglediscrption = (TextView)findViewById(R.id.singledonationdiscription);
        mSingleaddress = (TextView)findViewById(R.id.singledonationaddress);
        mSinglephone = (TextView)findViewById(R.id.singledonationphonenumber);
        mPost_key = getIntent().getExtras().getString("post_id");
      //  Toast.makeText(SinglepostActivity.this,"This is single" ,Toast.LENGTH_LONG).show();
        mDatabase.child(mPost_key).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String spost_title = (String) dataSnapshot.child("Title").getValue();
                String spost_discrption = (String) dataSnapshot.child("Discription").getValue();
                String spost_address = (String) dataSnapshot.child("Address").getValue();
                String spost_number = (String) dataSnapshot.child("Number").getValue();
                String spost_uid = (String) dataSnapshot.child("uId").getValue();
                String spost_image = (String) dataSnapshot.child("Image").getValue();

                //
                mSingletitle.setText(spost_title);
                mSinglediscrption.setText(spost_discrption);
                mSingleaddress.setText(spost_address);
                mSinglephone.setText(spost_number);

                Picasso.with(SinglepostActivity.this).load(spost_image).into(mSingleimage);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
