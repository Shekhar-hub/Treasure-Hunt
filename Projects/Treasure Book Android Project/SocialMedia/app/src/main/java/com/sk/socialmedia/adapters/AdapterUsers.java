package com.sk.socialmedia.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.ChatActivity;
import com.sk.socialmedia.ThereProfileActivity;
import com.sk.socialmedia.models.ModelUser;
import com.sk.socialmedia.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterUsers extends RecyclerView.Adapter<AdapterUsers.MyHolder>{
    Context context;
    List<ModelUser> userList;
    //AdapterUsers adapterUsers;
    FirebaseAuth mAuth;
    String myUid;

    public AdapterUsers(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
        mAuth=FirebaseAuth.getInstance();
        myUid=mAuth.getUid();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view= LayoutInflater.from(context).inflate(R.layout.row_users,parent,false);
       return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        final String chatUserId=userList.get(position).getUid();
        String uName=userList.get(position).getName();
        String uImage=userList.get(position).getImage();
        String[] uEmail=(userList.get(position).getEmail()).split("@");

        holder.mNameTv.setText(uName);
        holder.mEmailTv.setText(uEmail[0]);

        try {
            Picasso.get().load(uImage)
                    .placeholder(R.drawable.ic_default_img)
                    .into(holder.mProfileIv);
        }
        catch (Exception e)
        {

        }

        holder.blockIv.setImageResource(R.drawable.ic_unblocked_green);
        checkIsBlocked(chatUserId,holder,position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setItems(new String[]{"Profile", "Chat"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which==0)
                        {
                            Intent intent=new Intent(context, ThereProfileActivity.class);
                            intent.putExtra("uid",chatUserId);
                            context.startActivity(intent);
                        }
                        if (which==1)
                        {
                            imBlockedOrNot(chatUserId);

                        }
                    }
                });
                builder.create().show();
            }
        });
        holder.blockIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userList.get(position).isBlocked())
                {
                    unBlockUser(chatUserId);
                }
                else
                {
                    blockUser(chatUserId);
                }

            }
        });
    }

    private void imBlockedOrNot(final String chatUserId)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(chatUserId).child("BlockedUsers").orderByChild("uid").equalTo(myUid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if (ds.exists())
                            {
                                Toast.makeText(context, "You're blocked by that user, can't send message", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }

                        Intent chatIntent=new Intent(context, ChatActivity.class);
                        chatIntent.putExtra("chatUserId",chatUserId);
                        context.startActivity(chatIntent);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }
    private void checkIsBlocked(String chatUserId, final MyHolder holder, final int position) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(chatUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if (ds.exists())
                            {
                                holder.blockIv.setImageResource(R.drawable.ic_blocked_red);
                                userList.get(position).setBlocked(true);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void unBlockUser(String chatUserId) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").orderByChild("uid").equalTo(chatUserId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if (ds.exists())
                            {
                                ds.getRef().removeValue()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(context, "Unblocked successfully...", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void blockUser(String chatUserId) {

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",chatUserId);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").child(chatUserId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(context, "Blocked successfully...", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView mProfileIv,blockIv;
        TextView mNameTv,mEmailTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            mNameTv=itemView.findViewById(R.id.nameTv);
            mEmailTv=itemView.findViewById(R.id.userNameTv);
            mProfileIv=itemView.findViewById(R.id.profileCIV);
            blockIv=itemView.findViewById(R.id.blockIv);

        }
    }
}
