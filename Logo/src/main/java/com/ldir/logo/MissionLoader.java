package com.ldir.logo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.util.Log;

public class MissionLoader {

	static int levels[][][]={
			{
				{0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,1,0,0,0,0,0},
				{0,0,0,1,2,1,0,0,0,0},
				{0,0,0,1,2,1,0,0,0,0},
				{0,0,0,0,1,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0},
			}, {
				{0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,2,0,0,0,0,0},
				{0,0,0,1,3,1,0,0,0,0},
				{0,0,0,1,4,1,0,0,0,0},
				{0,0,0,0,1,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0,0,0,0},
			},
		};
	
	
	static int [][] get(int i)
	{
		return levels[i];
	}
	
//	static void load(){
//		 InputStream is = GameApp.getAppContext().getResources().openRawResource(R.raw.levels);
//		    BufferedReader br = new BufferedReader(new InputStreamReader(is));
//		    String readLine = null;
//
//		    try {
//		        // While the BufferedReader readLine is not null 
//		        while ((readLine = br.readLine()) != null) {
//		        	if(readLine.startsWith(":"))
//		        		Log.d("TEXT", "New");
//		        	else
//		        		for(int i=0;i<readLine.length();i++)
//		        			Log.d("TEXT", String.format("Z=%d",Character.digit(readLine.charAt(i),10)));
//		        	}
//
//		    // Close the InputStream and BufferedReader
//		    is.close();
//		    br.close();
//
//		    } catch (IOException e) {
//		        e.printStackTrace();
//		    }
//	}
	
	
}
