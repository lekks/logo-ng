package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Тут состредоточим всю логику

// TODO попрятать всё что можно
public class Game {

    public Observed.Event fieldChanged = new Observed.Event();
    public Observed.Event missionChanged = new Observed.Event();
    public Observed.Value<Integer> timerChanged = new Observed.Value<Integer>();
    public Observed.Value<StateChange> observedState = new Observed.Value<StateChange>();

    private GameLevel gameLevel;
    private GameMap gameMap = new GameMap();
    private int levelTime;
    private Runnable onTimerTick = new Runnable() {
        @Override
        public void run() {
//            Log.d("Timer","Tick");
            if (levelTime > 0) {
                --levelTime;
                timerChanged.update(levelTime);
            } else {
                timerChanged.update(levelTime);
                changeState(GlobalState.GAME_LOST);
            }
        }
    };
    private int level;
    private MapHistory history = new MapHistory();
    private ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();

    public enum GlobalState {
        UNDEFINED,
        PLAYING,
        PAUSE,
        LEVEL_COMPLETE,
        GAME_OVER,
        GAME_COMPLETE,
        GAME_LOST,
    }

    public class StateChange {
        public GlobalState oldState = GlobalState.UNDEFINED;
        public GlobalState newState = GlobalState.UNDEFINED;

        void set(GlobalState from, GlobalState to) {
            oldState = from;
            newState = to;
        }
    }

    private ScheduledFuture mTimerFuture;
    private GlobalState globalState = GlobalState.UNDEFINED;
    public Observer onFieldTransitionEnd = new Observer() {
        @Override
        public void update(Observable observable, Object arg) {
            switch (globalState) {
                case PLAYING: // TODO Сделать тоже самое при окончании таймера
                    if (gameMap.isEqual(gameLevel.map)) {
                        if (lastLevel()) {
                            changeState(GlobalState.GAME_COMPLETE);
                        } else {
                            changeState(GlobalState.LEVEL_COMPLETE);
                        }
                    }
                    break;
            }
        }
    };
    private StateChange mStateChange = new StateChange();

    public int getCurrenLevel() {
        return level;
    }

    public GameMap getGameMap() {
        return gameMap;
    }

    public boolean undo() {
        GameMap last = history.pop();
        if (last != null) {
            gameMap.assign(last);
            fieldChanged.update();
            return true;
        } else
            return false;
    }

    public void reset() {
        levelTime = gameLevel.time + 1;
        gameMap.resetField();
        history.clear();
        fieldChanged.update();
    }

    public boolean skipLevel() {

        if (!lastLevel()) {
            gameLevel = MissionLoader.get(++level);
            missionChanged.update();
            reset();
            return true;
        } else {
            return false;
        }
    }

    private boolean lastLevel() {
        if (level + 1 < MissionLoader.length())
            return false;
        else
            return true;
    }

    public boolean makeMove(GameMap.Pos clickPos) {

        history.push(gameMap);
        if (gameMap.gameMove(clickPos.row, clickPos.col)) {
            fieldChanged.update();
            return true;
        } else {
            history.pop();
            return false;
        }
    }

    public void restartGame() {
        level = 0;
        gameLevel = MissionLoader.get(level);
        missionChanged.update();
        reset();
    }

//    private  void onChangeState(StateChange state)
//    {
//
//    }

    private synchronized void changeState(GlobalState newState) {
        if (!globalState.equals(newState)) {
            mStateChange.set(globalState, newState);
            Log.i("State changed", "From " + globalState + " to " + newState);
            globalState = newState;
//            onChangeState(mStateChange);
            observedState.update(mStateChange);
        }
    }

    public void enterPlayground() {
        changeState(GlobalState.PLAYING);
        mTimerFuture = mTimerExecutor.scheduleAtFixedRate(onTimerTick, 1, 1, TimeUnit.SECONDS);
    }

    public void exitPlayground() {
        mTimerFuture.cancel(false);
        switch (globalState) {
            case GAME_COMPLETE:
            case LEVEL_COMPLETE:
                break;
            default:
                changeState(GlobalState.PAUSE);
        }
    }

    public void gameOver() {
        changeState(GlobalState.GAME_OVER);
    }

    public void exitOptScreen() {
        if (globalState != GlobalState.PLAYING)
            changeState(GlobalState.PAUSE);
    }




}
