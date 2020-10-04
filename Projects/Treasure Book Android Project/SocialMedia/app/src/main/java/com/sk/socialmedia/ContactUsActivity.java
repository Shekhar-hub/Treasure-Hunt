package com.sk.socialmedia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class ContactUsActivity extends AppCompatActivity {

    private Button liveChat;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);

        liveChat=findViewById(R.id.liveChat);


        liveChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // startActivity(new Intent(ContactUsActivity.this,LiveChatActivity.class));
                Intent intent= new Intent(ContactUsActivity.this,LiveChatActivity.class);

                startActivity(intent);

            }
        });

    }
}