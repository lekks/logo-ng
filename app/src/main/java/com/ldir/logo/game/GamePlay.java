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

    protected final MapHistory history = new MapHistory();

    public abstract void moveCompleted();


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


    public GameLevel getCurrentLevel() {
        return gameLevel;
    }


    public GameMap getGameMap() {
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

    public void reset() {
        Log.i("Game reset","");
        levelTime = gameLevel.time + 1;
        gameMap.resetField();
        history.clear();
        emitEvent(GameEvent.FIELD_CHANGED);
    }

    public abstract void nextLevel();
    public abstract void restartGame(int from_level); //FIXME Сделать конструктором?


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
        Log.i("Game vent", event.toString());
        gameEvent.update(event);
    }
}
