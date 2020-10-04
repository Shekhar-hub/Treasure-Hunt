package com.sk.socialmedia.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.sk.socialmedia.GamesActivity;
import com.sk.socialmedia.GroupCreateActivity;
import com.sk.socialmedia.MainActivity;
import com.sk.socialmedia.NewsActivity;
import com.sk.socialmedia.R;
import com.sk.socialmedia.SettingsActivity;
import com.sk.socialmedia.adapters.AdapterChatlist;
import com.sk.socialmedia.models.ModelChat;
import com.sk.socialmedia.models.ModelChatlist;
import com.sk.socialmedia.models.ModelUser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatListFragment extends Fragment {
    private FirebaseAuth mAuth;
    RecyclerView recyclerView;
    List<ModelChatlist> chatlistList;
    List<ModelUser> userList;
    DatabaseReference reference;
    FirebaseUser currentUser;
    AdapterChatlist adapterChatlist;

    public ChatListFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_chat_list, container, false);
        mAuth= FirebaseAuth.getInstance();
        currentUser=mAuth.getCurrentUser();

        recyclerView=view.findViewById(R.id.recyclerView_chatList);

        chatlistList=new ArrayList<>();

        reference= FirebaseDatabase.getInstance().getReference("Chatlist").child(currentUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                chatlistList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelChatlist chatlist=ds.getValue(ModelChatlist.class);
                    chatlistList.add(chatlist);
                }
                loadChats();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private void loadChats() {
        userList=new ArrayList<>();
        reference=FirebaseDatabase.getInstance().getReference("Users");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelUser user=ds.getValue(ModelUser.class);
                    for (ModelChatlist chatlist:chatlistList)
                    {
                        if (user.getUid()!=null && user.getUid().equals(chatlist.getId()))
                        {
                            userList.add(user);
                            break;
                        }
                    }
                    adapterChatlist=new AdapterChatlist(getContext(),userList);

                    recyclerView.setAdapter(adapterChatlist);
                    for (int i=0;i<userList.size();i++)
                    {
                        lastMessage(userList.get(i).getUid());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void lastMessage(final String userId) {
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Chats");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String theLastMessage="default";
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelChat chat=ds.getValue(ModelChat.class);
                    if (chat==null)
                    {
                        continue;

                    }
                    String sender=chat.getSender();
                    String reciever=chat.getReciever();
                    if (sender==null||reciever==null)
                    {
                        continue;
                    }
                    if (chat.getReciever().equals(currentUser.getUid())
                            && chat.getSender().equals(userId)
                            || chat.getReciever().equals(userId)
                            && chat.getSender().equals(currentUser.getUid()))
                    {
                        if (chat.getType().equals("image"))
                        {
                            theLastMessage="Sent a photo";

                        }
                        else {
                            theLastMessage = chat.getMessage();
                        }
                    }
                }
                adapterChatlist.setLastMessage(userId,theLastMessage);
                adapterChatlist.notifyDataSetChanged();
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
        menu.findItem(R.id.action_add_post).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_group_info).setVisible(false);
        menu.findItem(R.id.action_report).setVisible(false);

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
        else if (id==R.id.action_settings)
        {
            startActivity(new Intent(getActivity(), SettingsActivity.class));
        }
        else if (id==R.id.action_create_group)
        {
            startActivity(new Intent(getActivity(), GroupCreateActivity.class));
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
