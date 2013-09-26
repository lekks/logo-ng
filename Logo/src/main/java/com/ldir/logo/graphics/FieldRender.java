package com.ldir.logo.graphics;

/*
    Вариант алгоритма отрисовки анимации:
    Рендер работает в отдельном потоке и рисует в битмап. По завершению обменивается картинками с GameField и вызывает
    ему ivalidate


 */



import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.FloatMath;

import com.ldir.logo.game.GameMap;

public class FieldRender {
	private Cell cells[][];
    private GameMap map;
    private Sprites sprites;
    private float hsize;
    private int cols, rows;

    private class Cell {
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
            canvas.drawBitmap(sprites.pic[0], x- hsize, y- hsize, paint);
            if(val>0) {
                canvas.drawBitmap(sprites.pic[val], x- hsize, y- hsize, paint);
//                RectF dst = new RectF(x-hsize, y-hsize,x+hsize , y+ hsize);
//                canvas.drawBitmap(sprites.pic[val], null,dst, paint);
            }
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


//    public void setMap(GameMap map) {
//        this.map = map;
//    }

	public boolean findCell(float cX, float cY, GameMap.Pos retPos) { // TODO Оптимизировать, убрать цикл
		for(int i=0;i<rows;i++){
			for(int j=0;j<cols;j++){
				if(cells[i][j].test(cX, cY)){
                    retPos.set(i,j);
					return true;
				}
			}
		}
        return false;
	}


    public FieldRender(float span, GameMap gameMap) {
        if(gameMap==null) {
            gameMap=new GameMap(); // Для дизаянера интерфейса
            gameMap.set(2,2,(byte)2);
        }
        this.map=gameMap;
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

