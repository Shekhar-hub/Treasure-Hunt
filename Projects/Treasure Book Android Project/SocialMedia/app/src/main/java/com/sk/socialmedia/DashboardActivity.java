package com.sk.socialmedia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;

import com.sk.socialmedia.fragments.ChatListFragment;
import com.sk.socialmedia.fragments.GroupChatsFragment;
import com.sk.socialmedia.fragments.HomeFragment;
import com.sk.socialmedia.fragments.NotificationsFragment;
import com.sk.socialmedia.fragments.ProfileFragment;
import com.sk.socialmedia.fragments.UsersFragment;
import com.sk.socialmedia.notification.Token;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;

public class DashboardActivity extends AppCompatActivity {
private FirebaseAuth mAuth;
private ActionBar actionBar;
private BottomNavigationView navigationView;
private String mUID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        actionBar=getSupportActionBar();
        actionBar.setTitle("Profile");
        InitializeFields();
        navigationView=findViewById(R.id.navigation);
        navigationView.setOnNavigationItemSelectedListener(selectedListener);
        actionBar.setTitle("Home");
        HomeFragment fragment1=new HomeFragment();
        FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
        ft1.replace(R.id.content,fragment1,"");
        ft1.commit();

        checkUserStatus();
         }

    @Override
    protected void onResume() {
        checkUserStatus();
        super.onResume();
    }

    public void updateToken(String token)
    {
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Tokens");
        Token mToken=new Token(token);
        ref.child(mUID).setValue(mToken);
    }
private BottomNavigationView.OnNavigationItemSelectedListener selectedListener
        =new BottomNavigationView.OnNavigationItemSelectedListener() {
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.nav_home:
                actionBar.setTitle("Home");
                HomeFragment fragment1=new HomeFragment();
                FragmentTransaction ft1=getSupportFragmentManager().beginTransaction();
                ft1.replace(R.id.content,fragment1,"");
                ft1.commit();

                return true;

            case R.id.nav_profile:
                actionBar.setTitle("Profile");
                ProfileFragment fragment2=new ProfileFragment();
                FragmentTransaction ft2=getSupportFragmentManager().beginTransaction();
                ft2.replace(R.id.content,fragment2,"");
                ft2.commit();
                return true;
            case R.id.nav_users:
                actionBar.setTitle("Users");
                UsersFragment fragment3=new UsersFragment();
                FragmentTransaction ft3=getSupportFragmentManager().beginTransaction();
                ft3.replace(R.id.content,fragment3,"");
                ft3.commit();
                return true;
            case R.id.nav_chat:
                actionBar.setTitle("Chats");
                ChatListFragment fragment4=new ChatListFragment();
                FragmentTransaction ft4=getSupportFragmentManager().beginTransaction();
                ft4.replace(R.id.content,fragment4,"");
                ft4.commit();
                return true;

            case R.id.nav_more:
                showMoreOptions();
                return true;

        }
        return false;
    }
};


    private void showMoreOptions() {

        PopupMenu popupMenu=new PopupMenu(this,navigationView, Gravity.END);
        popupMenu.getMenu().add(Menu.NONE,0,0,"Notifications");
        popupMenu.getMenu().add(Menu.NONE,1,0,"Group Chats");

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                int id= menuItem.getItemId();
                if (id==0)
                {
                    actionBar.setTitle("Notifications");
                    NotificationsFragment fragment5=new NotificationsFragment();
                    FragmentTransaction ft5=getSupportFragmentManager().beginTransaction();
                    ft5.replace(R.id.content,fragment5,"");
                    ft5.commit();

                }
                else  if (id==1)
                {
                    actionBar.setTitle("Group Chats");
                    GroupChatsFragment fragment6=new GroupChatsFragment();
                    FragmentTransaction ft6=getSupportFragmentManager().beginTransaction();
                    ft6.replace(R.id.content,fragment6,"");
                    ft6.commit();

                }
                return  false;
            }
        });
        popupMenu.show();
    }

    private void InitializeFields() {
        mAuth=FirebaseAuth.getInstance();
    }

    private void checkUserStatus() {
        FirebaseUser user=mAuth.getCurrentUser();
        if (user!=null)
        {
            Log.i("error","ok");
            mUID=user.getUid();
            SharedPreferences sp=getSharedPreferences("SP_USER",MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putString("Current_USERID",mUID);
            editor.apply();
            updateToken(FirebaseInstanceId.getInstance().getToken());


        }
        else
        {
            Log.i("error","notok");
            startActivity(new Intent(DashboardActivity.this,MainActivity.class));
            finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        checkUserStatus();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
