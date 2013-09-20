package com.ldir.logo.game;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatMath;

import com.ldir.logo.Sprites;

public class GameMatrix {
	private Cell cells[][];
    private GameMap map;

	int cols, rows;
	public GameMatrix(int cols, int rows, float span) {
		this.rows=rows;
		this.cols=cols;
        map = new GameMap();
		Sprites sprites = new Sprites((int) FloatMath.floor(span));
		cells = new Cell[rows][];
		for(int i=0;i<rows;i++){
			cells[i] = new Cell[cols];
			for(int j=0;j<cols;j++){
				cells[i][j]= new Cell(i,j,span, sprites);
			}
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
	
	public void load(GameMap map) {
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				this.map.set(i,j, map.get(i,j)); // FIXME Убрать цикл!!!
			}
		}
	}
	public GameMap save(GameMap copy) {
		if(copy == null)
			copy = new GameMap(rows,cols);
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				copy.set(i,j,map.get(i,j)); // FIXME Убрать цикл!!!
			}
		}
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
}

class Cell {
	Rect rect=new Rect();
	float x;
	float y;
	float size;
	Sprites sprites;
	
	public Cell(int row, int col, float span, Sprites sprites) {
		this.size=span;
		float s=span*0.42f;
		this.x=(col+0.5f)*span;
		this.y=(row+0.5f)*span;
		this.sprites = sprites;
		rect.set((int)(x-s), (int)(y-s), (int)(x+s), (int)(y+s));
	}
	
	public boolean test(float cX, float cY){
		float hs=size/2;
		return cX > (x-hs) && cX < (x+hs) && cY > (y-hs) && cY < (y+hs);
	}

	public String  toString(byte val) {
		return String.format("%d", val);
	}
	

	void draw(Canvas canvas, Paint paint, byte val){
//		canvas.drawRect(rect, paint);
		canvas.drawBitmap(sprites.pic[val], x-size/2, y-size/2, paint);
//		canvas.drawText(String.format("%s",toString()), x, y, paint);
	}

}
