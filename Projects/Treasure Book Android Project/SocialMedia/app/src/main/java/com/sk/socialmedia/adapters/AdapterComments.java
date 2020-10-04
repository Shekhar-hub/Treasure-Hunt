package com.sk.socialmedia.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.R;
import com.sk.socialmedia.models.ModelComments;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class AdapterComments extends RecyclerView.Adapter<AdapterComments.MyHolder> {
    Context context;
    List<ModelComments> commentsList;
    String myUid,postId;

    public AdapterComments(Context context, List<ModelComments> commentsList, String myUid, String postId) {
        this.context = context;
        this.commentsList = commentsList;
        this.myUid = myUid;
        this.postId = postId;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_comments,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

        final String uid=commentsList.get(position).getUid();
        String name=commentsList.get(position).getuName();
        String email=commentsList.get(position).getuEmail();
        String image=commentsList.get(position).getuDp();
        final String cId=commentsList.get(position).getcId();
        String comment=commentsList.get(position).getComment();
        String timestamp=commentsList.get(position).getTimestamp();

        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(timestamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.nameTv.setText(name);
        holder.commentTv.setText(comment);
        holder.timeTv.setText(pTime);

        try
        {
            Picasso.get().load(image).placeholder(R.drawable.ic_default_img).into(holder.cProfileIv);
        }
        catch (Exception e)
        {
            Picasso.get().load(R.drawable.ic_default_img).placeholder(R.drawable.ic_default_img).into(holder.cProfileIv);

        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                    if (myUid.equals(uid))
                    {
                        AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
                        builder.setTitle("Delete");
                        builder.setMessage("Are you sure to delete this comment?");
                        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                deleteComment(cId);
                            }
                        });
                        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                        builder.create().show();
                    }
                    else
                    {

                    }
                return false;
            }
        });

    }

    private void deleteComment(String cId) {
        final DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        ref.child("Comments").child(cId).removeValue();

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String comments=""+dataSnapshot.child("pComments").getValue();
                int newCommentVal=Integer.parseInt(comments)-1;
                ref.child("pComments").setValue(""+newCommentVal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return commentsList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {

        ImageView cProfileIv;
        TextView nameTv,commentTv,timeTv;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cProfileIv=itemView.findViewById(R.id.cProfileIv_comment);
            nameTv=itemView.findViewById(R.id.nameTv_comment);
            commentTv=itemView.findViewById(R.id.commentTv);
            timeTv=itemView.findViewById(R.id.timeTv_comments);
        }
    }
}
