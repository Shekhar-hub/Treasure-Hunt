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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class GroupCreateActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private FirebaseAuth mAuth;

    private ProgressDialog pd;

    private ImageView groupIconIv;
    private EditText groupTitleEt,groupDescriptionEt;
    private FloatingActionButton createGroupBtn;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;


    private String[] cameraPermissions;
    private String[] storagePermission;
    private Uri imageUri=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_create);

        actionBar=getSupportActionBar();
        actionBar.setTitle("Create Group");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        InitializeFields();

        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        mAuth=FirebaseAuth.getInstance();
        checkUserStatus();

        groupIconIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showImagePicDialog();
            }
        });
        createGroupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startCreatingGroup();

            }
        });

    }

    private void startCreatingGroup() {

        pd=new ProgressDialog(this);
        pd.setMessage("Creating Group");

        final String groupTitle=groupTitleEt.getText().toString().trim();
        final String groupDescription=groupDescriptionEt.getText().toString().trim();

        if (TextUtils.isEmpty(groupTitle))
        {
            Toast.makeText(this, "Please enter group name...", Toast.LENGTH_SHORT).show();
            return;
        }
        pd.show();

        final String g_timestamp=""+System.currentTimeMillis();
        if (imageUri==null)
        {

            createGroup(
                    ""+g_timestamp,
                    ""+groupTitle,
                    ""+groupDescription,
                    ""
            );
        }
        else
        {

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
                                createGroup(
                                        ""+g_timestamp,
                                        ""+groupTitle,
                                        ""+groupDescription,
                                        ""+p_downloadUri
                                );
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    pd.dismiss();
                    Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
    }

    private void createGroup(final String g_timestamp, String groupTitle, String groupDescription, String groupIcon)
    {
        HashMap<String,String> hashMap=new HashMap<>();
        hashMap.put("groupId",""+g_timestamp);
        hashMap.put("groupTitle",""+groupTitle);
        hashMap.put("groupDescription",""+groupDescription);
        hashMap.put("groupIcon",""+groupIcon);
        hashMap.put("timestamp",""+g_timestamp);
        hashMap.put("createdBy",""+mAuth.getUid());

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Groups");
        ref.child(g_timestamp).setValue(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        HashMap<String,String> hashMap1=new HashMap<>();
                        hashMap1.put("uid",mAuth.getUid());
                        hashMap1.put("role","creator");
                        hashMap1.put("timestamp",g_timestamp);

                        DatabaseReference ref1=FirebaseDatabase.getInstance().getReference("Groups");
                        ref1.child(g_timestamp).child("Participants").child(mAuth.getUid())
                                .setValue(hashMap1)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        pd.dismiss();
                                        Toast.makeText(GroupCreateActivity.this, "Group created successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(GroupCreateActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void InitializeFields() {
        groupIconIv=findViewById(R.id.groupIconIv);
        groupTitleEt=findViewById(R.id.groupTitleEt);
        groupDescriptionEt=findViewById(R.id.groupDescriptionEt);
        createGroupBtn=findViewById(R.id.createGroupBtn);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
