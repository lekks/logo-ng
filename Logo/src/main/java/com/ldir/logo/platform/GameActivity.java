package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.fieldviews.MissionField;
import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends Activity {

    private final int NEXT_LEVEL_ACTIVITY = 1;
    private final int GAME_WIN_ACTIVITY = 2;

	private GameField gameField;
	private MissionField missionField;

    private void processFieldChange()
    {
        gameField.drawField();
    }

    private Observer onFieldChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            processFieldChange();
        }
    };

    private Observer onMissionChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            missionField.invalidate();
        }
    };

    public Observer onGameChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            Game.StateChange state = (Game.StateChange)arg;
            switch (state.oldState) {
                case GAME_WIN_MENU:
                    finish();
                    Game.restartGame();
                    break;
            }
            switch (state.newState) {
                case GAME_WIN:
                    startActivityForResult(new Intent(GameActivity.this, GameWinActivity.class),GAME_WIN_ACTIVITY);
                    break;
                case LEVEL_COMPLETE:
                    startActivityForResult( new Intent(GameActivity.this, NextLevelActivity.class),NEXT_LEVEL_ACTIVITY);
                    break;
            }
        }
    };


/*
    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case NEXT_LEVEL_ACTIVITY:
                break;
            case GAME_WIN_ACTIVITY:
                break;
        }
    }
*/


    private class OnFieldPressed implements GameField.FieldPressHandler
    {
        @Override
        public void onPress(GameMap.Pos clickPos) {
            Game.makeMove(clickPos);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.menu_undo:
            Game.undo();
            return true;
        case R.id.menu_newgame:
            Game.restartGame();
            return true;
        case R.id.menu_reset:
            Game.reset();
            return true;
         default:
            return super.onOptionsItemSelected(item);
        }
    }

 /*   public Observer onFieldTransitionEnd = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
        }
    };*/

    @Override
    protected void onResume() {
        super.onResume();
        Game.enterPlayground();
//        gameField.setAnimationEnable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        gameField.setAnimationEnable(false);
        Game.exitPlayground();
    }


        @Override
    protected void onStart() {
        super.onStart();
        Game.fieldChanged.addObserver(onFieldChange);
        Game.missionChanged.addObserver(onMissionChange);
        gameField.transitionEndEvent.addObserver(Game.onFieldTransitionEnd);
//        gameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Game.fieldChanged.deleteObserver(onFieldChange);
        Game.missionChanged.deleteObserver(onMissionChange);
        gameField.transitionEndEvent.deleteObserver(Game.onFieldTransitionEnd);
//        gameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        gameField = (GameField)findViewById(R.id.fieldSurface);
        gameField.setFieldPressHandler(new OnFieldPressed());
        missionField = (MissionField)findViewById(R.id.misionView);
        Game.observedState.addObserver(onGameChange);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Game.observedState.deleteObserver(onGameChange);
        gameField.destroy();
    }

}
