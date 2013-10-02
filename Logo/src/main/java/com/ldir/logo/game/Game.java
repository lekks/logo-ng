package com.ldir.logo.game;

import java.util.Stack;

public class Game {
//	INSTANCE;

    public static GameMap goalMap = new GameMap();
    public static GameMap gameMap = new GameMap();
	static int level;
	public static boolean win;
    private static Stack<GameMap> history = new Stack<GameMap>(); // TODO Переделать на Vector;

    public static boolean undo(){
        if(history.size() != 0 ){
            GameMap last = history.pop();
            Game.gameMap.assign(last);
            return true;
        } else
            return false;
    }

    public static void reset(){
        Game.gameMap.reset();
        history.clear();
        Game.win = false;
    }

    public static boolean nextlevel(){
        if(MissionLoader.load(goalMap,level+1)) {
            ++level;
            reset();
            return true;
        } else
            return false;
    }

    public static boolean lastLevel(){
        return MissionLoader.lastLevel(level);
    }


    public static boolean makeMove(GameMap.Pos clickPos) {
        GameMap last = new GameMap();
        last.assign(Game.gameMap);
        boolean move = gameMap.gameMove(clickPos.row, clickPos.col);
        if (move) {
            if (Game.gameMap.isEqual(Game.goalMap)) {
                Game.win = true;
            }
            history.add(last);
        }
        return move;
    }


    public static void startGame()
    {
        level = 0;
        MissionLoader.load(goalMap,level);
        reset();
    }

}
