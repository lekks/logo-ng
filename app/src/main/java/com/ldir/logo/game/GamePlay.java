package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

// Тут состредоточим всю логику

public class GamePlay {

    public final Observed.Value<GameEvent> gameEvent = new Observed.Value<>();

    private GameLevel gameLevel = new GameLevel();
    private final GameMap gameMap = new GameMap();
    private int levelTime;
    private int level;
    private final MapHistory history = new MapHistory();


    public void moveCompleted() {
        if (gameMap.isEqual(gameLevel.map)) {
            boolean uncompleted = !Levels.isCompleted(getCurrenLevel());
            Levels.setCompleted(getCurrenLevel());
            if (Levels.isAllCompleted() && uncompleted) {
                emitEvent(GameEvent.GAME_COMPLETE);
            } else {
                emitEvent(GameEvent.LEVEL_COMPLETE);
            }
        }
    }

    public enum GameEvent {
        TIMER_CHANGED,
        FIELD_CHANGED,
        LEVEL_CHANGED,
        LEVEL_COMPLETE,
        GAME_COMPLETE,
        GAME_LOST
    }

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

    public void nextLevel() {
        level = Levels.nextOpened(level);
        gameLevel = Levels.getLevel(level);
        emitEvent(GameEvent.LEVEL_CHANGED);
        reset();
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

    public void restartGame(int from_level) {
        Log.i("Game restart","From "+level);
        level = from_level;
        gameLevel = Levels.getLevel(level);
        emitEvent(GameEvent.LEVEL_CHANGED);
        reset();
    }

    private synchronized void emitEvent(GameEvent event) {
        Log.i("Game vent", event.toString());
        gameEvent.update(event);
    }
}
