package com.ldir.logo.game;

import java.util.ArrayList;

/**
 * Created by Ldir on 06.02.2015.
 */
public class MapHistory {
    ArrayList<GameMap> stack = new ArrayList<GameMap>(8);
    int count=0;

    void clear(){
        count=0;
        stack.clear();
    }

    void push(GameMap map){
        count+=1;
        while(stack.size() < count) {
            stack.add(new GameMap());
        }
        stack.get(count-1).assign(map);
    }

    GameMap pop(){
        if(count == 0)
            return null;
        count-=1;
        return stack.get(count);
//        stack.remove(count);
    }


}
