package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

public abstract class GamePlay {

    public enum GameEvent {
        TIMER_CHANGED,
        FIELD_CHANGED,
        LEVEL_CHANGED,
        LEVEL_COMPLETE,
        GAME_COMPLETE,
        GAME_LOST
    }

    public final Observed.Value<GameEvent> gameEvent = new Observed.Value<>();

    protected GameLevel gameLevel = new GameLevel();
    protected final GameMap gameMap = new GameMap();
    protected int levelTime;
    private long timeStart;

    protected final MapHistory history = new MapHistory();

//    public abstract void moveCompleted();


    public String getTimeString() {
        return String.format("%02d:%02d", levelTime / 60, levelTime % 60);
    }

    public void onSecondTimer() {
        if (levelTime > 0) {
            --levelTime;
            emitEvent(GameEvent.TIMER_CHANGED);
        } else {
            emitEvent(GameEvent.GAME_LOST);
        }
    }


    public final GameLevel getCurrentLevel() {
        return gameLevel;
    }


    public final GameMap getGameMap() {
        return gameMap;
    }

    public boolean undo() {
        GameMap last = history.pop();
        if (last != null) {
            gameMap.assign(last);
            emitEvent(GameEvent.FIELD_CHANGED);
            return true;
        } else
            return false;
    }

    public long timeElapsed(){
        return ( System.currentTimeMillis() - timeStart)/1000;
    }

    public void reset() {
        Log.i("Game reset","");
        levelTime = gameLevel.time + 1;
        timeStart = System.currentTimeMillis();
        gameMap.resetField();
        history.clear();
        emitEvent(GameEvent.FIELD_CHANGED);
    }

    public boolean makeMove(GameMap.Pos clickPos) {

        history.push(gameMap);
        if (gameMap.gameMove(clickPos.row, clickPos.col)) {
            emitEvent(GameEvent.FIELD_CHANGED);
            return true;
        } else {
            history.pop();
            return false;
        }
    }

    protected synchronized void emitEvent(GameEvent event) {
        if(event != GameEvent.TIMER_CHANGED)
            Log.i("Game vent", event.toString());
        gameEvent.update(event);
    }
}
