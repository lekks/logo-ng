package com.ldir.logo.game;

import android.util.Log;


public class GamePlayGenerated extends GamePlay{

    public void moveCompleted() {
        if (gameMap.isEqual(gameLevel.map)) {
            emitEvent(GameEvent.LEVEL_COMPLETE);
        }
    }

    public void nextLevel() {
        gameLevel = LevelMaker.makeLevel();
        emitEvent(GameEvent.LEVEL_CHANGED);
        reset();
    }

    public void restartGame(int from_level) {
        Log.i("Game restart","Randeom ");
        nextLevel();
//        emitEvent(GameEvent.LEVEL_CHANGED);
//        reset();
    }

}
