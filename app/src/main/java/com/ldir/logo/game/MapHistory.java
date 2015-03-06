package com.ldir.logo.game;

import java.util.ArrayList;

/**
 * Created by Ldir on 06.02.2015.
 */
class MapHistory {
    private final ArrayList<GameMap> stack = new ArrayList<>(8);
    private int mCount =0;

    void clear(){
        mCount =0;
        stack.clear();
    }

    void push(GameMap map){
        mCount +=1;
        while(stack.size() < mCount) {
            stack.add(new GameMap());
        }
        stack.get(mCount -1).assign(map);
    }

    GameMap pop(){
        if(mCount == 0)
            return null;
        mCount -=1;
        return stack.get(mCount);
//        stack.remove(mCount);
    }


}
