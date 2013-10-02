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

public class MainActivity extends Activity {

    private final int NEXT_LEVEL_ACTIVITY = 1;

	private GameField gameField;
	private MissionField missionField;

    private void processFieldChange()
    {
        gameField.drawField();
    }

    private void makeMove(GameMap.Pos clickPos)
    {
        Game.makeMove(clickPos);
        processFieldChange();
        if(Game.win) {
            startNextLevelActitity();
        }

    }

    private void nextLevel()
    {
        if (Game.nextlevel() ==false ) {
            Game.reset();
            Toast.makeText(this, "That`s all folks", Toast.LENGTH_SHORT).show();
        } else {
            missionField.invalidate();
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
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        if(requestCode == NEXT_LEVEL_ACTIVITY) {
            nextLevel();
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
        setContentView(R.layout.activity_main);
        gameField = (GameField)findViewById(R.id.fieldSurface);
        gameField.setFieldPressHandler(new OnFieldPressed());
        missionField = (MissionField)findViewById(R.id.misionView);
    }
}
