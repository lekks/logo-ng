package com.ldir.logo;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.FloatMath;

public class GameMatrix {
	private Cell cells[][];
	int cols, rows;
	public GameMatrix(int cols, int rows, float span) {
		this.rows=rows;
		this.cols=cols;
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
		for (Cell[] row : cells) {
			for (Cell cell: row) {
				cell.draw(canvas, paint);
			}
		}
	}
	public void reset() {
		for (Cell[] row : cells) {
			for (Cell cell: row) {
				cell.reset();
			}
		}
	}

	public boolean isEqual(GameMatrix other) {
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(cells[i][j].val != other.cells[i][j].val)
					return false;
			}
		}
		return true;
	}
	
	public void load(int[][] copy) {
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				cells[i][j].val = copy[i][j];
			}
		}
	}
	public int[][] save(int[][] copy) {
		if(copy == null) 
			copy = new int[rows][cols];
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				copy[i][j] = cells[i][j].val ;
			}
		}
		return copy;
	}
	
	public boolean click(float cX,float cY) {
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(cells[i][j].test(cX, cY)){
					cells[i][j].next();
					if (i+1<rows) cells[i+1][j].next();
					if (j+1<cols) cells[i][j+1].next();
					if (i>0) cells[i-1][j].next();
					if (j>0) cells[i][j-1].next();
					return true;
				}
			}
		}
		return false;
	}
}

class Cell {
	Rect rect=new Rect();
	public int val;
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

	public String  toString() {
		return String.format("%d", val);
	}
	
	public void reset() {
		val=0;
	}
	public void next(){
		if(++val>4)val=1;
	}
	
	void draw(Canvas canvas, Paint paint){
//		canvas.drawRect(rect, paint);
		canvas.drawBitmap(sprites.pic[val], x-size/2, y-size/2, paint);
//		canvas.drawText(String.format("%s",toString()), x, y, paint);
	}

}
