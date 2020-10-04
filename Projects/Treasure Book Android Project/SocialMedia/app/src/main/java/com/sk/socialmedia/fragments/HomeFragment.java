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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sk.socialmedia.AddPostActivity;
import com.sk.socialmedia.GamesActivity;
import com.sk.socialmedia.MainActivity;
import com.sk.socialmedia.NewsActivity;
import com.sk.socialmedia.R;
import com.sk.socialmedia.SettingsActivity;
import com.sk.socialmedia.adapters.AdapterPosts;
import com.sk.socialmedia.models.ModelPost;
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
public class HomeFragment extends Fragment {


    RecyclerView recyclerView;
    List<ModelPost> postList;
    AdapterPosts adapterPosts;
    private FirebaseAuth mAuth;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_home, container, false);

        mAuth=FirebaseAuth.getInstance();
        recyclerView=view.findViewById(R.id.postsRecyclerView);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getActivity());
        layoutManager.setStackFromEnd(true);
        layoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setNestedScrollingEnabled(false);

        postList=new ArrayList<>();

        loadPosts();
        return view;
    }
    private void searchPosts(final String searchQuery) {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost modelPost=ds.getValue(ModelPost.class);

                    if (modelPost.getpTitle().toLowerCase().contains(searchQuery.toLowerCase())||
                        modelPost.getpDescr().toLowerCase().contains(searchQuery.toLowerCase())) {
                        postList.add(modelPost);
                    }
                    adapterPosts=new AdapterPosts(getActivity(),postList);

                    recyclerView.setAdapter(adapterPosts);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(), ""+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }
    private void loadPosts() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference("Posts");
        
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList.clear();
                for (DataSnapshot ds:dataSnapshot.getChildren())
                {
                    ModelPost modelPost=ds.getValue(ModelPost.class);
                    postList.add(modelPost);
                    adapterPosts=new AdapterPosts(getActivity(),postList);
                    
                    recyclerView.setAdapter(adapterPosts);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                try {
                    Toast.makeText(getActivity(), "DB Error:"+databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
                catch (Exception e)
                {

                }
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

        menu.findItem(R.id.action_create_group).setVisible(false);
        menu.findItem(R.id.action_add_participant).setVisible(false);
        menu.findItem(R.id.action_group_info).setVisible(false);
        menu.findItem(R.id.action_report).setVisible(false);

        MenuItem item=menu.findItem(R.id.action_search);

        SearchView searchView= (SearchView) MenuItemCompat.getActionView(item);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                if (!TextUtils.isEmpty(query))
                {
                    searchPosts(query);
                }
                else
                {
                    loadPosts();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (!TextUtils.isEmpty(newText))
                {
                    searchPosts(newText);
                }
                else
                {
                    loadPosts();
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
