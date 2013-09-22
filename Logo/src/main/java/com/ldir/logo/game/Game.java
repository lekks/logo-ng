package com.ldir.logo.game;

public class Game {
//	INSTANCE;

    public static GameMap goalMap;
    public static GameMap gameMap;


	static int level;
	public static boolean win;



    public static void startGame()
    {
        Game.gameMap = new GameMap();
        Game.goalMap = MissionLoader.load(1);
        win = false;
    }

}
