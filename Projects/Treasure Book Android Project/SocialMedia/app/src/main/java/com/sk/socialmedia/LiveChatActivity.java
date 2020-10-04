package com.sk.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.applozic.mobicomkit.api.account.register.RegistrationResponse;

import com.applozic.mobicomkit.api.account.user.UserService;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.kommunicate.KmConversationBuilder;
import io.kommunicate.Kommunicate;
import io.kommunicate.callbacks.KMLoginHandler;
import io.kommunicate.callbacks.KMLogoutHandler;
import io.kommunicate.callbacks.KmCallback;
import io.kommunicate.users.KMUser;

import static io.kommunicate.users.KMUser.isLoggedIn;

public class LiveChatActivity extends AppCompatActivity {
    private ProgressDialog pd;
    private FirebaseAuth mAuth;
    private TextView exitMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_chat);

        exitMessage=findViewById(R.id.exitMessage);

        mAuth=FirebaseAuth.getInstance();

        pd=new ProgressDialog(this);
        pd.setTitle("Connecting");
        pd.setMessage("Please wait while we setup for you...");

        Kommunicate.init(getApplicationContext(),"25e67a5e779d747e07b3bfea7847f25f4");
        List<String> botList = new ArrayList(); botList.add("sk-vujkl"); //enter your integrated bot Ids


        pd.show();


        final KMUser kmUser = new KMUser();
        kmUser.setUserId(mAuth.getCurrentUser().getUid());
        kmUser.setDisplayName(mAuth.getCurrentUser().getEmail());
        //kmUser.setImageLink(<NEW_IMAGE_URL>);


        new KmConversationBuilder(LiveChatActivity.this)
                .setSingleConversation(false) // Pass false if you would like to create new conversation every time user starts a chat. This is true by default which means only one conversation will open for the user every time the user starts a chat.
                .setBotIds(botList) // The conversation will be created with these bot(s)
                .launchConversation(new KmCallback() {
                    @Override
                    public void onSuccess(Object message) {
                        pd.dismiss();


                    }

                    @Override
                    public void onFailure(Object error) {

                        pd.dismiss();
                    }
                });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        exitMessage.setVisibility(View.VISIBLE);
    }
}