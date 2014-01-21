package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.MainActivity;
import com.skad.android.androidm2ihm.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by skad on 14/01/14.
 */
public class MoveSdCard extends AsyncTaskWithPopUp {

    public MoveSdCard(Context c) {
        super(c, c.getString(R.string.moving_content_sdcard));

    }

    @Override
    protected Long doInBackground(String... urls) {
        mProgresPrecent = 0;
        parsingAsset();
        return null;
    }

    protected void parsingAsset() {
        AssetManager assetManager = mContext.getAssets();
        String[] dirs = null;
        String[] sousdirs = null;

        try {
            dirs = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        Log.d("tag", "nb files " + dirs.length);
        for (String dir : dirs) {
            try {
                sousdirs = assetManager.list(dir);
            } catch (IOException e) {
                Log.e("tag", "Failed to get asset file list.", e);
            }
            Log.d("tag", "Dir " + dir);
            if (!(dir.matches("images") || dir.matches("sounds") || dir.matches("webkit"))) //useless assets dir
            {
                FileUtils.makeDir(mContext.getExternalFilesDir(null) + File.separator + dir);
                for (String filename : sousdirs) {
                    copyAssets(dir + File.separator + filename);
                    mProgresPrecent++;
                    publishProgress(mProgresPrecent);
                }
            }
        }
    }

    protected void copyAssets(String filename) {
        Log.d("tag", "File : " + filename);
        AssetManager assetManager = mContext.getAssets();
        InputStream in = null;
        String path = mContext.getExternalFilesDir(null).getPath();

        try {
            in = assetManager.open(filename);
            FileUtils.writeFile(in, path, filename);
            in.close();
            in = null;
        } catch (IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        }
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);

        ((MainActivity) mContext).onFinishMoveFile();
    }
}
