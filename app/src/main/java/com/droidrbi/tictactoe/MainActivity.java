package com.droidrbi.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];

    private int player1Score = 0;
    private int player2Score = 0;

    private TextView player1ScoreTextView;
    private TextView player2ScoreTextView;

    private boolean isPlayer1 = true;

    private HashMap<Button, Boolean> clickedButtons;
    private int numTilesPlayed = 0;

    private int[][] boardArr = new int[3][3];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1ScoreTextView = (TextView) findViewById(R.id.player1Score);
        player2ScoreTextView = (TextView) findViewById(R.id.player2Score);

        int i = 0, j = 0;
        String buttonId = "";
        String packageId = "id";
        for(; i < 3; i++){
            for(j = 0; j < 3; j++){
                buttonId = "gameButton" + i + j;

                buttons[i][j] = (Button) findViewById(this.getResources().getIdentifier(buttonId,packageId,getPackageName()));
                buttons[i][j].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        onTileClick(v);
                    }
                });
            }
        }
        clickedButtons = new HashMap<>();
        player1ScoreTextView.setText(String.valueOf(player1Score));
        player2ScoreTextView.setText(String.valueOf(player2Score));


    }

    private void initiateBoard() {
        int i = 0,j = 0;
        for(;i < 3; i++){
            for(j = 0; j < 3; j++){
                boardArr[i][j] = 0;
            }
        }
    }

    private void onTileClick(View v) {
        Button button = (Button) v;

        if(notClicked(button)){
            updateTile(button);
            tilePlayed(button);
        }


    }

    private void updateTile(Button button) {
        if(isPlayer1) {
            button.setText(getString(R.string.ex));
            isPlayer1 = false;

        }else{
            button.setText(getString(R.string.zero));
            isPlayer1 = true;
        }
    }

    private void tilePlayed(Button button) {
        clickedButtons.put(button, true);
    }

    private boolean notClicked(Button button) {
        if(clickedButtons.containsKey(button)){
            Toast.makeText(this, getString(R.string.tile_played_already), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
