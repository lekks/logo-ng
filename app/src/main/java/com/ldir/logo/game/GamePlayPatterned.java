package com.ldir.logo.game;

import android.util.Log;

import com.ldir.logo.util.Observed;

public class GamePlayPatterned extends GamePlay{

    private int level;

    public int getLevelId(){
        return level;
    }

    public void moveCompleted() {
        if (gameMap.isEqual(gameLevel.map)) {
            boolean uncompleted = !Levels.isCompleted(level);
            Levels.setCompleted(level);
            if (Levels.isAllCompleted() && uncompleted) {
                emitEvent(GameEvent.GAME_COMPLETE);
            } else {
                emitEvent(GameEvent.LEVEL_COMPLETE);
            }
        }
    }

    public void nextLevel() {
        level = Levels.nextOpened(level);
        gameLevel = Levels.getLevel(level);
        emitEvent(GameEvent.LEVEL_CHANGED);
        reset();
    }

    public void restartGame(int from_level) {
        Log.i("Game restart","From "+level);
        level = from_level;
        gameLevel = Levels.getLevel(level);
        emitEvent(GameEvent.LEVEL_CHANGED);
        reset();
    }

}
