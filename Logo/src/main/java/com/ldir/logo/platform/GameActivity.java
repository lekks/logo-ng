package com.ldir.logo.platform;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

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

	private GameField mGameField;
	private MissionField mMissionField;
    private TextView mTimeLabel;
    private TextView mLevelLabel;
    private int mLevelTime;
    private Handler mUI_handler = new Handler();
    private Runnable mUpdateTimerLabel = new Runnable() {
        @Override
        public void run() {
            mTimeLabel.setText(String.format("%d:%02d", mLevelTime, mLevelTime));
        }
    };

    private void processFieldChange()
    {
        mGameField.drawField();
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
            mMissionField.invalidate();
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

    private Observer onTimeChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            mLevelTime = (Integer)arg;
            mUI_handler.postDelayed(mUpdateTimerLabel,0);
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
//        mGameField.setAnimationEnable(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
//        mGameField.setAnimationEnable(false);
        Game.exitPlayground();
    }


        @Override
    protected void onStart() {
        super.onStart();
        Game.timerChanged.addObserver(onTimeChange);
        Game.fieldChanged.addObserver(onFieldChange);
        Game.missionChanged.addObserver(onMissionChange);
        mGameField.transitionEndEvent.addObserver(Game.onFieldTransitionEnd);
//        mGameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Game.fieldChanged.deleteObserver(onFieldChange);
        Game.missionChanged.deleteObserver(onMissionChange);
        mGameField.transitionEndEvent.deleteObserver(Game.onFieldTransitionEnd);
//        mGameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

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
        mGameField = (GameField)findViewById(R.id.fieldSurface);
        mGameField.setFieldPressHandler(new OnFieldPressed());
        mMissionField = (MissionField)findViewById(R.id.misionView);

        mTimeLabel = (TextView) findViewById(R.id.timeLabel);
        mLevelLabel = (TextView) findViewById(R.id.levelLabel);
        TextView patternLabel = (TextView) findViewById(R.id.patternLabel);

        Typeface font = Typeface.createFromAsset(getAssets(),"zebulon.ttf");

        mTimeLabel.setTypeface(font);
        mLevelLabel.setTypeface(font);
        patternLabel.setTypeface(font);

        Game.observedState.addObserver(onGameChange);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Game.observedState.deleteObserver(onGameChange);
        mGameField.destroy();
    }

}
