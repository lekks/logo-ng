package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.music.Music;

import java.util.Observable;
import java.util.Observer;

public class GameActivity extends Activity {

    private final int NEXT_LEVEL_ACTIVITY = 1;
    private final int GAME_WIN_ACTIVITY = 2;
    private final int GAME_LOST_ACTIVITY = 3;
    private final int GAME_OPT_ACTIVITY = 4;

	private GameField mGameField;
	private LevelField mLevelField;
    private TextView mTimeLabel;
    private TextView mLevelLabel;
    private int mLevelTime;
    private Handler mUI_handler = new Handler();
    private Runnable mUpdateTimerLabel = new Runnable() {
        @Override
        public void run() {
            mTimeLabel.setText(getString(R.string.timeLabelText)+String.format("%d:%02d", mLevelTime/60, mLevelTime % 60));
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
            mLevelField.setLevel(Game.getCurrenLevel());
            mLevelField.invalidate();
        }
    };

    private Observer onTimeChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            mLevelTime = (Integer)arg;
            mUI_handler.postDelayed(mUpdateTimerLabel,0);
        }
    };

    public Observer onGameChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            Game.StateChange state = (Game.StateChange) arg;

            switch (state.newState) {
                case GAME_OVER:
                    finish();
                    Game.restartGame();
                    break;
                case GAME_LOST:
                    startActivityForResult(new Intent(GameActivity.this, TimeoutActivity.class), GAME_LOST_ACTIVITY);
                    break;
                case GAME_COMPLETE:
                    startActivityForResult(new Intent(GameActivity.this, GameWinActivity.class), GAME_WIN_ACTIVITY);
                    break;
                case LEVEL_COMPLETE:
                    startActivityForResult(new Intent(GameActivity.this, NextLevelActivity.class), NEXT_LEVEL_ACTIVITY);
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case NEXT_LEVEL_ACTIVITY:
                Game.skipLevel();
                break;
            case GAME_WIN_ACTIVITY:
                Game.gameOver();
                break;
            case GAME_LOST_ACTIVITY:
                Game.gameOver();
                break;
            case GAME_OPT_ACTIVITY:
//                Game.exitOptScreen();
                if (resultCode == RESULT_OK && data != null) {
                    int cmd = data.getIntExtra(GameOptActvity.CMD,0);
                    switch (cmd) {
                        case GameOptActvity.CMD_EXIT:
                            finish();
                            break;
                        case GameOptActvity.CMD_RESET:
                            Game.reset();
                            break;
                        case GameOptActvity.CMD_RESTART:
                            Game.restartGame();
                            break;
                    }
                }
                break;
        }
    }

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

    public void onOptionButton(View v){
        startActivityForResult(new Intent(GameActivity.this, GameOptActvity.class), GAME_OPT_ACTIVITY);
    }

    public void onUndoButton(View v){
        Game.undo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("GameActivity", "Resume");
        Game.enterPlayground();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("GameActivity", "Restart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("GameActivity", "Pause");
        Game.exitPlayground();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GameActivity", "Start");
        Game.timerChanged.addObserver(onTimeChange);
        Game.fieldChanged.addObserver(onFieldChange);
        Game.missionChanged.addObserver(onMissionChange);
        mGameField.transitionEndEvent.addObserver(Game.onFieldTransitionEnd);
        Music.setMusicOn(true);
//        mGameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("GameActivity", "Stop");
        Game.fieldChanged.deleteObserver(onFieldChange);
        Game.missionChanged.deleteObserver(onMissionChange);
        mGameField.transitionEndEvent.deleteObserver(Game.onFieldTransitionEnd);
        Music.setMusicOn(false);
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
        Log.i("GameActivity", "Create");
        setContentView(R.layout.activity_game);
        mGameField = (GameField)findViewById(R.id.fieldSurface);
        mGameField.setFieldPressHandler(new OnFieldPressed());
        mLevelField = (LevelField)findViewById(R.id.misionView);
        mLevelField.setLevel(Game.getCurrenLevel());

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
        Log.i("GameActivity", "Destroy");
        Game.observedState.deleteObserver(onGameChange);
        mGameField.destroy();
        mLevelField.destroy();
    }

}
