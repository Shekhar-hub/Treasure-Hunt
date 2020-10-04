package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.socialmedia.adapters.AdapterGroupChat;
import com.sk.socialmedia.models.ModelGroupChat;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class GroupChatActivity extends AppCompatActivity {



    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri imageUri=null;

    private String groupId,myGroupRole;
    private FirebaseAuth mAuth;
    private Toolbar toolbar;
    private ImageView groupIconIv;
    private TextView groupTitleTv;
    private EditText messageEt;
    private ImageButton sendBtn,attachBtn;

    private ArrayList<ModelGroupChat> groupChatList;
    private AdapterGroupChat adapterGroupChat;

    private RecyclerView groupChatRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_chat);


        InitializeFields();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};




        Intent intent=getIntent();
        groupId=intent.getStringExtra("groupId");


        loadMyGroupRole();

        loadGroupInfo();
        loadGroupMessages();


        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message=messageEt.getText().toString().trim();

                if (TextUtils.isEmpty(message))
                {
                    Toast.makeText(GroupChatActivity.this, "Please Type a message first...", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    sendMessage(message);
                }
            }
        });

        attachBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePicDialog();
            }
        });


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

                sendImageMessage();

            }
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri=data.getData();
                sendImageMessage();
            }
        }
    }

    private void sendImageMessage() {
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setMessage("Sending Image...");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        String fileNameAndPath="GroupChatImages/"+""+System.currentTimeMillis();

        StorageReference storageReference= FirebaseStorage.getInstance().getReference(fileNameAndPath);
        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {


                        Task<Uri> p_uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!p_uriTask.isSuccessful()) ;
                        String p_downloadUri = p_uriTask.getResult().toString();

                        if (p_uriTask.isSuccessful()) {

                            String timestamp=String.valueOf(System.currentTimeMillis());
                            HashMap<String,Object> hashMap=new HashMap<>();
                            hashMap.put("sender",""+mAuth.getUid());
                            hashMap.put("message",""+p_downloadUri);
                            hashMap.put("timestamp",""+timestamp);
                            hashMap.put("type","image");

                            DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
                            ref.child(groupId).child("Messages")
                                    .child(timestamp)
                                    .setValue(hashMap)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            messageEt.setText("");
                                            pd.dismiss();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(GroupChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });

                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        pd.dismiss();
                        Toast.makeText(GroupChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void loadMyGroupRole() {
        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Participants")
                .orderByChild("uid").equalTo(mAuth.getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            myGroupRole=""+ds.child("role").getValue();
                            //Log.i("role",myGroupRole);

                        }
                        if(myGroupRole!=null) {
                            invalidateOptionsMenu();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

    }

    private void loadGroupMessages() {
        groupChatList=new ArrayList<>();

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Messages")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        groupChatList.clear();
                        for (DataSnapshot ds:dataSnapshot.getChildren())
                        {
                            ModelGroupChat model=ds.getValue(ModelGroupChat.class);
                            groupChatList.add(model);
                        }
                        adapterGroupChat=new AdapterGroupChat(GroupChatActivity.this,groupChatList);
                        groupChatRv.setAdapter(adapterGroupChat);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void sendMessage(String message) {

        String timestamp=String.valueOf(System.currentTimeMillis());
        HashMap<String,Object> hashMap=new HashMap<>();
        hashMap.put("sender",""+mAuth.getUid());
        hashMap.put("message",""+message);
        hashMap.put("timestamp",""+timestamp);
        hashMap.put("type","text");

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(groupId).child("Messages")
                .child(timestamp)
                .setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        messageEt.setText("");
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(GroupChatActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
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
                            String groupTitle=""+ds.child("groupTitle").getValue();
                            String groupDescription=""+ds.child("groupDescription").getValue();
                            String groupIcon=""+ds.child("groupIcon").getValue();
                            String timestamp=""+ds.child("timestamp").getValue();
                            String createdBy=""+ds.child("createdBy").getValue();

                            groupTitleTv.setText(groupTitle);
                            try {
                                Picasso.get().load(groupIcon).placeholder(R.drawable.ic_group_white).into(groupIconIv);

                            }
                            catch (Exception e)
                            {
                                groupIconIv.setImageResource(R.drawable.ic_group_white);
                            }

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
    }

    private void InitializeFields() {
        toolbar=findViewById(R.id.toolbar_group);

        groupIconIv=findViewById(R.id.groupIconIv_chat);
        groupTitleTv=findViewById(R.id.groupTitleTv_chat);
        attachBtn=findViewById(R.id.attachBtn_group);
        messageEt=findViewById(R.id.messageEt_group);
        sendBtn=findViewById(R.id.sendBtn_group);
        groupChatRv=findViewById(R.id.groupChatRv);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getApplicationContext());
        layoutManager.setStackFromEnd(true);
      
        groupChatRv.setLayoutManager(layoutManager);
        //recyclerView.setNestedScrollingEnabled(false);

        mAuth=FirebaseAuth.getInstance();
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);

        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_search).setVisible(false);
        menu.findItem(R.id.action_games).setVisible(false);
        menu.findItem(R.id.action_settings).setVisible(false);
        menu.findItem(R.id.action_news).setVisible(false);

        Log.i("info",""+myGroupRole);
        if (myGroupRole!=null) {
            if (myGroupRole.equals("creator") || myGroupRole.equals("admin")) {
                menu.findItem(R.id.action_add_participant).setVisible(true);

            } else {
                menu.findItem(R.id.action_add_participant).setVisible(false);


            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_add_participant)
        {
            Intent intent=new Intent(this,GroupParticipantsAddActivity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
        else  if (id==R.id.action_group_info)
        {
            Intent intent=new Intent(this,GroupInfoActivity.class);
            intent.putExtra("groupId",groupId);
            startActivity(intent);
        }
        else if(id==R.id.action_report)
        {
            Intent reportIntent= new Intent(GroupChatActivity.this,LiveChatActivity.class);

            startActivity(reportIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
