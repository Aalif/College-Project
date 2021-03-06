package com.example.ak.savefood;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {
    //for firebaseauth
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    //declare recycleview
    private RecyclerView mDonationPost;
    //database reference
    private DatabaseReference mDatabase;
    private DatabaseReference mDatabaseUsers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //firebase auth
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser() == null){
                    Intent loginIntent = new Intent(MainActivity.this,LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(loginIntent);
                }
            }
        };
        //fireasedata
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Donation");
        mDatabaseUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseUsers.keepSynced(true);
        mDatabase.keepSynced(true);
        //recylerview setting and declartion
        mDonationPost = (RecyclerView)findViewById(R.id.main_donation_post);
        mDonationPost.setHasFixedSize(true);
        mDonationPost.setLayoutManager(new LinearLayoutManager(this));
    }
    //for view

    @Override
    protected void onStart() {
        super.onStart();
        //Authentication
        mAuth.addAuthStateListener(mAuthListener);
        //firebase adapter
        FirebaseRecyclerAdapter<blog,Blogviewholder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<blog, Blogviewholder>(
                blog.class,
                R.layout.donation_post,
                Blogviewholder.class,
                mDatabase

        ) {
            @Override
            protected void populateViewHolder(Blogviewholder viewHolder, blog model, int position) {
                //get keyy
                final String post_key = getRef(position).getKey();
                //antoher
                viewHolder.set_title(model.getTitle());
                viewHolder.set_discription((model.getDiscription()));
                viewHolder.set_Image(getApplicationContext(),model.getImage());
                viewHolder.set_Username(model.getUser_name());

                //for recyclerview onclik
                viewHolder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                      //  Toast.makeText(MainActivity.this,"You click this",Toast.LENGTH_LONG).show();
                        Intent singlePost = new Intent(MainActivity.this,SinglepostActivity.class);
                        singlePost.putExtra("post_id", post_key);
                        startActivity(singlePost);
                    }
                });
            }
        };
        mDonationPost.setAdapter(firebaseRecyclerAdapter);
    }
    //for recyclerviewholder
    public static class Blogviewholder extends RecyclerView.ViewHolder{

        View mView;
        public Blogviewholder(View itemView) {
            super(itemView);
             mView = itemView;
        }
        //for title
        public void set_title(String title){
            TextView Post_title = (TextView)mView.findViewById(R.id.gdonationtitle);
            Post_title.setText(title);
        }
        public void set_discription(String discription){
            TextView Post_discription = (TextView)mView.findViewById(R.id.gdonationdiscription);
            Post_discription.setText(discription);
        }
        //for username
        public void set_Username(String username){
            TextView Username = (TextView)mView.findViewById(R.id.gdonationusername);
            Username.setText(username);
        }
        public void set_Image(final Context ctx,final String image){
            final ImageView Post_image = (ImageView)mView.findViewById(R.id.gdonationimg);
            //Picasso.with(ctx).load(image).into(Post_image);
            Picasso.with(ctx).load(image).networkPolicy(NetworkPolicy.OFFLINE).into(Post_image, new Callback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError() {
                    Picasso.with(ctx).load(image).into(Post_image);
                }
            });
        }
    }

    //add menu item for post

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
    // open menu item for

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_add){
            startActivity(new Intent(MainActivity.this,PostActivity.class));
        }
        if(item.getItemId() == R.id.action_mypost){
            startActivity(new Intent(MainActivity.this,MypostActivity.class));
        }
        if(item.getItemId() == R.id.action_logout){
            logout();
        }
        return super.onOptionsItemSelected(item);
    }
    //logout
    private void logout() {
        mAuth.signOut();
    }


}
