package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.socialmedia.adapters.AdapterParticipantsAdd;
import com.sk.socialmedia.models.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class GroupInfoActivity extends AppCompatActivity {

    private String groupId;
    private ActionBar actionBar;

    private ArrayList<ModelUser> userList;
    private AdapterParticipantsAdd adapterParticipantsAdd;

    private FirebaseAuth mAuth;

    private ImageView groupIconIv;
    private TextView descriptionTv,createdByTv,editGroupTv,addParticipantTv,leaveGroupTv,participantsTv;
    private RecyclerView participantsRv;
    private String myGroupRole="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_info);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Group Info");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        groupId=getIntent().getStringExtra("groupId");

        mAuth=FirebaseAuth.getInstance();
        loadGroupInfo();
        loadMyGroupRole();

        addParticipantTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupInfoActivity.this,GroupParticipantsAddActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });

        editGroupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(GroupInfoActivity.this,GroupEditActivity.class);
                intent.putExtra("groupId",groupId);
                startActivity(intent);
            }
        });

        leaveGroupTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String dialogTitle="";
                String dialogDescription="";
                String positiveButtonTitle="";

                if (myGroupRole.equals("creator"))
                {
                    dialogTitle="Delete Group";
                    dialogDescription="Are you sure you want to Delete group permanently?";
                    positiveButtonTitle="DELETE";
                }
                else
                {
                    dialogTitle="Leave Group";
                    dialogDescription="Are you sure you want to Leave group permanently?";
                    positiveButtonTitle="LEAVE";
                }
                AlertDialog.Builder builder=new AlertDialog.Builder(GroupInfoActivity.this);
                builder.setTitle(dialogTitle)
                        .setMessage(dialogDescription)
                        .setPositiveButton(positiveButtonTitle, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (myGroupRole.equals("creator"))
                                {
                                    deleteGroup();
                                }
                                else
                                {
                                    leaveGroup();
                                }
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                dialogInterface.dismiss();
                            }
                        })
                        .show();
            }
        });

    }

    private void leaveGroup() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .child(mAuth.getUid())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GroupInfoActivity.this, "Group left successfully...", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(GroupInfoActivity.this,DashboardActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupInfoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteGroup() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId)
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(GroupInfoActivity.this, "Group deleted successfully...", Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(GroupInfoActivity.this,DashboardActivity.class));
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(GroupInfoActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadMyGroupRole() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");

        ref.child(groupId).child("Participants")
                .orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            myGroupRole=""+ds.child("role").getValue();

                            //Log.i("role",myGroupRole);
                            actionBar.setSubtitle(mAuth.getCurrentUser().getEmail()+" ("+myGroupRole+")");

                            if (myGroupRole.equals("participant"))
                            {
                                editGroupTv.setVisibility(View.GONE);
                                addParticipantTv.setVisibility(View.GONE);
                                leaveGroupTv.setText("Leave Group");
                            }
                            else if (myGroupRole.equals("admin"))
                            {
                                editGroupTv.setVisibility(View.GONE);
                                addParticipantTv.setVisibility(View.VISIBLE);
                                leaveGroupTv.setText("Leave Group");
                            }
                            else if (myGroupRole.equals("creator"))
                            {
                                editGroupTv.setVisibility(View.VISIBLE);
                                addParticipantTv.setVisibility(View.VISIBLE);
                                leaveGroupTv.setText("Delete Group");
                            }
                        }
                        loadParticipants();
                    }


                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });


    }

    private void loadParticipants() {

        userList=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        userList.clear();
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            String uid=""+ds.child("uid").getValue();
                            DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Users");
                            ref1.orderByChild("uid").equalTo(uid)
                                    .addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            for (DataSnapshot ds:dataSnapshot.getChildren())
                                            {
                                                ModelUser modelUser=ds.getValue(ModelUser.class);

                                                userList.add(modelUser);
                                            }
                                            adapterParticipantsAdd=new AdapterParticipantsAdd(GroupInfoActivity.this,userList,groupId,myGroupRole);
                                            participantsRv.setAdapter(adapterParticipantsAdd);
                                            participantsTv.setText("Participants ("+userList.size()+")");

                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {

                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void loadGroupInfo() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
        ref.orderByChild("groupId").equalTo(groupId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            String groupId=""+ds.child("groupId").getValue();
                            String groupTitle=""+ds.child("groupTitle").getValue();
                            String groupDescription=""+ds.child("groupDescription").getValue();
                            String groupIcon=""+ds.child("groupIcon").getValue();
                            String timestamp=""+ds.child("timestamp").getValue();
                            String createdBy=""+ds.child("createdBy").getValue();


                            Calendar cal=Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(timestamp));
                            String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

                            loadCreatorInfo(dateTime,createdBy);

                            actionBar.setTitle(groupTitle);
                            descriptionTv.setText(groupDescription);

                            try {

                                Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_primary).into(groupIconIv);
                            }
                            catch (Exception e)
                            {
                                groupIconIv.setImageResource(R.drawable.ic_group_primary);
                            }


                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void loadCreatorInfo(final String dateTime, String createdBy) {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(createdBy)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            String name=""+ds.child("name").getValue();
                            createdByTv.setText("Created by "+name+" on "+dateTime);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void InitializeFields() {

        groupIconIv=findViewById(R.id.groupInfoIconIv);
        descriptionTv=findViewById(R.id.groupDescriptionTv);
        createdByTv=findViewById(R.id.createdByTv);
        editGroupTv=findViewById(R.id.editGroupTv);
        addParticipantTv=findViewById(R.id.addParticipantTv);
        leaveGroupTv=findViewById(R.id.leaveGroupTv);
        participantsTv=findViewById(R.id.participantsTv);
        participantsRv=findViewById(R.id.participantsRv);


    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
