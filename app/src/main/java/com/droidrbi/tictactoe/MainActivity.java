package com.droidrbi.tictactoe;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private Button[][] buttons = new Button[3][3];

    private int player1Score = 0;
    private int player2Score = 0;

    private TextView player1ScoreTextView;
    private TextView player2ScoreTextView;

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

        player1ScoreTextView.setText(String.valueOf(player1Score));
        player2ScoreTextView.setText(String.valueOf(player2Score));


    }

    private void onTileClick(View v) {
        Button button = (Button) v;
        button.setText(getString(R.string.ex));

    }
}
