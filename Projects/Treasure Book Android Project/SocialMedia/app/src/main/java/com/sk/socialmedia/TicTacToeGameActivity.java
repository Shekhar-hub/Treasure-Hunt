package com.sk.socialmedia;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.gridlayout.widget.GridLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class TicTacToeGameActivity extends AppCompatActivity {
    int winnerpos;
    boolean gameIsactive=true;
    int activeplayer=0;
    int[][] winningpositions={{0,1,2},{3,4,5},{6,7,8},{0,4,8},{2,4,6},{0,3,6},{1,4,7},{2,5,8}};
    int[] gamestate={2,2,2,2,2,2,2,2,2};  //2 means unplayed
    public void dropin(View view)
    {
        ImageView counter =(ImageView) view;
        int tappedcounter=Integer.parseInt(counter.getTag().toString());
        if(gamestate[tappedcounter]==2 && gameIsactive) {
            gamestate[tappedcounter] = activeplayer;
            counter.setTranslationY(-1000f);
            if (activeplayer == 0) {
                counter.setImageResource(R.drawable.o);
                activeplayer = 1;
            } else {
                counter.setImageResource(R.drawable.x);
                activeplayer = 0;
            }
            counter.animate().translationYBy(1000f).rotationYBy(3600).setDuration(0);
            for (int[] winningposition : winningpositions) {
                if (gamestate[winningposition[0]] == gamestate[winningposition[1]] &&
                        gamestate[winningposition[1]] == gamestate[winningposition[2]] &&
                        gamestate[winningposition[0]] != 2) {
                    gameIsactive = false;
                    winnerpos=gamestate[winningposition[0]];
                }
            }
            if (!gameIsactive) {
                String Winner = "X";
                if (winnerpos ==0) {
                    Winner = "O";
                }
                TextView winnermessage = findViewById(R.id.textView2);
                winnermessage.setText(Winner + " has won!");
                RelativeLayout linearLayout = findViewById(R.id.linearLayout);
                linearLayout.setVisibility(view.VISIBLE);
            }
            else
            {
                boolean gameisover=true;
                for(int counterstate:gamestate)
                {
                    if(counterstate==2)
                    {
                        gameisover=false;
                    }
                }
                if(gameisover)
                {
                    TextView winnermessage = findViewById(R.id.textView2);
                    winnermessage.setText("It's a Draw...");
                    RelativeLayout linearLayout = findViewById(R.id.linearLayout);
                    linearLayout.setVisibility(view.VISIBLE);
                }
            }
        }
    }
    public void playagain(View view)
    {
        gameIsactive=true;
        RelativeLayout linearLayout = findViewById(R.id.linearLayout);
        linearLayout.setVisibility(view.INVISIBLE);
        activeplayer=0;
        for(int i=0;i<gamestate.length;i++)
        {
            gamestate[i]=2;
        }
        GridLayout gridLayout = findViewById(R.id.gridLayout);
        //GridLayout gridLayout=findViewById(R.id.grid);
        for(int i=0;i<gridLayout.getChildCount();i++)
        {
            ((ImageView) gridLayout.getChildAt(i)).setImageResource(0);
        }
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tic_tac_toe_game);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setTitle("Tic Tac Toe");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF4081")));

        GridLayout gridLayout = findViewById(R.id.gridLayout);
        gridLayout.setTranslationX(-1000f);
        gridLayout.animate().translationXBy(1000f).setDuration(1000);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}