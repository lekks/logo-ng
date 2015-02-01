package com.ldir.logo.activities;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.ldir.logo.R;

public class SelectLevelActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_level);

        String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
                "Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
                "Linux", "OS/2" };
        LazyAdapter adapter = new LazyAdapter();
        final GridView g = (GridView) findViewById(R.id.levelsGrid);
        g.setAdapter(adapter);
    }

}

class LazyAdapter extends BaseAdapter {

    @Override
    public int getCount() {
        return 100;
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
        if(convertView==null)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.list_level, parent,false);
            TextView textView =  (TextView) convertView.findViewById(R.id.listLevelItemLabel);
            textView.setText("L="+position);
        }

//        TextView chapterName = (TextView)convertView.findViewById(R.id.textView1);
//        TextView chapterDesc = (TextView)convertView.findViewById(R.id.textView2);
//
//        codeLearnChapter chapter = codeLearnChapterList.get(arg0);
//
//        chapterName.setText(chapter.chapterName);
//        chapterDesc.setText(chapter.chapterDescription);

        return convertView;

//        return null;
    }
}