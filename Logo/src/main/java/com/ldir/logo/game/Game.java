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

    public static void makeMove(GameMap.Pos clickPos){
        GameMap last = new GameMap();
        last.assign(Game.gameMap);
        Game.gameMap.gameMove(clickPos.row, clickPos.col);
        if(Game.gameMap.isEqual(Game.goalMap)) {
            Game.win = true;
        }
        history.add(last);
    }


    public static void startGame()
    {
        level = 0;
        MissionLoader.load(goalMap,level);
        reset();
    }

}
