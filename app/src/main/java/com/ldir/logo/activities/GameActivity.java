package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.ldir.logo.GameApp;
import com.ldir.logo.R;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.GamePlay;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.game.GamePlayPatterned;
import com.ldir.logo.game.GameProgress;
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

    private static GamePlayPatterned game;
    private ScheduledFuture mTimerFuture;
    private ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();

    private GameField mGameField;
    private LevelField mLevelField;
    private TextView mTimeLabel;
    private TextView mLevelLabel;
    private final Handler mUI_handler = new Handler();


    private final Observer onGameEvent = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            GamePlay.GameEvent event = (GamePlay.GameEvent) arg;

            switch (event) {
                case GAME_LOST:
                    startActivityForResult(new Intent(GameActivity.this, TimeoutActivity.class), GAME_LOST_ACTIVITY);
                    break;
                case GAME_COMPLETE:
                    startActivityForResult(new Intent(GameActivity.this, GameWinActivity.class), GAME_WIN_ACTIVITY);
                    break;
                case LEVEL_COMPLETE:
                    startActivityForResult(new Intent(GameActivity.this, NextLevelActivity.class), NEXT_LEVEL_ACTIVITY);
                    break;

                case FIELD_CHANGED:
                    updateField();
                    break;
                case TIMER_CHANGED:
                    mTimeLabel.setText(getString(R.string.timeLabelText) + game.getTimeString());
                    break;
                case LEVEL_CHANGED:
                    updateMission();
                    break;
            }
        }
    };

    void updateMission() {
        GameProgress.saveCurrentLevel(game.getLevelId());
        mLevelField.setLevel(game.getCurrentLevel());
        mLevelField.invalidate();

        if(game.getCurrentLevel().tag != null) {
            mLevelLabel.setText("Level "+Integer.toString(game.getLevelId()+1)+"("+game.getCurrentLevel().tag+")");
        } else
            mLevelLabel.setText("Level "+Integer.toString(game.getLevelId()+1));

        String[] songs = GameApp.getAppResources().getStringArray(R.array.game_mus);
        int musNdx = game.getLevelId() % songs.length;
        Music.setFile(songs[musNdx]);
    }

    private void updateField() {
        mGameField.drawField();
    }


    private final Observer onFieldTransitionEnd = new Observer() {
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
                game.restartGame(0);
                finish();
                break;
            case GAME_LOST_ACTIVITY:
                game.reset();
                finish();
                break;
            case GAME_OPT_ACTIVITY:
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
                GameSound.play();
            }
        }
    }

    @Override
    public void onBackPressed() {
        if(!game.undo())
            super.onBackPressed();
    }

    private void startTimer() { // Запуск таймера в текущем потоке
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
    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.i("GameActivity", "Start");
        mGameField.transitionEndEvent.addObserver(onFieldTransitionEnd);
        Music.setMusicOn(Music.GAME_MUS,true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("GameActivity", "Stop");
        mGameField.transitionEndEvent.deleteObserver(onFieldTransitionEnd);
        Music.setMusicOn(Music.GAME_MUS,false);
    }

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
            game = new GamePlayPatterned();
        game.gameEvent.addObserver(onGameEvent);
        game.restartGame(0);

        if(getIntent().hasExtra("level")) {
            Log.i("GameActivity", "Create from level ");
            int from_level;
            from_level = getIntent().getIntExtra("level", 0);
            getIntent().removeExtra("level"); // same request going again after flipping the screen or the activity being brought back to the foreground.
            game.restartGame(from_level);
        } else if(getIntent().hasExtra("generated")) {
            getIntent().removeExtra("generated"); // same request going again after flipping the screen or the activity being brought back to the foreground.
        } else
            updateMission();

        mGameField.setMap(game.getGameMap());
        GameSound.load();

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        Log.i("GameActivity", "Destroy");
        game.gameEvent.deleteObserver(onGameEvent);
        mGameField.setFieldPressHandler(null);

        mGameField.destroy();
        mLevelField.destroy();
//        GameSound.release();

    }

}
