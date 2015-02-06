package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

// Тут состредоточим всю логику

// TODO попрятать всё что можно
public class Game {
    //	INSTANCE;


    public static Observed.Event fieldChanged = new  Observed.Event();
    public static Observed.Event missionChanged = new  Observed.Event();
    public static Observed.Value<Integer> timerChanged = new  Observed.Value<Integer>();

    private static GameLevel gameLevel;
    private static GameMap gameMap = new GameMap();
    private static int levelTime;
	private static int level;
    private static MapHistory history = new MapHistory();
    private static ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture mTimerFuture;

    public static int getCurrenLevel() {
        return level;
    }


//   public static GameMap getGoalMap() {
//        return gameLevel.map;
//    }

    public static GameMap getGameMap() {
        return gameMap;
    }

    public static boolean undo(){
        GameMap last = history.pop();
        if(last != null ){
            Game.gameMap.assign(last);
            fieldChanged.update();
            return true;
        } else
            return false;
    }

    public static void reset(){
        levelTime = gameLevel.time+1;
        Game.gameMap.resetField();
        history.clear();
        fieldChanged.update();
    }

    public static boolean skipLevel(){

        if(!lastLevel()) {
            gameLevel = MissionLoader.get(++level);
            missionChanged.update();
            reset();
            return true;
        } else {
            return false;
        }
    }

    private static boolean lastLevel(){
        if (level+1 < MissionLoader.length())
            return false;
        else
            return true;
    }

    public static boolean makeMove(GameMap.Pos clickPos) {

        history.push(Game.gameMap);
        if (gameMap.gameMove(clickPos.row, clickPos.col)) {
            fieldChanged.update();
            return true;
        } else {
            history.pop();
            return false;
        }
    }


    public static void restartGame()
    {
        level = 0;
        gameLevel = MissionLoader.get(level);
        missionChanged.update();
        reset();
    }

    public static enum GlobalState {
        UNDEFINED,
        PLAYING,
        PAUSE,
        LEVEL_COMPLETE,
        GAME_OVER,
        GAME_COMPLETE,
        GAME_LOST,
    }

    public static class StateChange {
        public GlobalState oldState = GlobalState.UNDEFINED;
        public GlobalState newState = GlobalState.UNDEFINED;
        void set(GlobalState from,GlobalState to){
            oldState = from;
            newState = to;
        };
    }

    private static Runnable onTimerTick = new Runnable() {
        @Override
        public void run() {
//            Log.d("Timer","Tick");
            if(levelTime>0) {
                --levelTime;
                timerChanged.update(levelTime);
            } else {
                timerChanged.update(levelTime);
                changeState(GlobalState.GAME_LOST);
            }
        }
    };

    private static GlobalState globalState = GlobalState.UNDEFINED;

    public static Observed.Value<StateChange> observedState = new  Observed.Value<StateChange>();

//    private static void onChangeState(StateChange state)
//    {
//
//    }

    private static StateChange mStateChange=new StateChange();
    private static synchronized void changeState(GlobalState newState) {
        if(!globalState.equals(newState)){
            mStateChange.set(globalState,newState);
            Log.i("State changed", "From " + globalState + " to " + newState);
            globalState = newState;
//            onChangeState(mStateChange);
            observedState.update(mStateChange);
        }
    }

    public static void enterPlayground() {
        changeState(GlobalState.PLAYING);
        mTimerFuture = mTimerExecutor.scheduleAtFixedRate(onTimerTick,1, 1, TimeUnit.SECONDS);
    }
    public static void exitPlayground()  {
        mTimerFuture.cancel(false);
        switch (globalState) {
            case GAME_COMPLETE:
            case LEVEL_COMPLETE:
                break;
            default:
                changeState(GlobalState.PAUSE);
        }
    }

    public static void gameOver(){
        changeState(GlobalState.GAME_OVER);
    }
    public static void exitOptScreen()  {
        if (globalState != GlobalState.PLAYING)
            changeState(GlobalState.PAUSE);
    }

    public static Observer onFieldTransitionEnd = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            switch (globalState){
                case PLAYING: // TODO Сделать тоже самое при окончании таймера
                    if (Game.gameMap.isEqual(Game.gameLevel.map)) {
                        if(Game.lastLevel()) {
                            changeState(GlobalState.GAME_COMPLETE);
                        } else {
                            changeState(GlobalState.LEVEL_COMPLETE);
                        }
                    }
                break;
            }
        }
    };


}
