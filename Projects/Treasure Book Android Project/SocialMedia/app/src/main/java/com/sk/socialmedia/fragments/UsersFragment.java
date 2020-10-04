package com.sk.socialmedia.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.sk.socialmedia.adapters.AdapterUsers;
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
public class UsersFragment extends Fragment {
    RecyclerView recyclerView;
    List<ModelUser> userList;
    AdapterUsers adapterUsers;
    private FirebaseAuth mAuth;

    public UsersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_users, container, false);
        recyclerView=view.findViewById(R.id.recyclerView);
        mAuth=FirebaseAuth.getInstance();
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userList=new ArrayList<>();
        getAllUsers();
        return view;
    }

    private void getAllUsers() {
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelUser modelUser=ds.getValue(ModelUser.class);

                    if (!modelUser.getUid().equals(fuser.getUid()))
                    {
                        userList.add(modelUser);
                    }
                    adapterUsers=new AdapterUsers(getActivity(),userList);
                    recyclerView.setAdapter(adapterUsers);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void searchUsers(final String query) {
        final FirebaseUser fuser= FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Users");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelUser modelUser=ds.getValue(ModelUser.class);

                    if (!modelUser.getUid().equals(fuser.getUid())) {
                        if (modelUser.getName().toLowerCase().contains(query.toLowerCase())
                                || modelUser.getEmail().toLowerCase().contains(query.toLowerCase()))
                        {
                            userList.add(modelUser);
                        }
                    }
                    adapterUsers=new AdapterUsers(getActivity(),userList);
                    adapterUsers.notifyDataSetChanged();
                    recyclerView.setAdapter(adapterUsers);
                }

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

        MenuItem item=menu.findItem(R.id.action_search);


        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query.trim()))
                {
                    searchUsers(query);
                }
                else
                {
                    getAllUsers();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText.trim()))
                {
                    searchUsers(newText);
                }
                else
                {
                    getAllUsers();
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
