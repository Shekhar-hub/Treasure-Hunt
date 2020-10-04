package com.example.musicplayer;

import androidx.appcompat.app.AppCompatActivity;
import com.squareup.picasso.Picasso;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends AppCompatActivity {
    private RelativeLayout parentRelativeLayout;
    private SpeechRecognizer speechRecognizer;

    private Intent speechRecognizerIntent;
    private String keeper="";
    private ImageView playPlauseBtn,prevBtn,nextBtn;
    private ImageView logo;
    private TextView songNameTxt;
    private RelativeLayout lowerRelativeLayout;
    private ImageButton voiceEnabledBtn;
    private String mode="ON",mSongName;
    private MediaPlayer myMediaPlayer;
    private int position;
    private ArrayList<File> mySongs;
    private SeekBar progressBar;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        InitializeFields();
        checkVoiceCommandPermission();

        speechRecognizer=SpeechRecognizer.createSpeechRecognizer(MainActivity.this);
        speechRecognizerIntent=new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        speechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());

        validateRecievedValuesAndStartPlaying();
        //logo.setBackgroundResource(R.drawable.logo);

        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                progressBar.setProgress(myMediaPlayer.getCurrentPosition());
            }
        },0,1000);
        speechRecognizer.setRecognitionListener(new RecognitionListener() {
            @Override
            public void onReadyForSpeech(Bundle params) {
                //Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onBeginningOfSpeech() {

            }

            @Override
            public void onRmsChanged(float rmsdB) {

            }

            @Override
            public void onBufferReceived(byte[] buffer) {

            }

            @Override
            public void onEndOfSpeech() {

            }

            @Override
            public void onError(int error) {

            }

            @Override
            public void onResults(Bundle results) {
                ArrayList<String> matchesFound=results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
                if (matchesFound!=null)
                {
                    keeper=matchesFound.get(0);
                    if (keeper.equals("pause the song"))
                    {
                        myMediaPlayer.start();
                        playPauseSongs();
                        Toast.makeText(MainActivity.this, "Command: "+keeper, Toast.LENGTH_LONG).show();
                    }
                    else if(keeper.equals("play the song"))
                    {
                        myMediaPlayer.pause();
                        playPauseSongs();
                        Toast.makeText(MainActivity.this, "Command: "+keeper, Toast.LENGTH_LONG).show();
                    }
                    else if(keeper.equals("play the next song"))
                    {
                        playNextSong();
                        Toast.makeText(MainActivity.this, "Command: "+keeper, Toast.LENGTH_LONG).show();
                    }
                    else if(keeper.equals("play the previous song"))
                    {
                        playPreviousSong();
                        Toast.makeText(MainActivity.this, "Command: "+keeper, Toast.LENGTH_LONG).show();
                    }

                }

            }

            @Override
            public void onPartialResults(Bundle partialResults) {

            }

            @Override
            public void onEvent(int eventType, Bundle params) {

            }
        });

        parentRelativeLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        speechRecognizer.startListening(speechRecognizerIntent);
                        Toast.makeText(MainActivity.this, "Listening", Toast.LENGTH_SHORT).show();

                        keeper="";

                        break;
                    case MotionEvent.ACTION_UP:
                        speechRecognizer.stopListening();
                       // Toast.makeText(MainActivity.this, "Stop Listening", Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
        voiceEnabledBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mode.equals("ON"))
                {
                    mode="OFF";
                    voiceEnabledBtn.setBackgroundResource(R.drawable.ic_mic_off);
                    Toast.makeText(MainActivity.this, "Mic-Off", Toast.LENGTH_SHORT).show();
                    lowerRelativeLayout.setVisibility(View.VISIBLE);
                    speechRecognizer.stopListening();
                }
                else
                {
                    mode="ON";
                    voiceEnabledBtn.setBackgroundResource(R.drawable.ic_mic_on);
                    Toast.makeText(MainActivity.this, "Mic-On\nHold the screen and start speaking... ", Toast.LENGTH_SHORT).show();
                    lowerRelativeLayout.setVisibility(View.GONE);

                }
            }
        });
        playPlauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playPauseSongs();
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMediaPlayer.getCurrentPosition()>0)
                {
                   playPreviousSong();
                }

            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (myMediaPlayer.getCurrentPosition()>0)
                {
                    playNextSong();
                }

            }
        });
        progressBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser)
                    myMediaPlayer.seekTo(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.pause();

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                myMediaPlayer.start();

            }
        });
    }

    private void InitializeFields() {
        playPlauseBtn=findViewById(R.id.play_pause_btn);
        prevBtn=findViewById(R.id.prev_btn);
        nextBtn=findViewById(R.id.next_btn);
        songNameTxt=findViewById(R.id.songsName);
        logo=findViewById(R.id.logo);
        parentRelativeLayout=findViewById(R.id.parentRelativeLayout);
        lowerRelativeLayout=findViewById(R.id.lower);
        voiceEnabledBtn=findViewById(R.id.voice_enabled_btn);
        progressBar=findViewById(R.id.progress_bar);
    }

    private void validateRecievedValuesAndStartPlaying()
    {
        if (myMediaPlayer!=null)
        {
            myMediaPlayer.stop();
            myMediaPlayer.release();
        }
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        mySongs= (ArrayList) bundle.getParcelableArrayList("song");
        mSongName=mySongs.get(position).getName();
        String songName=intent.getStringExtra("name");
        songNameTxt.setText(songName);
        songNameTxt.setSelected(true);
        position=bundle.getInt("position",0);
        Uri uri=Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(MainActivity.this,uri);
        myMediaPlayer.start();
        progressBar.setMax(myMediaPlayer.getDuration());
    }


    private void checkVoiceCommandPermission()
    {
        Dexter.withActivity(this)
                .withPermission(Manifest.permission.RECORD_AUDIO)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        //displayAudioSongs();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        MainActivity.this.finish();
                        System.exit(0);
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void playPauseSongs()
    {
        //logo.setBackgroundResource(R.drawable.two);
        Picasso.get().load(R.drawable.two).into(logo);
        if (myMediaPlayer.isPlaying())
        {
            playPlauseBtn.setImageResource(R.drawable.play);
            myMediaPlayer.pause();

        }
        else
        {
            playPlauseBtn.setImageResource(R.drawable.pause);
            myMediaPlayer.start();
            //logo.setBackgroundResource(R.drawable.three);
            Picasso.get().load(R.drawable.three).into(logo);

        }
    }
    private void playNextSong()
    {
        myMediaPlayer.pause();
        myMediaPlayer.stop();
        myMediaPlayer.release();
        position=((position+1)%mySongs.size());
        Uri uri=Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(MainActivity.this,uri);

        mSongName=mySongs.get(position).getName();
        songNameTxt.setText(mSongName);
        myMediaPlayer.start();
        progressBar.setMax(myMediaPlayer.getDuration());
        if (myMediaPlayer.isPlaying())
        {
            playPlauseBtn.setImageResource(R.drawable.pause);
            //myMediaPlayer.pause();
        }
        else
        {
            playPlauseBtn.setImageResource(R.drawable.play);
            //myMediaPlayer.start();
           // logo.setBackgroundResource(R.drawable.three);
            Picasso.get().load(R.drawable.three).into(logo);

        }

    }
    private void playPreviousSong()
    {
        myMediaPlayer.pause();
        myMediaPlayer.stop();
        myMediaPlayer.release();
        position=((position-1)<0?(mySongs.size()-1):(position-1));
        Uri uri=Uri.parse(mySongs.get(position).toString());
        myMediaPlayer=MediaPlayer.create(MainActivity.this,uri);

        mSongName=mySongs.get(position).getName();
        songNameTxt.setText(mSongName);
        myMediaPlayer.start();
        progressBar.setMax(myMediaPlayer.getDuration());
        if (myMediaPlayer.isPlaying())
        {
            playPlauseBtn.setImageResource(R.drawable.pause);
           // myMediaPlayer.pause();
        }
        else
        {
            playPlauseBtn.setImageResource(R.drawable.play);
            //myMediaPlayer.start();
            //logo.setBackgroundResource(R.drawable.three);
            Picasso.get().load(R.drawable.logo).into(logo);

        }

    }

}
