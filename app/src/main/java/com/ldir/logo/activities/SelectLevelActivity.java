package com.ldir.logo.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.Levels;
import com.ldir.logo.sound.Music;

public class SelectLevelActivity extends Activity {
    private Typeface font;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);
        font = Typeface.createFromAsset(getAssets(),"zebulon.ttf");
        LevelsListAdapter adapter = new LevelsListAdapter();
        final GridView grid = (GridView) findViewById(R.id.levelsGrid);
        grid.setAdapter(adapter);

        grid.setOnItemClickListener(new LevelStarter());
        TextView textView =  (TextView) findViewById(R.id.selectLevel);
        textView.setTypeface(font);

    }

    private class LevelStarter implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int level = position;
//            Toast.makeText(SelectLevelActivity.this, "" + level, Toast.LENGTH_SHORT).show();
            if(level == 0 || Levels.isCompleted(level) || Levels.isOpened(level) ) {
                Intent intent = new Intent(SelectLevelActivity.this, GameActivity.class);
                intent.putExtra("from", level);
                startActivity(intent);
            }
        }
    }


    private class LevelsListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Levels.levelsCount();
        }
        @Override
        public Object getItem(int position) {
            return null;
        }
        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            int level = position;
            if(convertView==null) {
                LayoutInflater inflater = LayoutInflater.from(SelectLevelActivity.this);
                convertView = inflater.inflate(R.layout.list_level, parent,false);
            }
            TextView textView =  (TextView) convertView.findViewById(R.id.listLevelItemLabel);
            textView.setTypeface(font);
            textView.setText(Integer.toString(level+1));

            textView =  (TextView) convertView.findViewById(R.id.listLevelStatus);

            if(Levels.isCompleted(level))
                textView.setText("Complete");
            else if(Levels.isOpened(level))
                textView.setText("Openened");
            else
                textView.setText("Closed");

            LevelField levelField =  (LevelField ) convertView.findViewById(R.id.levelListField);
            levelField.setLevel(level);
            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Music.setMusicOn(Music.MENU_SEL_MUS, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.setMusicOn(Music.MENU_SEL_MUS,false);
    }

}
