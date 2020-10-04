package com.sk.socialmedia.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.R;
import com.sk.socialmedia.models.ModelGroupChat;
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

public class AdapterGroupChat extends RecyclerView.Adapter<AdapterGroupChat.HolderGroupChat>{

    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;

    private Context context;
    private ArrayList<ModelGroupChat> modelGroupChatList;
    private FirebaseAuth mAuth;

    public AdapterGroupChat(Context context, ArrayList<ModelGroupChat> modelGroupChatList) {
        this.context = context;
        this.modelGroupChatList = modelGroupChatList;
        mAuth=FirebaseAuth.getInstance();
    }

    @NonNull
    @Override
    public HolderGroupChat onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_TYPE_RIGHT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.row_groupchat_right,parent,false);
            return  new HolderGroupChat(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.row_groupchat_left,parent,false);
            return  new HolderGroupChat(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull HolderGroupChat holder, int position) {

        ModelGroupChat model=modelGroupChatList.get(position);
        String message=model.getMessage();
        String timestamp=model.getTimestamp();
        String senderUid=model.getSender();
        String messageType=model.getType();

        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timestamp));
        String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        if(messageType.equals("text"))
        {
            holder.messageTv.setVisibility(View.VISIBLE);
            holder.messageIv.setVisibility(View.GONE);

            holder.messageTv.setText(message);

        }
        else
        {
            holder.messageTv.setVisibility(View.GONE);
            holder.messageIv.setVisibility(View.VISIBLE);

            Picasso.get().load(message).placeholder(R.drawable.ic_image_black).into(holder.messageIv);


        }
       // holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);

        setUserName(model,holder);
    }

    private void setUserName(ModelGroupChat model, final HolderGroupChat holder) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.orderByChild("uid").equalTo(model.getSender())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            String name=""+ds.child("name").getValue();
                            holder.nameTv.setText(name);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    @Override
    public int getItemCount() {
        return modelGroupChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (modelGroupChatList.get(position).getSender().equals(mAuth.getUid()))
        {
            return  MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }

    class HolderGroupChat extends RecyclerView.ViewHolder
    {
        private TextView nameTv,messageTv,timeTv;
        private ImageView messageIv;

        public HolderGroupChat(@NonNull View itemView) {
            super(itemView);

            nameTv=itemView.findViewById(R.id.nameTv_groupChat);
            messageTv=itemView.findViewById(R.id.messageTv_groupChat);
            timeTv=itemView.findViewById(R.id.timeTv_groupChat);
            messageIv=itemView.findViewById(R.id.messageIv);

        }
    }
}