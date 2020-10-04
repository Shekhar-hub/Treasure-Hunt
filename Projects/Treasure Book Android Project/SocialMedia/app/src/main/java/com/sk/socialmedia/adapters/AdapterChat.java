package com.sk.socialmedia.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.R;
import com.sk.socialmedia.models.ModelChat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterChat  extends  RecyclerView.Adapter<AdapterChat.MyHolder>{
    private static final int MSG_TYPE_LEFT=0;
    private static final int MSG_TYPE_RIGHT=1;
    Context context;
    List<ModelChat> chatList;
    String imageUrl;
    FirebaseUser fUser;

    public AdapterChat(Context context, List<ModelChat> chatList, String imageUrl) {
        this.context = context;
        this.chatList = chatList;
        this.imageUrl = imageUrl;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i==MSG_TYPE_RIGHT)
        {
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_right,parent,false);
            return  new MyHolder(view);
        }
        else
        {
            View view= LayoutInflater.from(context).inflate(R.layout.row_chat_left,parent,false);
            return  new MyHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, final int position) {
        String message=chatList.get(position).getMessage();
        String timeStamp=chatList.get(position).getTimestamp();
        String type=chatList.get(position).getType();

        Calendar cal=Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Long.parseLong(timeStamp));
        String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();

        if(type.equals("text"))
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

        //holder.messageTv.setText(message);
        holder.timeTv.setText(dateTime);

        try
        {
            Picasso.get().load(imageUrl).into(holder.mProfileIv);
        }
        catch (Exception e)
        {

        }
        holder.messageLayout.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle("Delete");
                builder.setMessage("Are you sure to delete the message?");

                builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteMessage(position);
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.create().show();
                return true;
            }
        });
        Log.i("position",String.valueOf(position));
        if (position==chatList.size()-1)
        {
            if (chatList.get(position).isSeen())
            {
                holder.isSeenTv.setText("Seen");
            }
            else
            {
                holder.isSeenTv.setText("Delivered");
            }
        }
        else
        {
            holder.isSeenTv.setVisibility(View.GONE);
        }
    }

    private void deleteMessage(int position) {
        final String myUID=FirebaseAuth.getInstance().getCurrentUser().getUid();
        String msgTimeStamp=chatList.get(position).getTimestamp();
        DatabaseReference dbRef= FirebaseDatabase.getInstance().getReference("Chats");
        Query query=dbRef.orderByChild("timestamp").equalTo(msgTimeStamp);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if (ds.child("sender").getValue().equals(myUID))
                    {
                        //ds.getRef().removeValue();

                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("message","This message was deleted...");
                        ds.getRef().updateChildren(hashMap);
                    }
                    else
                    {

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        fUser= FirebaseAuth.getInstance().getCurrentUser();
        if (chatList.get(position).getSender().equals(fUser.getUid()))
        {
            return MSG_TYPE_RIGHT;
        }
        else
        {
            return MSG_TYPE_LEFT;
        }
    }

    class MyHolder extends RecyclerView.ViewHolder
    {
        ImageView mProfileIv,messageIv;
        TextView messageTv,timeTv,isSeenTv;
        LinearLayout messageLayout;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            messageTv=itemView.findViewById(R.id.messageTv);
            timeTv=itemView.findViewById(R.id.timeTv);
            mProfileIv=itemView.findViewById(R.id.profile_msgCIV);
            messageIv=itemView.findViewById(R.id.messageIv);
            isSeenTv=itemView.findViewById(R.id.isSeenTv);
            messageLayout=itemView.findViewById(R.id.messageLayout);
        }
    }
}
