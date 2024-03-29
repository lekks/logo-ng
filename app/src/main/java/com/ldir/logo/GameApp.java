package com.ldir.logo;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

public class GameApp  extends Application{

    private static final String SHARED_SETTINGS = "com.ldir.logo";
    public static final String SHARED_SETTINGS_MUS_ENABLED = "MUS_ENABLED";
    public static final String SHARED_SETTINGS_SND_ENABLED = "SND_ENABLED";
    public static final String SHARED_SETTINGS_LEVELS_PROGRESS = "LEVELS_PROGRESS";
    public static final String SHARED_SETTINGS_CURRENT_LEVEL = "CURRENT_LEVEL";

    private static Context context;
    private static Resources resources;
//    private static Application app;
    private static final String log_dir = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "logo_ng";


    @Override
    public void onCreate(){
        super.onCreate();
        Log.i("GameApp", "onCreate");
        GameApp.context = getApplicationContext();
        GameApp.resources = getResources();
    }

    public static Context getAppContext() {
        return GameApp.context;
    }

    public static Resources getAppResources() {
        return GameApp.resources;
    }
    public static InputStream getAsset(String path) throws IOException {
        return GameApp.context.getAssets().open(path);
    }
    public static SharedPreferences getGamePreferences(){
        return GameApp.context.getSharedPreferences(SHARED_SETTINGS, MODE_PRIVATE);

    }

    public static Bundle getMetaBundle(){
        try {
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return ai.metaData;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("GetMeta", "Failed to load meta-data, NameNotFound: " + e.getMessage());
        } catch (NullPointerException e) {
            Log.e("GetMeta", "Failed to load meta-data, NullPointer: " + e.getMessage());
        }
        return null;
    }


    public static String Log(String str) throws IOException {
        final String log_name = /*File.separator + */"logo_log.txt";

        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            File dir = new File(log_dir);
            Log.i("Log dir", log_dir);
            if(dir.exists() || dir.mkdirs()) {
                File file = new File(log_dir,log_name);
                FileWriter out = new FileWriter(file, true);
                out.write(str);
                out.close();
                return file.getCanonicalPath();
            } else {
                throw new IOException("Can`t create dir");
            }
        } else {
            throw new IOException("No sd-card");
        }
    }

    public static String getAssetAsString(String name) {
        try {
            InputStream is = getAsset(name);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");

        } catch (IOException ex) {
            ex.printStackTrace();
            throw new RuntimeException();
        }
    }

}
