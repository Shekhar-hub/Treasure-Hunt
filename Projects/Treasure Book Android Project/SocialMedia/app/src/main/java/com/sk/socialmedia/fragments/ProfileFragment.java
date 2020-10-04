package com.sk.socialmedia.fragments;


import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.content.ContextCompat;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sk.socialmedia.AddPostActivity;
import com.sk.socialmedia.GamesActivity;
import com.sk.socialmedia.MainActivity;
import com.sk.socialmedia.NewsActivity;
import com.sk.socialmedia.R;
import com.sk.socialmedia.SettingsActivity;
import com.sk.socialmedia.adapters.AdapterPosts;
import com.sk.socialmedia.models.ModelPost;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
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
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends Fragment {
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseUser user;
    private ImageView profileImage, coverImage;
    private TextView nameTv, emailTv, statusTv;
    private FloatingActionButton fab;
    private ProgressDialog pd;
    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    private String cameraPermissions[];
    private String storagePermission[];
    private Uri imageUri=null;
    private String profileOrCoverPhoto;
    private StorageReference storageReference;
    private String storagePath="Users_Profile_Cover_Imgs";

    private RecyclerView postsRecyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    String uid;

    public ProfileFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");
        storageReference= FirebaseStorage.getInstance().getReference();

        profileImage = view.findViewById(R.id.profile_image);
        nameTv = view.findViewById(R.id.name_tv);
        emailTv = view.findViewById(R.id.email_tv);
        statusTv = view.findViewById(R.id.status_tv);
        coverImage = view.findViewById(R.id.coverIv);
        fab = view.findViewById(R.id.fab);
        postsRecyclerView = view.findViewById(R.id.recyclerView_posts);

        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        postsRecyclerView.setLayoutManager(layoutManager);
        postsRecyclerView.setNestedScrollingEnabled(false);


        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        pd = new ProgressDialog(getActivity());

        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = "" + ds.child("name").getValue();
                        String email = "" + ds.child("email").getValue();
                        String status = "" + ds.child("status").getValue();
                        String image = "" + ds.child("image").getValue();
                        String cover = "" + ds.child("cover").getValue();

                        nameTv.setText(name);
                        emailTv.setText(email);
                        statusTv.setText(status);

                        try {
                            Picasso.get().load(image).into(profileImage);

                        } catch (Exception e) {
                            Picasso.get().load(R.drawable.ic_add_image).into(profileImage);
                        }

                        try {
                            Picasso.get().load(cover).into(coverImage);
                        } catch (Exception e) {
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditProfileDialog();
            }
        });

        postList=new ArrayList<>();
        checkUserStatus();
        loadMyPosts();
        return view;

    }

    private void loadMyPosts() {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        Query query=ref.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost myPosts=ds.getValue(ModelPost.class);
                    postList.add(myPosts);

                    adapterPosts=new AdapterPosts(getActivity(),postList);
                    postsRecyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                try {
                    Toast.makeText(getContext(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {

                }
            }
        });
    }

    private void searchMyPosts(final String searchQuery) {

        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
        Query query=ref.orderByChild("uid").equalTo(uid);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost myPosts=ds.getValue(ModelPost.class);

                    if (myPosts.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())||
                            myPosts.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(myPosts);
                    }

                    adapterPosts=new AdapterPosts(getActivity(),postList);
                    postsRecyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void requestStoragePermission() {
        requestPermissions(storagePermission, STORAGE_REQUEST_CODE);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    private void requestCameraPermission() {
        requestPermissions(cameraPermissions, CAMERA_REQUEST_CODE);
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
                        Toast.makeText(getActivity(), "Please allow camera and storage permission...", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
            case STORAGE_REQUEST_CODE:
                if (grantResults.length > 0) {
                    boolean writeStorageAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageAccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(getActivity(), "Please allow storage permission...", Toast.LENGTH_SHORT).show();
                    }


                }
                break;
        }
    }

    private void showEditProfileDialog() {
        String options[] = {"Edit Profile Picture", "Edit Cover Picture", "Edit Name", "Edit Status","Change Password"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    pd.setMessage("Updating Profile Picture");
                    profileOrCoverPhoto="image";
                    showImagePicDialog();

                } else if (which == 1) {
                    pd.setMessage("Updating Cover Picture");
                    profileOrCoverPhoto="cover";
                    showImagePicDialog();

                } else if (which == 2) {
                    pd.setMessage("Updating Name");
                    showNameStatusUpdateDialog("name");
                } else if (which == 3) {
                    pd.setMessage("Updating Status");
                    showNameStatusUpdateDialog("status");
                } else if (which == 4) {
                    pd.setMessage("Changing Password");
                    showChangePasswordDialog();
                }
            }
        });
        builder.create().show();
    }

    private void showChangePasswordDialog() {


        View view=LayoutInflater.from(getActivity()).inflate(R.layout.dialog_update_password,null);

        final EditText passwordEt=view.findViewById(R.id.oldPassword_et);
        final EditText newPasswordEt=view.findViewById(R.id.newPassword_et);
        Button updatePasswordBtn=view.findViewById(R.id.updatePasswordBtn);

        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setView(view);

        final AlertDialog dialog=builder.create();
        dialog.show();

        updatePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword=passwordEt.getText().toString().trim();
                String newPassword=newPasswordEt.getText().toString().trim();
                if (TextUtils.isEmpty(oldPassword))
                {
                    Toast.makeText(getActivity(), "Enter your current password...", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (newPassword.length()<6)
                {
                    Toast.makeText(getActivity(), "Password length must atleast 6 characters...", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
                updatePassword(oldPassword,newPassword);
            }
        });
    }

    private void updatePassword(String oldPassword, final String newPassword) {

        pd.show();

        final FirebaseUser user=mAuth.getCurrentUser();

        AuthCredential authCredential= EmailAuthProvider.getCredential(user.getEmail(),oldPassword);
        user.reauthenticate(authCredential)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        user.updatePassword(newPassword)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        pd.dismiss();
                                        Toast.makeText(getActivity(), "Password updated successfully...", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                pd.dismiss();
                                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showNameStatusUpdateDialog(final String key) {
        AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        builder.setTitle("Update "+key);
        LinearLayout linearLayout=new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(10,10,10,10);
        final EditText editText=new EditText(getActivity());
        editText.setHint("Enter "+key);
        linearLayout.addView(editText);
        builder.setView(linearLayout);
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String value=editText.getText().toString().trim();
                if (!TextUtils.isEmpty(value))
                {
                    pd.show();
                    HashMap<String,Object> result=new HashMap<>();
                    result.put(key,value);
                    databaseReference.child(user.getUid()).updateChildren(result)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Updated Successfully...", Toast.LENGTH_SHORT).show();

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });

                    if (key.equals("name"))
                    {
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                        Query query=ref.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren())
                                {
                                    String child=ds.getKey();
                                    dataSnapshot.getRef().child(child).child("uName").setValue(value);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds:dataSnapshot.getChildren())
                                {
                                    final String child=ds.getKey();
                                    if (dataSnapshot.child(child).hasChild("Comments"))
                                    {
                                        String child1=""+dataSnapshot.child(child).getKey();
                                        Query child2=FirebaseDatabase.getInstance().getReference("Posts").child(child1).child("Comments").orderByChild("uid").equalTo(uid);

                                        child2.addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds:dataSnapshot.getChildren())
                                                {
                                                    dataSnapshot.getRef().child(child).child("uName").setValue(value);
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {

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

                }
                else
                {
                    Toast.makeText(getActivity(), "Please Enter "+key, Toast.LENGTH_SHORT).show();
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

            }
        });
    builder.create().show();
    }

    private void showImagePicDialog() {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose Action");
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
        imageUri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_PICK_CAMERA_CODE) {

                uploadProfileCoverPhoto(imageUri);

            }
            if (requestCode == IMAGE_PICK_GALLERY_CODE) {
                imageUri=data.getData();
                uploadProfileCoverPhoto(imageUri);
            }
        }
    }

    private void uploadProfileCoverPhoto(Uri imageUri) {
        pd.show();

        String filePathAndName=""+profileOrCoverPhoto+"_"+user.getUid();
        StorageReference storageReference2=storageReference.child(storagePath).child(filePathAndName);
        storageReference2.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask=taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isSuccessful());
                        final Uri downloadUri=uriTask.getResult();

                        if (uriTask.isSuccessful())
                        {
                            HashMap<String,Object> results=new HashMap<>();
                            results.put(profileOrCoverPhoto,downloadUri.toString());
                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            pd.dismiss();
                                            Toast.makeText(getActivity(), ""+profileOrCoverPhoto+" updated successfully...", Toast.LENGTH_SHORT).show();

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    pd.dismiss();
                                    Toast.makeText(getActivity(), "Error updating Image...\n["+e.getMessage()+"]", Toast.LENGTH_SHORT).show();

                                }
                            });
                            if (profileOrCoverPhoto.equals("image"))
                            {
                                DatabaseReference ref=FirebaseDatabase.getInstance().getReference("Posts");
                                Query query=ref.orderByChild("uid").equalTo(uid);
                                query.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds:dataSnapshot.getChildren())
                                        {
                                            String child=ds.getKey();
                                            dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {

                                    }
                                });
                                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds:dataSnapshot.getChildren())
                                        {
                                            final String child=ds.getKey();
                                            if (dataSnapshot.child(child).hasChild("Comments"))
                                            {
                                                String child1=""+dataSnapshot.child(child).getKey();
                                                Query child2=FirebaseDatabase.getInstance().getReference("Posts").child(child1).child("Comments").orderByChild("uid").equalTo(uid);

                                                child2.addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot ds:dataSnapshot.getChildren())
                                                        {
                                                            dataSnapshot.getRef().child(child).child("uDp").setValue(downloadUri.toString());
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {

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

                        }
                        else
                        {
                            pd.dismiss();
                            Toast.makeText(getActivity(), "Error:Please Try Later...", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               pd.dismiss();
                Toast.makeText(getActivity(), ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void checkUserStatus() {
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            uid=user.getUid();

        }
        else
        {
            startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        }
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main,menu);

        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_group_info).setVisible(false);
        MenuItem item=menu.findItem(R.id.action_search);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query))
                {
                    searchMyPosts(query);
                }
                else
                {
                    loadMyPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText))
                {
                    searchMyPosts(newText);
                }
                else
                {
                    loadMyPosts();
                }
                return false;
            }
        });

        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.action_logout)
        {
            mAuth.signOut();
            checkUserStatus();
        }
        else if (id==R.id.action_add_post)
        {
            startActivity(new Intent(getActivity(), AddPostActivity.class));
        }
        else if (id==R.id.action_settings)
        {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
        else if (id==R.id.action_games)
        {
            startActivity(new Intent(getActivity(), GamesActivity.class));
        }
        else if (id==R.id.action_news)
        {
            startActivity(new Intent(getActivity(), NewsActivity.class));
            getActivity().finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
