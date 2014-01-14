package com.skad.android.androidm2ihm.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.MainActivity;
import com.skad.android.androidm2ihm.utils.FileUtils;

import java.io.*;
import java.net.URL;

/**
 * Created by skad on 14/01/14.
 */
public class MoveSdCard extends AsyncTask<URL, Integer, Long>{

    private Context mContext;
    private ProgressDialog progress;
    private int mProgresPrecent;

    public MoveSdCard(Context c)
    {
        super();
        mContext = c;

    }

    protected void onPreExecute() {
        super.onPreExecute();
        progress = new ProgressDialog(mContext);
        progress.setMessage(mContext.getString(R.string.moving_content_sdcard));
        progress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progress.setProgress(0);
        progress.show();
    }

    protected Long doInBackground(URL... urls) {
        mProgresPrecent = 0;
        parsingAsset();
        return null;
    }

    protected void parsingAsset()
    {
        AssetManager assetManager = mContext.getAssets();
        String[] dirs = null;
        String[] sousdirs = null;

        try {
            dirs = assetManager.list("");
        } catch (IOException e) {
            Log.e("tag", "Failed to get asset file list.", e);
        }
        Log.d("tag","nb files "+dirs.length);
        for(String dir : dirs)
        {
            try {
                sousdirs = assetManager.list(dir);
            } catch (IOException e) {
                Log.e("tag", "Failed to get asset file list.", e);
            }
            Log.d("tag","Dir "+dir);
            FileUtils.makeDir(mContext.getExternalFilesDir(null)+File.separator+dir);
            for(String filename : sousdirs)
            {
                copyAssets(dir + File.separator + filename);
                mProgresPrecent++;
                publishProgress(mProgresPrecent);
            }
        }
    }
    protected void copyAssets(String filename)
    {
        Log.d("tag","File : "+filename);
        AssetManager assetManager = mContext.getAssets();
        InputStream in = null;
        OutputStream out = null;
        try {
            in = assetManager.open(filename);
            out = new FileOutputStream(new File(mContext.getExternalFilesDir(null), filename));
            FileUtils.copyFile(in, out);
            in.close();
            in = null;
            out.flush();
            out.close();
            out = null;
        } catch(IOException e) {
            Log.e("tag", "Failed to copy asset file: " + filename, e);
        }
    }

    protected void onProgressUpdate(Integer... progres) {
        super.onProgressUpdate(progres);
        progress.setProgress(progres[0]);
    }

    protected void onPostExecute(Long result) {
         progress.dismiss();
        ((MainActivity)mContext).onFinishMoveFile();
    }
}
