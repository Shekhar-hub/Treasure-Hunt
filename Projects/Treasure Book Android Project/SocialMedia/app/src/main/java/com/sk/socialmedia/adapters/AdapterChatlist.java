package com.sk.socialmedia.adapters;

import android.content.Context;
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
import com.sk.socialmedia.R;
import com.sk.socialmedia.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class AdapterChatlist extends RecyclerView.Adapter<AdapterChatlist.MyHolder> {

    Context context;
    List<ModelUser> userList;
    private HashMap<String,String> lastMessageMap;
    FirebaseAuth mAuth;

    public AdapterChatlist(Context context, List<ModelUser> userList) {
        this.context = context;
        this.userList = userList;
        mAuth=FirebaseAuth.getInstance();
        lastMessageMap = new HashMap<>();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_chatlist,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String chatUserId=userList.get(position).getUid();
        String userImage=userList.get(position).getImage();
        String userName=userList.get(position).getName();
        String lastMessage=lastMessageMap.get(chatUserId);

        holder.nameTv.setText(userName);
        if (lastMessage==null || lastMessage.equals("default"))
        {
            holder.lastMessageTv.setVisibility(View.GONE);
        }
        else
        {
            holder.lastMessageTv.setVisibility(View.VISIBLE);
            holder.lastMessageTv.setText(lastMessage);
        }
        try
        {
            Picasso.get().load(userImage).placeholder(R.drawable.ic_default_img).into(holder.profileIv);
        }
        catch (Exception e)
        {
            Picasso.get().load(R.drawable.ic_default_img).placeholder(R.drawable.ic_default_img).into(holder.profileIv);

        }

        if (userList.get(position).getOnlineStatus().equals("Online"))
        {
            holder.onlineStatusIv.setImageResource(R.drawable.circle_online);
        }
        else
        {
            holder.onlineStatusIv.setImageResource(R.drawable.circle_offline);
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Intent intent=new Intent(context, ChatActivity.class);
                intent.putExtra("chatUserId",chatUserId);
                context.startActivity(intent);*/
                imBlockedOrNot(chatUserId);
            }
        });
    }
    public void setLastMessage(String userId,String lastMessage)
    {
        lastMessageMap.put(userId,lastMessage);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
    private void imBlockedOrNot(final String chatUserId)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(chatUserId).child("BlockedUsers").orderByChild("uid").equalTo(mAuth.getUid())
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
    class MyHolder extends RecyclerView.ViewHolder
    {

        ImageView profileIv,onlineStatusIv;
        TextView nameTv,lastMessageTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            profileIv=itemView.findViewById(R.id.profileivIv);
            nameTv=itemView.findViewById(R.id.nameTv_chatlist);
            onlineStatusIv=itemView.findViewById(R.id.onlineStatusTv);
            lastMessageTv=itemView.findViewById(R.id.lastMessageTv);
        }
    }
}
