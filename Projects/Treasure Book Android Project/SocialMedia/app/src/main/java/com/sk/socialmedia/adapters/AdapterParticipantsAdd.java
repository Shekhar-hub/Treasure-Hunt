package com.sk.socialmedia.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.R;
import com.sk.socialmedia.models.ModelUser;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class AdapterParticipantsAdd extends RecyclerView.Adapter<AdapterParticipantsAdd.HolderParticipantsAdd>{

    private Context context;
    private ArrayList<ModelUser> userList;
    private String groupId,myGroupRole;

    public AdapterParticipantsAdd(Context context, ArrayList<ModelUser> userList, String groupId, String myGroupRole) {
        this.context = context;
        this.userList = userList;
        this.groupId = groupId;
        this.myGroupRole = myGroupRole;
    }

    @NonNull
    @Override
    public HolderParticipantsAdd onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_participants_add,parent,false);
        return new HolderParticipantsAdd(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderParticipantsAdd holder, int position) {

        final ModelUser modelUser=userList.get(position);
        String name=modelUser.getName();
        String email=modelUser.getEmail();
        String image=modelUser.getImage();
        final String uid=modelUser.getUid();

        holder.nameTv.setText(name);
        holder.emailTv.setText(email);

        try
        {
            Picasso.get().load(image).placeholder(R.drawable.ic_default_img).into(holder.profileIv);
        }
        catch (Exception e)
        {
            holder.profileIv.setImageResource(R.drawable.ic_default_img);

        }

        checkIfAlreadyExists(modelUser,holder);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
                ref.child(groupId).child("Participants").child(uid)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists())
                                {
                                    String hisPreviousRole=""+dataSnapshot.child("role").getValue();

                                    String[] options;

                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Choose Option");
                                    if (myGroupRole.equals("creator"))
                                    {
                                        if (hisPreviousRole.equals("admin"))
                                        {
                                            options=new String[]{"Remove Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    if (which==0)
                                                    {
                                                        removeAdmin(modelUser);
                                                    }
                                                    else
                                                    {
                                                        removeParticipant(modelUser);
                                                    }
                                                }
                                            }).show();
                                        }
                                        else if (hisPreviousRole.equals("participant")) {
                                            options=new String[]{"Make Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    if (which==0)
                                                    {
                                                        makeAdmin(modelUser);
                                                    }
                                                    else
                                                    {
                                                        removeParticipant(modelUser);
                                                    }
                                                }
                                            }).show();
                                        }

                                    }
                                    else if (myGroupRole.equals("admin"))
                                    {
                                        if (hisPreviousRole.equals("creator"))
                                        {
                                            Toast.makeText(context, "Creator of Group...", Toast.LENGTH_SHORT).show();
                                        }
                                        else if (hisPreviousRole.equals("admin"))
                                        {
                                            options=new String[]{"Remove Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    if (which==0)
                                                    {
                                                        removeAdmin(modelUser);
                                                    }
                                                    else
                                                    {
                                                        removeParticipant(modelUser);
                                                    }
                                                }
                                            }).show();
                                        }
                                        else if (hisPreviousRole.equals("participant"))
                                        {
                                            options=new String[]{"Make Admin","Remove User"};
                                            builder.setItems(options, new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int which) {

                                                    if (which==0)
                                                    {
                                                        makeAdmin(modelUser);
                                                    }
                                                    else
                                                    {
                                                        removeParticipant(modelUser);
                                                    }
                                                }
                                            }).show();
                                        }
                                    }
                                }
                                else
                                {
                                    AlertDialog.Builder builder=new AlertDialog.Builder(context);
                                    builder.setTitle("Add Participant")
                                            .setMessage("Add this user in this group?")
                                            .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {

                                                    addParticipant(modelUser);
                                                }
                                            })
                                            .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    dialogInterface.dismiss();
                                                }
                                            }).show();

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
            }
        });
    }

    private void addParticipant(ModelUser modelUser) {

        String timestamp=""+System.currentTimeMillis();
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",modelUser.getUid());
        hashMap.put("role","participant");
        hashMap.put("timestamp",""+timestamp);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .child(modelUser.getUid())
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Added successfully...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void makeAdmin(ModelUser modelUser) {


        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("role","admin");

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .child(modelUser.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "The user is now Admin...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void removeParticipant(ModelUser modelUser) {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .child(modelUser.getUid())
                .removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "User Removed...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void removeAdmin(ModelUser modelUser) {


        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("role","participant");

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .child(modelUser.getUid())
                .updateChildren(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "The user is no longer Admin now...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void checkIfAlreadyExists(ModelUser modelUser, final HolderParticipantsAdd holder) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants").child(modelUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            String hisRole=""+dataSnapshot.child("role").getValue();
                            holder.statusTv.setText(hisRole);
                        }
                        else
                        {
                            holder.statusTv.setText("");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class HolderParticipantsAdd extends RecyclerView.ViewHolder
    {
        ImageView profileIv;
        TextView nameTv,emailTv,statusTv;

        public HolderParticipantsAdd(@NonNull View itemView) {
            super(itemView);
            nameTv=itemView.findViewById(R.id.nameTv);
            emailTv=itemView.findViewById(R.id.userNameTv);
            profileIv=itemView.findViewById(R.id.profileCIV);
            statusTv=itemView.findViewById(R.id.statusTv);

        }
    }
}
