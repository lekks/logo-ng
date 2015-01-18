package com.ldir.logo.music;

import com.ldir.logo.game.Game;
import com.ldir.logo.platform.GameApp;

import java.io.IOException;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by Ldir on 30.12.2014.
 */
public class Music {
    private static IBXMPlayer music;

    private static boolean musicEnabled;

    public static Observer onGameChange = new Observer(){
        @Override
        public void update(Observable observable, Object arg) {
            Game.StateChange state = (Game.StateChange)arg;
//            switch (state.oldState) {
//                case PLAYING:
//                case GAME_OPTIONS:
//                    break;
//            }

            switch (state.newState) {
                case PLAYING:
                case GAME_OPTIONS:
                    startMusic();
                    break;
                default:
                    stopMusic();
            }
        }
    };

    public static void startMusic() {
        if (music == null) {
            try {
                music = new IBXMPlayer(GameApp.getAppContext().getAssets().open("mus/menu/1.xm"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void stopMusic() {
        if (music != null) {
            try {
                music.close();
                music = null;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
