package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;

public class SettingsActivity extends AppCompatActivity {

    private SwitchCompat postSwitch;
    private CardView contactUs,about;

    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private static final String TOPIC_POST_NOTIFICATION="POST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

       ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Settings");

        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        postSwitch=findViewById(R.id.postSwitch);
        contactUs=findViewById(R.id.contactUs);
        about=findViewById(R.id.about);

        contactUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,ContactUsActivity.class));

            }
        });

        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingsActivity.this,AboutActivity.class));

            }
        });

        sp=getSharedPreferences("Notification_SP",MODE_PRIVATE);
        boolean isPostEnabled=sp.getBoolean(""+TOPIC_POST_NOTIFICATION,false);

        if (isPostEnabled)
        {
            postSwitch.setChecked(true);
        }
        else
        {
            postSwitch.setChecked(false);
        }

        postSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {

                editor=sp.edit();
                editor.putBoolean(""+TOPIC_POST_NOTIFICATION,isChecked);
                editor.apply();

                if (isChecked)
                {
                    subscribePostNotification();
                }
                else
                {
                    unsubscribePostNotification();

                }
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    private void unsubscribePostNotification() {

        FirebaseMessaging.getInstance().unsubscribeFromTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg="You will not receive post notifications";
                        if (!task.isSuccessful())
                        {
                            msg="UnSubscription failed";
                        }
                        Toast.makeText(SettingsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void subscribePostNotification() {

        FirebaseMessaging.getInstance().subscribeToTopic(""+TOPIC_POST_NOTIFICATION)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg="You will receive post notifications";
                        if (!task.isSuccessful())
                        {
                            msg="Subscription failed";
                        }
                        Toast.makeText(SettingsActivity.this, ""+msg, Toast.LENGTH_SHORT).show();
                    }
                });

    }
}
