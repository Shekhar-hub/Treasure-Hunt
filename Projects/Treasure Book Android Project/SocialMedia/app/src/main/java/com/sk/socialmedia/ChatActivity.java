package com.sk.socialmedia;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.sk.socialmedia.adapters.AdapterChat;
import com.sk.socialmedia.models.ModelChat;
import com.sk.socialmedia.models.ModelUser;
import com.sk.socialmedia.notification.Data;
import com.sk.socialmedia.notification.Sender;
import com.sk.socialmedia.notification.Token;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {


    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri imageUri=null;

    private Toolbar toolbar;
    RecyclerView recyclerView;
    private ImageView profile_chatCiv,blockIv;
    private TextView chatNameTv,userStatusTv;
    private EditText messageEt;
    private ImageButton sendBtn,attachBtn;
     FirebaseAuth mAuth;
     String chatUserId,myUid,chatUserImage;
     FirebaseDatabase firebaseDatabase;
     DatabaseReference usersRef,userRefForSeen;
     ValueEventListener seenListener;

     List<ModelChat> chatList;
     AdapterChat adapterChat;

     private RequestQueue requestQueue;
     private boolean notify=false,isBlocked=false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        initializeFields();
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};



        Query query=usersRef.orderByChild("uid").equalTo(chatUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    String name=""+ds.child("name").getValue();
                    chatUserImage=""+ds.child("image").getValue();
                    String typingStatus=""+ds.child("typingTo").getValue();

                    if (typingStatus.equals(myUid))
                    {
                        userStatusTv.setText("typing...");
                    }
                    else
                    {
                        String onlineStatus=""+ds.child("onlineStatus").getValue();
                        if (onlineStatus.equals("Online"))
                        {
                            userStatusTv.setText(onlineStatus);
                        }
                        else
                        {
                            Calendar cal=Calendar.getInstance(Locale.ENGLISH);
                            cal.setTimeInMillis(Long.parseLong(onlineStatus));
                            String dateTime= DateFormat.format("dd/MM/yyyy hh:mm aa",cal).toString();
                            userStatusTv.setText("Last seen: "+dateTime);
                        }

                    }


                    chatNameTv.setText(name);
                    try {
                        Picasso.get()
                                .load(chatUserImage)
                                .placeholder(R.drawable.ic_default)
                                .into(profile_chatCiv);
                    }
                    catch (Exception e)
                    {
                        profile_chatCiv.setImageResource(R.drawable.ic_default);

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message=messageEt.getText().toString().trim();

                if (TextUtils.isEmpty(message))
                {
                    Toast.makeText(ChatActivity.this, "Please Type a message first...", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    notify=true;
                    sendMessage(message);
                }
                messageEt.setText("");
            }
        });


        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });

        messageEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length()==0)
                {
                    checkTypingStatus("noOne");
                }
                else
                {
                    checkTypingStatus(chatUserId);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        blockIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (isBlocked)
                {
                    unBlockUser();
                }
                else
                {
                    blockUser();
                }

            }
        });

        readMessages();

        checkIsBlocked();

        seenMessage();
    }
    private void checkIsBlocked() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(mAuth.getUid()).child("BlockedUsers").orderByChild("uid").equalTo(chatUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            if (ds.exists())
                            {
                                blockIv.setImageResource(R.drawable.ic_blocked_red);
                                isBlocked=true;
                                sendBtn.setVisibility(View.GONE);
                                messageEt.setVisibility(View.GONE);
                                attachBtn.setVisibility(View.GONE);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void unBlockUser() {

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
                                                blockIv.setImageResource(R.drawable.ic_unblocked_green);
                                                isBlocked=false;
                                                Toast.makeText(ChatActivity.this, "Unblocked successfully...", Toast.LENGTH_SHORT).show();
                                                sendBtn.setVisibility(View.VISIBLE);
                                                messageEt.setVisibility(View.VISIBLE);
                                                attachBtn.setVisibility(View.VISIBLE);
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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

    private void blockUser() {

        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("uid",chatUserId);

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");
        ref.child(myUid).child("BlockedUsers").child(chatUserId)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        isBlocked=true;
                        Toast.makeText(ChatActivity.this, "Blocked successfully...", Toast.LENGTH_SHORT).show();
                        sendBtn.setVisibility(View.GONE);
                        messageEt.setVisibility(View.GONE);
                        attachBtn.setVisibility(View.GONE);
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initializeFields() {
        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("");
        recyclerView=findViewById(R.id.chat_recycleView);
        profile_chatCiv=findViewById(R.id.profile_chatCIV);
        blockIv=findViewById(R.id.blockIv);
        chatNameTv=findViewById(R.id.chat_nameTv);
        userStatusTv=findViewById(R.id.userStatusTv);
        messageEt=findViewById(R.id.messageEt);
        sendBtn=findViewById(R.id.sendBtn);
        attachBtn=findViewById(R.id.attachBtn);
        mAuth=FirebaseAuth.getInstance();

        requestQueue= Volley.newRequestQueue(getApplicationContext());

        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);



        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        Intent intent=getIntent();
        chatUserId=intent.getStringExtra("chatUserId");

        firebaseDatabase=FirebaseDatabase.getInstance();
        usersRef=firebaseDatabase.getReference("Users");



    }
    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Image from");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }

                } else if (which == 1) {

                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    private void pickFromCamera() {

        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (cameraAccepted && writeStorageAccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please allow camera and storage permission...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please allow storage permission...", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this,storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        ActivityCompat.requestPermissions(this,cameraPermissions, CAMERA_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

               sendImageMessage(imageUri);

            }
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri=data.getData();
                sendImageMessage(imageUri);
            }
        }
    }


    private void sendMessage(final String message) {

        DatabaseReference databaseReference=FirebaseDatabase.getInstance().getReference();

        String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",myUid);
        hashMap.put("reciever",chatUserId);
        hashMap.put("message",message);
        hashMap.put("timestamp",timestamp);
        hashMap.put("isSeen",false);
        hashMap.put("type","text");
        databaseReference.child("Chats").push().setValue(hashMap);

        DatabaseReference database=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ModelUser user=dataSnapshot.getValue(ModelUser.class);
                if (notify)
                {
                    sendNotification(chatUserId,user.getName(),message);
                }
                notify=false;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        final DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(myUid)
                .child(chatUserId);
        chatRef1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    chatRef1.child("id").setValue(chatUserId);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chatlist")
                .child(chatUserId)
                .child(myUid);
        chatRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists())
                {
                    chatRef2.child("id").setValue(myUid);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void sendImageMessage(Uri imageUri) {

        notify=true;
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Sending image...");
        pd.show();

        final String timeStamp=""+System.currentTimeMillis();
        String fileNameAndPath="ChatImages/"+"post_"+timeStamp;

        Bitmap bitmap= null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100,baos);
            byte[] data=baos.toByteArray();

            StorageReference ref= FirebaseStorage.getInstance().getReference().child(fileNameAndPath);
            ref.putBytes(data)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            pd.dismiss();
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isSuccessful()) ;
                            String downloadUri = uriTask.getResult().toString();

                            if (uriTask.isSuccessful()) {
                                DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference();
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("sender",myUid);
                                hashMap.put("reciever",chatUserId);
                                hashMap.put("message",downloadUri);
                                hashMap.put("timestamp",timeStamp);
                                hashMap.put("type","image");
                                hashMap.put("isSeen",false);
                                dbRef.child("Chats").push().setValue(hashMap);

                                DatabaseReference db=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
                                db.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        ModelUser user=dataSnapshot.getValue(ModelUser.class);

                                        if (notify)
                                        {
                                            sendNotification(chatUserId,user.getName(),"Sent you a photo...");

                                        }
                                        notify=false;
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                final DatabaseReference chatRef1=FirebaseDatabase.getInstance().getReference("Chatlist")
                                        .child(myUid)
                                        .child(chatUserId);
                                chatRef1.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists())
                                        {
                                            chatRef1.child("id").setValue(chatUserId);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });

                                final DatabaseReference chatRef2=FirebaseDatabase.getInstance().getReference("Chatlist")
                                        .child(chatUserId)
                                        .child(myUid);
                                chatRef2.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        if (!dataSnapshot.exists())
                                        {
                                            chatRef2.child("id").setValue(myUid);
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });




                            }


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {

                    pd.dismiss();
                    Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } catch (IOException e) {
            Toast.makeText(ChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();


        }

    }

    private void sendNotification(final String chatUserId, final String name, final String message) {
        DatabaseReference allTokens=FirebaseDatabase.getInstance().getReference("Tokens");
        Query query=allTokens.orderByKey().equalTo(chatUserId);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Token token=ds.getValue(Token.class);
                    Data data=new Data(""+myUid,
                            ""+name+":"+message,
                            "New Message",
                            ""+chatUserId,
                            "ChatNotification",
                            R.drawable.smallicon_32);
                    Sender sender=new Sender(data,token.getToken());

                    try {
                        JSONObject senderJsonObj=new JSONObject(new Gson().toJson(sender));

                        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("https://fcm.googleapis.com/fcm/send", senderJsonObj,
                                new Response.Listener<JSONObject>() {
                                    @Override
                                    public void onResponse(JSONObject response) {
                                        Log.d("JSON_RESPONSE","onResponse: "+response.toString());

                                    }
                                }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d("JSON_RESPONSE","onResponse: "+error.toString());


                            }
                        }){
                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                Map<String,String> headers=new HashMap<>();
                                headers.put("Content-Type","application/json");
                                headers.put("Authorization","key=AAAAT4_i4S0:APA91bED85w65KCGunlv6YI0eX1puywQ8GH2hIT_Nr4w5UuDH8ZjxtdDQ909Gl30vrjAqpRJ9e7MITZzTtf-yWPdgnMVAP_3zQnRykx1j0h_HDGYCk9uucnMpIClYcCOJWdbUehX_M1y");

                                return headers;
                            }

                        };
                        requestQueue.add(jsonObjectRequest);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void seenMessage() {
        userRefForSeen=FirebaseDatabase.getInstance().getReference("Chats");
        seenListener=userRefForSeen.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if (chat.getReciever().equals(myUid) && chat.getSender().equals(chatUserId))
                    {
                        HashMap<String,Object> hasSeenHashMap=new HashMap<>();
                        hasSeenHashMap.put("isSeen",true);
                        ds.getRef().updateChildren(hasSeenHashMap);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void readMessages() {
        chatList=new ArrayList<>();
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Chats");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if (chat.getReciever().equals(myUid) &&  chat.getSender().equals(chatUserId)
                        || chat.getReciever().equals(chatUserId) &&  chat.getSender().equals(myUid))
                    {
                        chatList.add(chat);

                    }
                    adapterChat=new AdapterChat(ChatActivity.this,chatList,chatUserImage);

                    adapterChat.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterChat);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void checkUserStatus() {
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            myUid=user.getUid();
        }
        else
        {
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }
    }
    private void checkOnlineStatus(String status)
    {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("onlineStatus",status);
        dbRef.updateChildren(hashMap);
    }
    private void checkTypingStatus(String typing)
    {
        DatabaseReference dbRef=FirebaseDatabase.getInstance().getReference("Users").child(myUid);
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("typingTo",typing);
        dbRef.updateChildren(hashMap);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_group_info).setVisible(false);
        menu.findItem(R.id.action_games).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_news).setVisible(false);

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
        else if(id==R.id.action_report)
        {

            Intent reportIntent= new Intent(ChatActivity.this,LiveChatActivity.class);
            startActivity(reportIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        checkUserStatus();
        checkOnlineStatus("Online");
        super.onStart();
    }

    @Override
    protected void onPause() {
        super.onPause();
        String timestamp=String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        checkTypingStatus("noOne");
        userRefForSeen.removeEventListener(seenListener);
    }

    @Override
    protected void onDestroy() {
        String timestamp=String.valueOf(System.currentTimeMillis());
        checkOnlineStatus(timestamp);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkOnlineStatus("Online");
    }
}
