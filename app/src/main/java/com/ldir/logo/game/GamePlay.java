package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

// Тут состредоточим всю логику

// TODO попрятать всё что можно
public class GamePlay {

    public Observed.Event fieldChanged = new Observed.Event();
    public Observed.Event missionChanged = new Observed.Event();
    public Observed.Value<Integer> timerChanged = new Observed.Value<>();
    public Observed.Value<GameState> observedState = new Observed.Value<>();

    private GameLevel gameLevel = new GameLevel();
    private GameMap gameMap = new GameMap();
    private int levelTime;
    private int level;
    private MapHistory history = new MapHistory();


    public void moveCompleted() {
        switch (globalState) {
            case PLAYING: // TODO Сделать тоже самое при окончании таймера
                if (gameMap.isEqual(gameLevel.map)) {
                    Levels.setCompleted(getCurrenLevel());
                    if (lastLevel()) {
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
        GAME_OVER,
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
    };

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

    private boolean lastLevel() {
        if (level + 1 < Levels.levelsCount())
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

    public void gameOver() {
        changeState(GameState.GAME_OVER);
    }


}
