package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Main2Activity extends AppCompatActivity {
    private String[] itemsAll;
    private ListView mSongsList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        mSongsList=findViewById(R.id.songs_list);

        appExternalStoragePermission();

    }
    public void appExternalStoragePermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        displayAudioSongs();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Main2Activity.this.finish();
                        System.exit(0);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
    public ArrayList<File> readOnlyAudioSongs(File file)
    {

        ArrayList<File> arrayList=new ArrayList<>();
        File[] allFiles=file.listFiles();
        for (File individualFile:allFiles)
        {
            if (individualFile.isDirectory() && !individualFile.isHidden()) {
               arrayList.addAll(readOnlyAudioSongs(individualFile));
            }
            else
            {
                if (individualFile.getName().endsWith(".mp3") || individualFile.getName().endsWith(".aac") || individualFile.getName().endsWith(".wav") || individualFile.getName().endsWith(".wma"))
                {
                   arrayList.add(individualFile);
                }
            }
        }
        return  arrayList;
    }
    private void displayAudioSongs()
    {

        final ArrayList<File> audioSongs=readOnlyAudioSongs(Environment.getExternalStorageDirectory());
        itemsAll=new String[audioSongs.size()];
        for (int songsCounter=0;songsCounter<audioSongs.size();songsCounter++)
        {
            itemsAll[songsCounter]=audioSongs.get(songsCounter).getName();

        }
        ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(Main2Activity.this,android.R.layout.simple_list_item_1,itemsAll);
        mSongsList.setAdapter(arrayAdapter);
        mSongsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String songName=mSongsList.getItemAtPosition(position).toString();

                Intent mainIntent=new Intent(Main2Activity.this,MainActivity.class);
                mainIntent.putExtra("song",audioSongs);
                mainIntent.putExtra("name",songName);
                mainIntent.putExtra("position",position);
                startActivity(mainIntent);
            }
        });

    }
}
