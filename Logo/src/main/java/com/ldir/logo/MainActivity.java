package com.ldir.logo;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends Activity {

	private GameField gameField;
	
	public void clearClicked(View v) {
		gameField.reset();
    }
	public void undoClicked(View v) {
        Toast.makeText(this, "Undo is Selected", Toast.LENGTH_SHORT).show();
		gameField.undo();
    }
	
	public void undoClicked(MenuItem item) {
        Toast.makeText(this, "Undo is Selected", Toast.LENGTH_SHORT).show();
		gameField.undo();
    }
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
//    	MissionLoader.load();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gameField = (GameField) findViewById(R.id.fieldView);
    }
    
    
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
         
        switch (item.getItemId())
        {
        case R.id.menu_undo:
    		gameField.undo();
            return true;
 
        case R.id.menu_reset:
//            Toast.makeText(this, "Save is Selected", Toast.LENGTH_SHORT).show();
    		gameField.reset();
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
}
