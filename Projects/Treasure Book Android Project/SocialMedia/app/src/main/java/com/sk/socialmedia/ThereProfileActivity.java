package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.socialmedia.adapters.AdapterPosts;
import com.sk.socialmedia.models.ModelPost;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ThereProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private RecyclerView postsRecyclerView;
    private ImageView profileImage, coverImage;
    private TextView nameTv, emailTv, statusTv;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_there_profile);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        profileImage = findViewById(R.id.profile_image);
        nameTv = findViewById(R.id.name_tv);
        emailTv =findViewById(R.id.email_tv);
        statusTv = findViewById(R.id.status_tv);
        coverImage = findViewById(R.id.coverIv);

        postsRecyclerView = findViewById(R.id.recyclerView_posts);
        mAuth=FirebaseAuth.getInstance();
        Intent intent=getIntent();
        uid=intent.getStringExtra("uid");

        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = "" + ds.child("name").getValue();
                        String email = "" + ds.child("email").getValue();
                        String status = "" + ds.child("status").getValue();
                        String image = "" + ds.child("image").getValue();
                        String cover = "" + ds.child("cover").getValue();

                        nameTv.setText(name);
                        emailTv.setText(email);
                        statusTv.setText(status);

                        try {
                            Picasso.get().load(image).into(profileImage);
                        } catch (Exception e) {
                            Picasso.get().load(R.drawable.ic_add_image).into(profileImage);
                        }

                        try {
                            Picasso.get().load(cover).into(coverImage);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postList=new ArrayList<>();
        checkUserStatus();
        loadHisPosts();

    }


    private void loadHisPosts() {
        LinearLayoutManager layoutManager=new LinearLayoutManager(ThereProfileActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setNestedScrollingEnabled(false);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        Query query=ref.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost myPosts=ds.getValue(ModelPost.class);
                    postList.add(myPosts);

                    adapterPosts=new AdapterPosts(ThereProfileActivity.this,postList);
                    postsRecyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ThereProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void searchHisPosts(final String searchQuery) {
        LinearLayoutManager layoutManager=new LinearLayoutManager(ThereProfileActivity.this);
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postsRecyclerView.setLayoutManager(layoutManager);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        Query query=ref.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost myPosts=ds.getValue(ModelPost.class);

                    if (myPosts.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())||
                            myPosts.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(myPosts);
                    }

                    adapterPosts=new AdapterPosts(ThereProfileActivity.this,postList);
                    postsRecyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(ThereProfileActivity.this, ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            //uid=user.getUid();

        }
        else
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);

        MenuItem item=menu.findItem(R.id.action_search);
        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query))
                {
                    searchHisPosts(query);
                }
                else
                {
                    loadHisPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText))
                {
                    searchHisPosts(newText);
                }
                else
                {
                    loadHisPosts();
                }
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_logout)
        {
            mAuth.signOut();
            checkUserStatus();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
