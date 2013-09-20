package com.ldir.logo.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.FloatMath;

import com.ldir.logo.Sprites;

public class GameMatrix {
	private Cell cells[][];
    private GameMap map;
    private Sprites sprites;
    private float hsize;
    private int cols, rows;

    class Cell {
        //	Rect rect=new Rect();
        private float x;
        private float y;

        Cell(int row, int col, float size) {
            this.x=(col+0.5f)*size;
            this.y=(row+0.5f)*size;
            //float s=span*0.42f;
            //rect.set((int)(x-s), (int)(y-s), (int)(x+s), (int)(y+s));
        }

        boolean test(float cX, float cY){
            return cX > (x- hsize) && cX < (x+ hsize) && cY > (y- hsize) && cY < (y+ hsize);
        }

        void draw(Canvas canvas, Paint paint, byte val){
            //canvas.drawRect(rect, paint);
            canvas.drawBitmap(sprites.pic[val], x- hsize, y- hsize, paint);
            //canvas.drawText(String.format("%i",val), x, y, paint);
        }
    }

	public void printNumbers(Canvas canvas, Paint paint) {
        for(int i=0;i<rows;i++){
            for(int j=0;j<cols;j++){
                cells[i][j].draw(canvas, paint,map.get(i,j));
            }
        }
	}

	public void reset() {
        map.reset();
	}

	public boolean isEqual(GameMatrix other) {
		return map.isEqual(other.map);
	}

    public void setMap(GameMap map) {
        this.map = map;
    }
    public GameMap  getMap() {
        return map;
    }

	public void load(GameMap map) {
        this.map.assign(map);
	}

	public GameMap save(GameMap copy) {
		if(copy == null)
            copy = new GameMap();
        copy.assign(this.map);
		return copy;
	}


	public boolean click(float cX,float cY) {
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(cells[i][j].test(cX, cY)){
                    map.gameMove(i,j);
					return true;
				}
			}
		}
		return false;
	}


    public GameMatrix(float span, GameMap gameMap) {
        if(gameMap != null)
            this.map=gameMap;
        else
            this.map = new GameMap();
        this.rows=gameMap.ROWS;
        this.cols=gameMap.COLS;
        hsize = span/2;

        sprites = new Sprites((int) FloatMath.floor(span));
        cells = new Cell[rows][];
        for(int i=0;i<rows;i++){
            cells[i] = new Cell[cols];
            for(int j=0;j<cols;j++){
                cells[i][j]= new Cell(i,j,span);
            }
        }
    }


}

