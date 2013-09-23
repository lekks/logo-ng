package com.ldir.logo.platform;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.ldir.logo.game.Game;
import com.ldir.logo.game.GameMap;
import com.ldir.logo.fieldviews.GameField;
import com.ldir.logo.R;

public class MainActivity extends Activity {

	private GameField gameField;

    private void processFieldChange(){
        gameField.drawField();

    }


    private class OnFieldPressed implements GameField.FieldPressHandler {
        @Override
        public void onPress(GameMap.Pos clickPos) {
            Log.i("Verbose","Field pressed"+clickPos.row+","+clickPos.col);
            Game.makeMove(clickPos);
            processFieldChange();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.menu_undo:
            if(Game.undo())
                processFieldChange();
            return true;
 
        case R.id.menu_reset:
//            Toast.makeText(this, "Save is Selected", Toast.LENGTH_SHORT).show();
            Game.reset();
            processFieldChange();
            return true;
         default:
            return super.onOptionsItemSelected(item);
        }
    }    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main_menu, menu);
        return true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	MissionLoader.load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameField = (GameField) findViewById(R.id.fieldView);
        gameField.setFieldPressHandler(new OnFieldPressed());
    }
}
