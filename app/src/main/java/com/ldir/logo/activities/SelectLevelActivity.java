package com.ldir.logo.activities;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ldir.logo.R;
import com.ldir.logo.fieldviews.LevelField;
import com.ldir.logo.game.Levels;

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

        TextView textView =  (TextView) findViewById(R.id.selectLevel);
        textView.setTypeface(font);

    }

    class LevelsListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return Levels.length();
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
            if(convertView==null) {
                LayoutInflater inflater = LayoutInflater.from(parent.getContext());
                convertView = inflater.inflate(R.layout.list_level, parent,false);
            }
            TextView textView =  (TextView) convertView.findViewById(R.id.listLevelItemLabel);
            textView.setTypeface(font);
            textView.setText(getString(R.string.level)+Integer.toString(position+1));
            LevelField levelField =  (LevelField ) convertView.findViewById(R.id.levelListField);
            levelField.setLevel(position);
            return convertView;
        }
    }


}
