package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
    private static final int RC_SIGN_IN =100 ;
    private GoogleSignInClient mGoogleSignInClient;
    private EditText mEmailEt,mPasswordEt;
    private Button mLoginBtn;
    private TextView mNotHaveAccountTv,mRecoverPassTv;
    private ProgressDialog progressDialog;
    private FirebaseAuth mAuth;
    private SignInButton mGoogleLoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        InitializeFields();
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Login");
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient=GoogleSignIn.getClient(this,gso);
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email=mEmailEt.getText().toString().trim();
                String password=mPasswordEt.getText().toString().trim();
                if (!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    mEmailEt.setError("Invalid Email");
                    mEmailEt.setFocusable(true);
                }
                else if(password.length()<6)
                {
                    mPasswordEt.setError("Password Length at least 6 characters");
                    mPasswordEt.setFocusable(true);
                }
                else
                {
                    login(email,password);
                }

            }
        });
        mNotHaveAccountTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this,RegisterActivity.class));
                finish();
            }
        });
        mRecoverPassTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRecoverPassDialog();
            }
        });
        mGoogleLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);

            }
        });

    }


    private void InitializeFields() {
        mAuth=FirebaseAuth.getInstance();
        mEmailEt=findViewById(R.id.email_et);
        mPasswordEt=findViewById(R.id.password_et);
        mLoginBtn=findViewById(R.id.loginBtn);
        mNotHaveAccountTv=findViewById(R.id.not_have_account_Tv);
        mGoogleLoginButton=findViewById(R.id.googleLoginButton);
        mRecoverPassTv=findViewById(R.id.recoverPass_Tv);
        progressDialog=new ProgressDialog(this);
       // progressDialog.setMessage("Logging In...");

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private void login(String email, String password) {
        progressDialog.setMessage("Logging In...");
        progressDialog.show();
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            FirebaseUser user=mAuth.getCurrentUser();
                            Intent intent=new Intent(LoginActivity.this, DashboardActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            //finish();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(LoginActivity.this, "Authentication failed...", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    private void showRecoverPassDialog() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setTitle("Recover Password");
        LinearLayout linearLayout=new LinearLayout(this);
        final EditText emailEt=new EditText(this);
        emailEt.setHint("Email");
        emailEt.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailEt.setMinEms(16);
        linearLayout.addView(emailEt);
        linearLayout.setPadding(10,10,10,10);
        builder.setView(linearLayout);
        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String email=emailEt.getText().toString().trim();
                beginRecovery(email);

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

    private void beginRecovery(String email) {
        progressDialog.setMessage("Sending Email...");
        progressDialog.show();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(LoginActivity.this, "Email sent successfully...", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            Toast.makeText(LoginActivity.this, "Failed...", Toast.LENGTH_SHORT).show();

                        }
                        progressDialog.dismiss();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this,""+e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });
    }
    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //Log.d(TAG, "signInWithCredential:success");
                            if (task.getResult().getAdditionalUserInfo().isNewUser())
                            {
                                FirebaseUser user = mAuth.getCurrentUser();
                                String email=user.getEmail();
                                String uid=user.getUid();
                                HashMap<Object,String> hashMap=new HashMap<>();
                                hashMap.put("email",email);
                                hashMap.put("uid",uid);
                                hashMap.put("name","");
                                hashMap.put("onlineStatus","Online");
                                hashMap.put("typingTo","noOne");
                                hashMap.put("status","");
                                hashMap.put("image","");
                                hashMap.put("cover","");
                                FirebaseDatabase database=FirebaseDatabase.getInstance();
                                DatabaseReference reference=database.getReference("Users");
                                reference.child(uid).setValue(hashMap);
                            }

                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                            finish();
                            //updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            //Log.w(TAG, "signInWithCredential:failure", task.getException());
                            //Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                            Toast.makeText(LoginActivity.this, "Login Failed...", Toast.LENGTH_SHORT).show();
                        }


                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(LoginActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken());
            } catch (ApiException e) {
                Toast.makeText(this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }
    }


}
