package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

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
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

public class GroupEditActivity extends AppCompatActivity {

    private String groupId;
    private ActionBar actionBar;

    private ImageView groupIconIv;
    private EditText groupTitleEt,groupDescriptionEt;
    private FloatingActionButton updateGroupBtn;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri imageUri=null;

    private FirebaseAuth mAuth;

    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_edit);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Edit Group");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        groupId=getIntent().getStringExtra("groupId");

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        pd=new ProgressDialog(this);
        pd.setTitle("Please wait");
        pd.setCanceledOnTouchOutside(false);

        mAuth=FirebaseAuth.getInstance();
        checkUserStatus();

        loadGroupInfo();

        groupIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImagePicDialog();
            }
        });
        updateGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startUpdatingGroup();

            }
        });

    }

    private void startUpdatingGroup() {

        final String groupTitle=groupTitleEt.getText().toString().trim();
        final String groupDescription=groupDescriptionEt.getText().toString().trim();

        if (TextUtils.isEmpty(groupTitle))
        {
            Toast.makeText(this, "Please enter group name...", Toast.LENGTH_SHORT).show();
            return;
        }

        pd.setMessage("Updating Group Info...");
        pd.show();
        if (imageUri==null) {

            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("groupTitle",""+groupTitle);
            hashMap.put("groupDescription",""+groupDescription);

            DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
            ref.child(groupId).updateChildren(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {

                            pd.dismiss();
                            Toast.makeText(GroupEditActivity.this, "Group Info updated successfully...", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(GroupEditActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {

            final String g_timestamp=""+System.currentTimeMillis();
            String fileNameAndPath="Group_Imgs/"+"image"+g_timestamp;

            StorageReference storageReference= FirebaseStorage.getInstance().getReference(fileNameAndPath);
            storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Task<Uri> p_uriTask=taskSnapshot.getStorage().getDownloadUrl();
                            while (!p_uriTask.isSuccessful());

                            Uri p_downloadUri=p_uriTask.getResult();
                            if (p_uriTask.isSuccessful())
                            {

                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("groupTitle",""+groupTitle);
                                hashMap.put("groupDescription",""+groupDescription);
                                hashMap.put("groupIcon", ""+p_downloadUri);

                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Groups");
                                ref.child(groupId).updateChildren(hashMap)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {

                                                pd.dismiss();
                                                Toast.makeText(GroupEditActivity.this, "Group Info updated successfully...", Toast.LENGTH_SHORT).show();
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        pd.dismiss();
                                        Toast.makeText(GroupEditActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(GroupEditActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
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


                            groupTitleEt.setText(groupTitle);
                            groupDescriptionEt.setText(groupDescription);

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

    private void InitializeFields() {
        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleEt=findViewById(R.id.groupTitleEt);
        groupDescriptionEt=findViewById(R.id.groupDescriptionEt);
        updateGroupBtn=findViewById(R.id.updateGroupBtn);

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

                groupIconIv.setImageURI(imageUri);

            }
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri=data.getData();
                groupIconIv.setImageURI(imageUri);
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void checkUserStatus() {
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            actionBar.setSubtitle(user.getEmail());
        }
        else
        {
        }
    }

}
