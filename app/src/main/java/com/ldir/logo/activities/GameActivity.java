package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.GamePlay;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.game.Levels;
import com.ldir.logo.sound.GameSound;
import com.ldir.logo.sound.Music;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameActivity extends Activity {

    private final int NEXT_LEVEL_ACTIVITY = 1;
    private final int GAME_WIN_ACTIVITY = 2;
    private final int GAME_LOST_ACTIVITY = 3;
    private final int GAME_OPT_ACTIVITY = 4;

    private GameSound clickSound;
    private static GamePlay game;
    private ScheduledFuture mTimerFuture;
    private ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();

    private GameField mGameField;
    private LevelField mLevelField;
    private TextView mTimeLabel;
    private TextView mLevelLabel;
    private Handler mUI_handler = new Handler();

    private void processFieldChange() {
        mGameField.drawField();
    }

    private Observer onFieldChange = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            processFieldChange();
        }
    };
    private Observer onMissionChange = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            updateMissionViews();        }
    };

    private Observer onTimeChange = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            int levelTime = (Integer) arg;
            mTimeLabel.setText(getString(R.string.timeLabelText) + String.format("%02d:%02d", levelTime / 60, levelTime % 60));
        }
    };

    public Observer onGameChange = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            GamePlay.GameState state = (GamePlay.GameState) arg;

            switch (state) {
                case GAME_OVER:
                    finish();
                    game.restartGame(0);
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

    public void updateMissionViews() {
        Levels.saveCurrentLevel(game.getCurrenLevel());
        mLevelField.setLevel(game.getCurrenLevel());
        mLevelField.invalidate();
        mLevelLabel.setText("Level "+Integer.toString(game.getCurrenLevel()+1));
    }



    public Observer onFieldTransitionEnd = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            game.moveCompleted();
        }
    };

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data)
    {
        switch (requestCode) {
            case NEXT_LEVEL_ACTIVITY:
                game.nextLevel();
                break;
            case GAME_WIN_ACTIVITY:
                game.gameOver();
                break;
            case GAME_LOST_ACTIVITY:
                game.gameOver();
                break;
            case GAME_OPT_ACTIVITY:
//                game.exitOptScreen();
                if (resultCode == RESULT_OK && data != null) {
                    int cmd = data.getIntExtra(GameOptActvity.CMD,0);
                    switch (cmd) {
                        case GameOptActvity.CMD_EXIT:
                            finish();
                            break;
                        case GameOptActvity.CMD_RESET:
                            game.reset();
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
            if(game.makeMove(clickPos)){
                clickSound.play();
            };
        }
    }



    @Override
    public void onBackPressed() {
        if(!game.undo())
            super.onBackPressed();
    }

    private void startTimer() { // Запуск таймера в текущем потоке
        //TODO перейти на java 8 и lambda
        mTimerFuture = mTimerExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                mUI_handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        game.onSecondTimer();
                    }
                }, 0);
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    private void stopTimer() {
        mTimerFuture.cancel(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("GameActivity", "Resume");
        game.enterPlayground();
        startTimer();
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
        stopTimer();
        game.exitPlayground();
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GameActivity", "Start");
        game.timerChanged.addObserver(onTimeChange);
        game.fieldChanged.addObserver(onFieldChange);
        mGameField.transitionEndEvent.addObserver(onFieldTransitionEnd);
        Music.setMusicOn(true);
//        mGameField.transitionEndEvent.addObserver(this.onFieldTransitionEnd);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("GameActivity", "Stop");
        game.fieldChanged.deleteObserver(onFieldChange);
        mGameField.transitionEndEvent.deleteObserver(onFieldTransitionEnd);
        Music.setMusicOn(false);
    }

/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_undo:
                game.undo();
                return true;
            case R.id.menu_reset:
                game.reset();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return false;
    }
*/

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ( keyCode == KeyEvent.KEYCODE_MENU ) {
            startActivityForResult(new Intent(GameActivity.this, GameOptActvity.class), GAME_OPT_ACTIVITY);
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

        mTimeLabel = (TextView) findViewById(R.id.timeLabel);
        mLevelLabel = (TextView) findViewById(R.id.levelLabel);

        Typeface font = Typeface.createFromAsset(getAssets(),"zebulon.ttf");

        mTimeLabel.setTypeface(font);
        mLevelLabel.setTypeface(font);

        if(game == null)
            game = new GamePlay();
        game.missionChanged.addObserver(onMissionChange);
        game.observedState.addObserver(onGameChange);

        if(getIntent().hasExtra("from")) {
            Log.i("GameActivity", "Extra from");
            int from_level;
            from_level = getIntent().getIntExtra("from", 0);
            getIntent().removeExtra("from");
            game.restartGame(from_level);
        } else
            updateMissionViews();

        mGameField.setMap(game.getGameMap());
        clickSound = new GameSound(this);
    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("GameActivity", "Destroy");
        game.observedState.deleteObserver(onGameChange);
        game.missionChanged.deleteObserver(onMissionChange);
        mGameField.destroy();
        mLevelField.destroy();
    }

}
