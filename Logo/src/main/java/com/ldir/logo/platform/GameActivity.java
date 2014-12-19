package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.fieldviews.MissionField;
import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.music.IBXMPlayer;

import java.io.IOException;

public class GameActivity extends Activity {

    private final int NEXT_LEVEL_ACTIVITY = 1;
    private final int GAME_WIN_ACTIVITY = 2;

	private GameField gameField;
	private MissionField missionField;
    private IBXMPlayer music;


    private void processFieldChange()
    {
        gameField.drawField();
    }

    private void makeMove(GameMap.Pos clickPos)
    {
        if(Game.makeMove(clickPos)) {
            processFieldChange();
            if(Game.win) {
                if(Game.lastLevel())
                    startGameWinActitity();
                 else
                    startNextLevelActitity();
            }
        }
    }

    private void nextLevel()
    {
        if (Game.nextlevel() ) {
            missionField.invalidate();
        } else {
            Game.reset();
            Log.e("Error", "No more levels");
            Toast.makeText(this, "That`s all folks", Toast.LENGTH_SHORT).show();
        }
        processFieldChange();
    }

    private void newGame()
    {
        Game.startGame();
        processFieldChange();
        missionField.invalidate();
    }

    private void undo()
    {
        if(Game.undo())
            processFieldChange();
    }

    private void reset()
    {
        Game.reset();
        processFieldChange();
    }

    // ***************************************************************************
    // Всё что выше - управление игровой логикой
    // Всё что ниже - обработка событий от пользователя
    // ***************************************************************************

    private void startNextLevelActitity()
    {
        Intent intent = new Intent(this, NextLevelActivity.class);
        intent.putExtra("level", "message");
        startActivityForResult(intent,NEXT_LEVEL_ACTIVITY);
    }
    private void startGameWinActitity()
    {
        Intent intent = new Intent(this, GameWinActivity.class);
        startActivityForResult(intent,GAME_WIN_ACTIVITY);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case NEXT_LEVEL_ACTIVITY:
                nextLevel();
                break;
            case GAME_WIN_ACTIVITY:
                newGame();
                break;
        }
    }

    private class OnFieldPressed implements GameField.FieldPressHandler
    {
        @Override
        public void onPress(GameMap.Pos clickPos) {
//            Log.i("Verbose","Field pressed"+clickPos.row+","+clickPos.col);
            makeMove(clickPos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.menu_undo:
            undo();
            return true;
        case R.id.menu_newgame:
            newGame();
            return true;
        case R.id.menu_reset:
            reset();
            return true;
         default:
            return super.onOptionsItemSelected(item);
        }
    }

    private void setMusicState(Boolean en) {
        if (en) {
            if (music == null) {
                try {
                    music = new IBXMPlayer(getAssets().open("1.xm"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else {
            if (music != null) {
                music.close();
                music = null;
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        setMusicState(true);
        gameField.setAnimationEnable(true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        setMusicState(false);
        gameField.setAnimationEnable(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
//    	MissionLoader.load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameField = (GameField)findViewById(R.id.fieldSurface);
        gameField.setFieldPressHandler(new OnFieldPressed());
        missionField = (MissionField)findViewById(R.id.misionView);
    }
}
