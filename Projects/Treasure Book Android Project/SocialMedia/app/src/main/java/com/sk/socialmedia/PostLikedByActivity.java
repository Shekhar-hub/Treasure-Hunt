package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.sk.socialmedia.adapters.AdapterUsers;
import com.sk.socialmedia.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PostLikedByActivity extends AppCompatActivity {

    String postId;
    private RecyclerView likesListRv;

    private List<ModelUser> userList;
    private AdapterUsers adapterUsers;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_liked_by);

        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Post Liked By");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        mAuth=FirebaseAuth.getInstance();
        actionBar.setSubtitle(mAuth.getCurrentUser().getEmail());

        likesListRv=findViewById(R.id.likesListRv);

        Intent intent=getIntent();
        postId=intent.getStringExtra("postId");

        userList=new ArrayList<>();

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Likes");
        ref.child(postId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String hisUid=""+ds.getRef().getKey();

                    getUsers(hisUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void getUsers(String hisUid) {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(hisUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {

                            ModelUser modelUser=ds.getValue(ModelUser.class);
                            userList.add(modelUser);
                        }
                        adapterUsers=new AdapterUsers(PostLikedByActivity.this,userList);
                        likesListRv.setAdapter(adapterUsers);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
