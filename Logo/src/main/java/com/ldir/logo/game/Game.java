package com.ldir.logo.game;

import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import java.util.Observable;
import java.util.Observer;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.ldir.logo.platform.GameWinActivity;
import com.ldir.logo.util.Observed;

// Тут состредоточим всю логику

// TODO попрятать всё что можно
public class Game {
    //	INSTANCE;


    public static GameMap goalMap = new GameMap();
    public static GameMap gameMap = new GameMap();
    public static Observed.Event fieldChanged = new  Observed.Event();
    public static Observed.Event missionChanged = new  Observed.Event();
    public static Observed.Value<Integer> timerChanged = new  Observed.Value<Integer>();

    private static int levelTime;
	private static int level;
    private static Stack<GameMap> history = new Stack<GameMap>();
    private static ScheduledExecutorService mTimerExecutor = Executors.newSingleThreadScheduledExecutor();
    private static ScheduledFuture mTimerFuture;

    public static boolean undo(){
        if(history.size() != 0 ){
            GameMap last = history.pop();
            Game.gameMap.assign(last);
            fieldChanged.update();
            return true;
        } else
            return false;
    }

    public static void reset(){
        levelTime = 21;
        Game.gameMap.resetField();
        history.clear();
        fieldChanged.update();
    }

    public static boolean skipLevel(){
        if(MissionLoader.load(goalMap,level+1)) {
            ++level;
            missionChanged.update();
            reset();
            return true;
        } else {
            return false;
        }
    }

    private static boolean lastLevel(){
        return MissionLoader.lastLevel(level);
    }


    public static boolean makeMove(GameMap.Pos clickPos) {
        GameMap last = new GameMap(); // TODO Сделать статическую переменную, потом коприровать по необходимости
        last.assign(Game.gameMap);
        if (gameMap.gameMove(clickPos.row, clickPos.col)) {
            history.add(last);
            fieldChanged.update();
            return true;
        } else
            return false;
    }


    public static void restartGame()
    {
        level = 0;
        MissionLoader.load(goalMap, level);
        missionChanged.update();
        reset();
    }

    public static enum GlobalState {
        UNDEFINED,
        PLAYING,
        PAUSE,
        LEVEL_COMPLETE,
        GAME_OVER,
        GAME_WIN,
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
            case GAME_WIN:
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
                    if (Game.gameMap.isEqual(Game.goalMap)) {
                        if(Game.lastLevel()) {
                            changeState(GlobalState.GAME_WIN);
                        } else {
                            changeState(GlobalState.LEVEL_COMPLETE);
                        }
                    }
                break;
            }
        }
    };


}
