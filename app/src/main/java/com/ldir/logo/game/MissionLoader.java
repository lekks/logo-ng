package com.ldir.logo.game;

public class MissionLoader {

    static int level_time[]={
            21,22,23
    };


    static byte levels[][][]={
            {
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,2,0,0,0},
				{0,0,2,1,2,0,0},
				{0,0,0,2,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
			},
            {
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,1,0,0,0},
				{0,0,1,1,1,0,0},
				{0,0,0,1,0,0,0},
				{0,0,0,0,0,0,0},
				{0,0,0,0,0,0,0},
			},
            {
				{0,0,0,0,0,0,0},
				{0,0,0,1,0,0,0},
				{0,0,1,1,1,0,0},
				{0,1,1,1,1,1,0},
				{0,0,1,1,1,0,0},
				{0,0,0,1,0,0,0},
				{0,0,0,0,0,0,0},
			},
//            {
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//				{0,0,0,0,0,0,0},
//			},
		};

    public static int levelNumber(){
        return levels.length-1;
    }

    public static boolean lastLevel(int n) {
        if (n+1 < levelNumber())
            return false;
        else
            return true;
    }

	
	public static boolean load(GameLevel level, int n)
	{
		if (n <levelNumber()) {
            for (int i = 0; i < levels[n].length; i++) {
                for (int j = 0; j < levels[n][i].length; j++) {
                    level.map.set(i,j,levels[n][i][j]);
                }
            }
            level.time = level_time[n];
            return true;
        } else
            return false;
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
