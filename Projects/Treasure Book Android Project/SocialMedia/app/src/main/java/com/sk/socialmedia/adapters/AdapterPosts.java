package com.sk.socialmedia.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.AddPostActivity;
import com.sk.socialmedia.PostDetailActivity;
import com.sk.socialmedia.PostLikedByActivity;
import com.sk.socialmedia.R;
import com.sk.socialmedia.ThereProfileActivity;
import com.sk.socialmedia.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class AdapterPosts extends RecyclerView.Adapter<AdapterPosts.MyHolder>{
    Context context;
    List<ModelPost> postList;

    String myUid;
    private DatabaseReference likesRef,postsRef;
    private boolean mProcessLike=false;


    public AdapterPosts(Context context, List<ModelPost> postList) {
        this.context = context;
        this.postList = postList;
        myUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
        likesRef=FirebaseDatabase.getInstance().getReference().child("Likes");
        postsRef=FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.row_posts,parent,false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {

        final String uid=postList.get(position).getUid();
        String uEmail=postList.get(position).getuEmail();
        String uName=postList.get(position).getuName();
        String uDp=postList.get(position).getuDp();
        final String pId=postList.get(position).getpId();
        final String pTitle=postList.get(position).getpTitle();
        final String pDescription=postList.get(position).getpDescr();
        final String pImage=postList.get(position).getpImage();
        String pTimeStamp=postList.get(position).getpTime();
        String pLikes=""+postList.get(position).getpLikes();
        String pComments=""+postList.get(position).getpComments();



        Calendar calendar=Calendar.getInstance(Locale.getDefault());
        calendar.setTimeInMillis(Long.parseLong(pTimeStamp));
        String pTime= DateFormat.format("dd/MM/yyyy hh:mm aa",calendar).toString();

        holder.uNameTv.setText(uName);
        holder.pTimeTv.setText(pTime);
        holder.pTitleTv.setText(pTitle);
        holder.pDescriptionTv.setText(pDescription);
        holder.pLikesTv.setText(pLikes+" Likes");
        holder.pCommentsTv.setText(pComments+" Comments");

        setLikes(holder,pId);
        try
        {
            Picasso.get()
                    .load(uDp)
                    .placeholder(R.drawable.ic_default_img)
                    .into(holder.uPictureIv);
        }
        catch (Exception e)
        {
        }

        if (pImage.equals("noImage"))
        {
            holder.pImageIv2.setVisibility(View.GONE);
        }
        else {

            holder.pImageIv2.setVisibility(View.VISIBLE);
            try {
                Picasso.get()
                        .load(pImage)
                        .into(holder.pImageIv2);
            } catch (Exception e) {
            }
        }
        holder.moreBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                showMoreOptions(holder.moreBtn,uid,myUid,pId,pImage);
            }
        });
        holder.likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pLikes=Integer.parseInt(postList.get(position).getpLikes());
                mProcessLike=true;

                final String postIde=postList.get(position).getpId();
                likesRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (mProcessLike)
                        {
                            if (dataSnapshot.child(postIde).hasChild(myUid))
                            {
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes-1));
                                likesRef.child(postIde).child(myUid).removeValue();
                                mProcessLike=false;
                            }
                            else
                            {
                                postsRef.child(postIde).child("pLikes").setValue(""+(pLikes+1));
                                likesRef.child(postIde).child(myUid).setValue("Liked");
                                mProcessLike=false;
                                addToHisNotifications(""+uid,""+pId,"Liked your post");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
        holder.commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, PostDetailActivity.class);
                intent.putExtra("postId",pId);
                context.startActivity(intent);

            }
        });
        holder.shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                BitmapDrawable bitmapDrawable=(BitmapDrawable) holder.pImageIv2.getDrawable();
                if (bitmapDrawable==null)
                {
                    shareTextOnly(pTitle,pDescription);
                }
                else
                {
                    Bitmap bitmap=bitmapDrawable.getBitmap();

                    shareImageAndText(pTitle,pDescription,bitmap);

                }
            }
        });
        holder.profileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent=new Intent(context, ThereProfileActivity.class);
                intent.putExtra("uid",uid);
                context.startActivity(intent);
            }
        });

        holder.pLikesTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(context, PostLikedByActivity.class);
                intent.putExtra("postId",pId);
                context.startActivity(intent);

            }
        });
    }

    private void addToHisNotifications(String hisUid,String pId,String notification) {
        String timestamp=""+System.currentTimeMillis();

        HashMap<Object,String> hashMap=new HashMap<>();
        hashMap.put("pId",pId);
        hashMap.put("timestamp",timestamp);
        hashMap.put("pUid",hisUid);
        hashMap.put("notification",notification);
        hashMap.put("sUid",myUid);

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Users");
        ref.child(hisUid).child("Notifications").child(timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });
    }

    private void shareImageAndText(String pTitle, String pDescription, Bitmap bitmap) {

        String shareBody=pTitle+"\n"+pDescription;

        Uri uri=saveImageToShare(bitmap);

        Intent sIntent=new Intent(Intent.ACTION_SEND);
        sIntent.putExtra(Intent.EXTRA_STREAM,uri);
        sIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        sIntent.setType("image/png");
        context.startActivity(Intent.createChooser(sIntent,"Share via"));

    }

    private void shareTextOnly(String pTitle, String pDescription) {
        String shareBody=pTitle+"\n"+pDescription;

        Intent sIntent=new Intent(Intent.ACTION_SEND);
        sIntent.setType("text/plain");
        sIntent.putExtra(Intent.EXTRA_SUBJECT,"Subject Here");
        sIntent.putExtra(Intent.EXTRA_TEXT,shareBody);
        context.startActivity(Intent.createChooser(sIntent,"Share via"));
    }

    private Uri saveImageToShare(Bitmap bitmap) {
        File imageFolder=new File(context.getCacheDir(),"images");
        Uri uri=null;
        try
        {
            imageFolder.mkdirs();
            File file=new File(imageFolder,"shared_images.png");

            FileOutputStream stream=new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG,90,stream);
            stream.flush();
            stream.close();
            uri= FileProvider.getUriForFile(context,"com.sk.socialmedia.fileprovider",file);
        }
        catch (Exception e)
        {
            Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

        }
        return uri;

    }

    private void setLikes(final MyHolder holder, final String postKey) {

        likesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(postKey).hasChild(myUid))
                {
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_liked,0,0,0);
                    holder.likeBtn.setText("Liked");
                }
                else
                {
                    holder.likeBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_like_black,0,0,0);
                    holder.likeBtn.setText("Like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void showMoreOptions(ImageButton moreBtn, String uid, String myUid, final String pId, final String pImage) {

        PopupMenu popupMenu=new PopupMenu(context,moreBtn, Gravity.END);

        if (uid.equals(myUid)) {
            popupMenu.getMenu().add(Menu.NONE, 0, 0, "Delete");
            popupMenu.getMenu().add(Menu.NONE, 1, 0, "Edit");
        }
        popupMenu.getMenu().add(Menu.NONE, 2, 0, "View Detail");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id=item.getItemId();
                if (id==0)
                {
                    beginDelete(pId,pImage);
                }
                else  if(id==1)
                {
                   Intent intent=new Intent(context, AddPostActivity.class);
                   intent.putExtra("key","editPost");
                   intent.putExtra("editPostId",pId);
                   context.startActivity(intent);
                }
                else  if(id==2)
                {
                    Intent intent=new Intent(context, PostDetailActivity.class);
                    intent.putExtra("postId",pId);
                    context.startActivity(intent);

                }
                return false;
            }
        });
        popupMenu.show();
    }

    private void beginDelete(String pId, String pImage) {
        if (pImage.equals("noImage"))
        {
            deleteWithoutImage(pId);
        }
        else
        {
            deleteWithImage(pId,pImage);
        }

    }

    private void deleteWithoutImage(String pId) {
        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Deleting");

        Query fquery= FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ds.getRef().removeValue();
                }
                pd.dismiss();
                //adapterPosts=new AdapterPosts(context,postList);
                notifyDataSetChanged();
                Toast.makeText(context, "Post Deleted Successfully...", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.dismiss();

            }
        });

    }

    private void deleteWithImage(final String pId, String pImage) {

        final ProgressDialog pd=new ProgressDialog(context);
        pd.setMessage("Deleting");

        StorageReference picRef= FirebaseStorage.getInstance().getReferenceFromUrl(pImage);
        picRef.delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Query fquery= FirebaseDatabase.getInstance().getReference("Posts").orderByChild("pId").equalTo(pId);
                        fquery.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren())
                                {
                                    ds.getRef().removeValue();
                                }
                                pd.dismiss();
                                notifyDataSetChanged();
                                Toast.makeText(context, "Post Deleted Successfully...", Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                pd.dismiss();

                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(context, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return postList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder
    {

        ImageView uPictureIv,pImageIv2;
        TextView uNameTv,pTimeTv,pTitleTv,pDescriptionTv,pLikesTv,pCommentsTv;
        ImageButton moreBtn;
        Button likeBtn,commentBtn,shareBtn;
        LinearLayout profileLayout;
        public MyHolder(@NonNull View itemView) {
            super(itemView);

            uPictureIv=itemView.findViewById(R.id.uPictureIv);
            pImageIv2=itemView.findViewById(R.id.pImageIv2);
            uNameTv=itemView.findViewById(R.id.uNameTv);
            pTimeTv=itemView.findViewById(R.id.uTimeTv);
            pTitleTv=itemView.findViewById(R.id.pTitleTv);
            pDescriptionTv=itemView.findViewById(R.id.pDescriptionTv);
            pLikesTv=itemView.findViewById(R.id.pLikesTv);
            pCommentsTv=itemView.findViewById(R.id.pCommentsTv);
            moreBtn=itemView.findViewById(R.id.moreBtn);
            likeBtn=itemView.findViewById(R.id.likeBtn);
            commentBtn=itemView.findViewById(R.id.commentBtn);
            shareBtn=itemView.findViewById(R.id.shareBtn);
            profileLayout=itemView.findViewById(R.id.profileLayout);

        }
    }
}
