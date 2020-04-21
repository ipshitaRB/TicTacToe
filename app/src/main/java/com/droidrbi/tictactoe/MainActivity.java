package com.droidrbi.tictactoe;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    private static final String PLAYER_1_SCORE = "PLAYER 1 SCORE";
    private static final String PLAYER_2_SCORE = "PLAYER 2 SCORE";
    private static final String CLICKED_BUTTON_IDS = "Button ids";

    private Button[][] buttons = new Button[3][3];

    private int player1Score = 0;
    private int player2Score = 0;

    private TextView player1ScoreTextView;
    private TextView player2ScoreTextView;

    private boolean isPlayer1Turn = true;

    private HashMap<Integer, Boolean> clickedButtons;
    private int numTilesPlayed = 0;

    private int[][] boardArr = new int[3][3];

    private boolean gameFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        player1ScoreTextView = findViewById(R.id.player1Score);
        player2ScoreTextView = findViewById(R.id.player2Score);

        Button resetButton = findViewById(R.id.resetButton);

        resetButton.setOnClickListener((view) -> reset());

        int i = 0, j;
        String buttonId;
        String packageId = "id";
        for(; i < 3; i++){
            for(j = 0; j < 3; j++){
                buttonId = "gameButton" + i + j;

                buttons[i][j] = findViewById(this.getResources().getIdentifier(buttonId, packageId, getPackageName()));
                buttons[i][j].setOnClickListener(this::onTileClick);
            }
        }
        clickedButtons = new HashMap<>();
        if(savedInstanceState != null){
            player1Score = savedInstanceState.getInt(PLAYER_1_SCORE);
            player2Score = savedInstanceState.getInt(PLAYER_2_SCORE);
            int[] clickedButtonIds = savedInstanceState.getIntArray(CLICKED_BUTTON_IDS);
            if (clickedButtonIds != null) {
                for (int id :
                        clickedButtonIds) {
                    Log.d("Main Activity", String.valueOf(id));
                }
            }
        }
        player1ScoreTextView.setText(String.valueOf(player1Score));
        player2ScoreTextView.setText(String.valueOf(player2Score));

        initiateBoard();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(PLAYER_1_SCORE, player1Score);
        outState.putInt(PLAYER_2_SCORE, player2Score);
        // save clicked buttons
        int[] buttonIds = getButtonIds();

        outState.putIntArray(CLICKED_BUTTON_IDS, buttonIds);

        // save player turn
        // save X and O information

    }

    private int[] getButtonIds() {

        int[] buttonIds = new int[clickedButtons.keySet().size()];
        int pos = 0;
        for (int buttonId :
                clickedButtons.keySet()) {
            buttonIds[pos] = buttonId;
            pos++;
        }
        return buttonIds;
    }


    private void reset() {
        gameFinished = false;
        clearBoardArr();
        clearClickedButtonsList();
        unclickTiles();
        numTilesPlayed = 0;
        isPlayer1Turn = true;
    }

    private void clearBoardArr(){
        int i = 0, j;
        for (; i < 3;i++){
            for(j = 0; j < 3;j++){
                boardArr[i][j] = 0;
            }
        }
    }

    private void clearClickedButtonsList(){
        clickedButtons.clear();
    }

    private void unclickTiles(){
        int i = 0, j;
        for (; i < 3;i++){
            for(j = 0; j < 3;j++){
                buttons[i][j].setText("");
            }
        }
    }



    private void initiateBoard() {
        int i = 0, j;
        for(;i < 3; i++){
            for(j = 0; j < 3; j++){
                boardArr[i][j] = 0;
            }
        }
    }

    private void onTileClick(View v) {
        Button button = (Button) v;

        if(gameStillOn() && notClicked(button)){
            tilePlayed(button, isPlayer1Turn);
            updateTile(button);
            // find which player has won
            if(hasPlayerWon()){
                // isPlayer1 was switched in updateTile()
                // Therefore, if isPlayer1 is true then player 2 played the latest move and
                // hence is the winner
                int winner = isPlayer1Turn ? 2:1;
                Log.i("Main Activity", "Player " + winner + " is the winner");
                gameFinished = true;
                showPlayerWinsMessage(winner);
                updateScore();
            }

        }


    }

    private void showPlayerWinsMessage(int winner) {
        String text = getString(R.string.player_wins,winner);
        Toast.makeText(this, text,Toast.LENGTH_LONG).show();
    }


    private boolean gameStillOn() {
        if(gameFinished){
            //toast game finished press reset to start a new game
            Toast.makeText(this, getString(R.string.game_finished), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    private void updateScore() {

        if(isPlayer1Turn){
            player2Score++;
            player2ScoreTextView.setText(String.valueOf(player2Score));
        }else{
            player1Score++;
            player1ScoreTextView.setText(String.valueOf(player1Score));
        }
    }


    private void updateBoard( Button button, boolean isPlayer1) {
        int resId = button.getId();
        String resName = button.getResources().getResourceEntryName(resId);
        Log.i("Main Activity", resName);
        // eg : resName = gameButton00
        int length = resName.length();
        int i = Character.getNumericValue(resName.charAt(length - 2));
        int j = Character.getNumericValue(resName.charAt(length - 1));
        boardArr[i][j] = isPlayer1? 1:2;
    }

    private boolean hasPlayerWon() {
        numTilesPlayed++;
        if(numTilesPlayed > 4){
//            8 possible ways of winning 2 diagonals + 3 verticals + 3 horizontals

            boolean isGameWon = isDiagonalAligned() || isRowAligned() || isColumnAligned();
            if(numTilesPlayed == 9 && !isGameWon){
                nobodyWon();
            }
            return isGameWon;

        }

        return false;
    }

    private void nobodyWon() {
        Toast.makeText(this, getString(R.string.nobody_won), Toast.LENGTH_LONG).show();
    }

    private boolean isColumnAligned() {
        for(int col = 0; col < 3; col++){

            if(boardArr[0][col] > 0 && boardArr[0][col] == boardArr[1][col] && boardArr[1][col] == boardArr[2][col] )
                return true;

        }
        return false;
    }

    private boolean isRowAligned() {
        for(int row = 0; row < 3; row++){

            if(boardArr[row][0] > 0 && boardArr[row][0] == boardArr[row][1] && boardArr[row][1] == boardArr[row][2])
                return true;

        }
        return false;
    }

    private boolean isDiagonalAligned() {
        return (boardArr[0][0] == boardArr[1][1] && boardArr[1][1] == boardArr[2][2])
                || (boardArr[2][0] == boardArr[1][1] && boardArr[1][1] == boardArr[0][2])
                && boardArr[1][1] > 0;
    }

    private void updateTile(Button button) {
        updateBoard(button, isPlayer1Turn);
        if(isPlayer1Turn) {
            button.setText(getString(R.string.ex));
            isPlayer1Turn = false;
        }else{
            button.setText(getString(R.string.zero));
            isPlayer1Turn = true;
        }

    }

    private void tilePlayed(Button button, boolean isPlayer1Turn) {

        clickedButtons.put(button.getId(), isPlayer1Turn);
    }

    private boolean notClicked(Button button) {
        if (clickedButtons.containsKey(button.getId())) {
            Toast.makeText(this, getString(R.string.tile_played_already), Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }
}
