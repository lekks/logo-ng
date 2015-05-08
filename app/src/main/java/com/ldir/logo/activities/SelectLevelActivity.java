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
import android.widget.ImageView;
import android.widget.TextView;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.GameProgress;
import com.ldir.logo.game.Levels;
import com.ldir.logo.sound.Music;

public class SelectLevelActivity extends Activity {
    private Typeface font;
    GridView grid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);
        font = Typeface.createFromAsset(getAssets(),"zebulon.ttf");
        LevelsListAdapter adapter = new LevelsListAdapter();
        grid  = (GridView) findViewById(R.id.levelsGrid);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new LevelStarter());
    }

    private class LevelStarter implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            int level = position;
            if(level == 0 || GameProgress.isCompleted(level) || GameProgress.isOpened(level) ) {
                Intent intent = new Intent(SelectLevelActivity.this, GameActivity.class);
                intent.putExtra("level", level);
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
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(SelectLevelActivity.this);
                convertView = inflater.inflate(R.layout.list_level, parent, false);
            }
            TextView textView = (TextView) convertView.findViewById(R.id.listLevelItemLabel);
            textView.setTypeface(font);
            textView.setText(Integer.toString(level + 1));

            ImageView star = (ImageView) convertView.findViewById(R.id.star);
            LevelField levelField = (LevelField) convertView.findViewById(R.id.levelListField);


            if (GameProgress.isCompleted(level)) {
                star.setVisibility(View.VISIBLE);
                levelField.setLevel(Levels.getLevel(level));
            }else if(GameProgress.isOpened(level)) {
                levelField.setLevel(Levels.getLevel(level));
                star.setVisibility(View.INVISIBLE);
            } else {
                star.setVisibility(View.INVISIBLE);
                levelField.setLevel(null);
            }

            return convertView;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        grid.setSelection(GameProgress.restoreCurrentLevel());
        Music.setMusicOn(Music.MENU_SEL_MUS, true);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Music.setMusicOn(Music.MENU_SEL_MUS,false);
    }

}
