package com.sk.socialmedia.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.sk.socialmedia.GamesActivity;
import com.sk.socialmedia.GroupCreateActivity;
import com.sk.socialmedia.MainActivity;
import com.sk.socialmedia.NewsActivity;
import com.sk.socialmedia.R;
import com.sk.socialmedia.SettingsActivity;
import com.sk.socialmedia.adapters.AdapterGroupChatList;
import com.sk.socialmedia.models.ModelGroupChatList;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class GroupChatsFragment extends Fragment {


    private RecyclerView groupsRv;
    private FirebaseAuth mAuth;
    private ArrayList<ModelGroupChatList> groupChatLists;
    private AdapterGroupChatList adapterGroupChatList;


    public GroupChatsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_group_chats, container, false);

        groupsRv=view.findViewById(R.id.groupListRv);

        mAuth=FirebaseAuth.getInstance();

        loadGroupChatsList();

        return view;
    }

    private void loadGroupChatsList() {

        groupChatLists=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if (ds.child("Participants").child(mAuth.getUid()).exists())
                    {
                        ModelGroupChatList model=ds.getValue(ModelGroupChatList.class);
                        groupChatLists.add(model);

                    }
                }
                adapterGroupChatList=new AdapterGroupChatList(getActivity(),groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchGroupChatsList(final String query) {

        groupChatLists=new ArrayList<>();
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference("Groups");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                groupChatLists.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    if (ds.child("Participants").child(mAuth.getUid()).exists())
                    {
                        if (ds.child("groupTitle").toString().toLowerCase().contains(query.toLowerCase())) {
                            ModelGroupChatList model = ds.getValue(ModelGroupChatList.class);
                            groupChatLists.add(model);
                        }
                    }
                }
                adapterGroupChatList=new AdapterGroupChatList(getActivity(),groupChatLists);
                groupsRv.setAdapter(adapterGroupChatList);
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

        MenuItem item=menu.findItem(R.id.action_search);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim()))
                {
                    searchGroupChatsList(query);
                }
                else
                {
                    loadGroupChatsList();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim()))
                {
                    searchGroupChatsList(newText);
                }
                else
                {
                    loadGroupChatsList();
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
