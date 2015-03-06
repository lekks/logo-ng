package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

// Тут состредоточим всю логику

public class GamePlay {

    public final Observed.Event fieldChanged = new Observed.Event();
    public final Observed.Event missionChanged = new Observed.Event();
    public final Observed.Value<Integer> timerChanged = new Observed.Value<>();
    public final Observed.Value<GameState> observedState = new Observed.Value<>();

    private GameLevel gameLevel = new GameLevel();
    private final GameMap gameMap = new GameMap();
    private int levelTime;
    private int level;
    private final MapHistory history = new MapHistory();


    public void moveCompleted() {
        switch (globalState) {
            case PLAYING:
                if (gameMap.isEqual(gameLevel.map)) {
                    boolean uncompleted = !Levels.isCompleted(getCurrenLevel());
                    Levels.setCompleted(getCurrenLevel());
                    if (Levels.isAllCompleted() && uncompleted) {
                        changeState(GameState.GAME_COMPLETE);
                    } else {
                        changeState(GameState.LEVEL_COMPLETE);
                    }
                }
                break;
        }
    }

    public enum GameState {
        UNDEFINED,
        PLAYING,
        PAUSE,
        LEVEL_COMPLETE,
        GAME_COMPLETE,
        GAME_LOST,
    }

    public void onSecondTimer() {
        if (levelTime > 0) {
            --levelTime;
            timerChanged.update(levelTime);
        } else {
            timerChanged.update(levelTime);
            changeState(GameState.GAME_LOST);
        }
    }

    private GameState globalState = GameState.UNDEFINED;

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
        Log.i("Game reset","");
        levelTime = gameLevel.time + 1;
        gameMap.resetField();
        history.clear();
        fieldChanged.update();
    }

    public void nextLevel() {
        level = Levels.nextOpened(level);
        gameLevel = Levels.getLevel(level);
        missionChanged.update();
        reset();
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

    public void restartGame(int from_level) {
        Log.i("Game restart","From "+level);
        level = from_level;
        gameLevel = Levels.getLevel(level);
        missionChanged.update();
        reset();
    }

    private synchronized void changeState(GameState newState) {
        if (!globalState.equals(newState)) {
            Log.i("State changed", "From " + globalState + " to " + newState);
            globalState = newState;
            observedState.update(newState);
        }
    }

    public void enterPlayground() {
        changeState(GameState.PLAYING);
        moveCompleted();
    }

    public void exitPlayground() {
        switch (globalState) {
            case GAME_COMPLETE:
            case LEVEL_COMPLETE:
                break;
            default:
                changeState(GameState.PAUSE);
        }
    }

}
