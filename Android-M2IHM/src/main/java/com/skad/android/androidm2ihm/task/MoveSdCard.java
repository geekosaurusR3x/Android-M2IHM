package com.skad.android.androidm2ihm.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.MainActivity;

import java.net.URL;

/**
 * Created by skad on 14/01/14.
 */
public class MoveSdCard extends AsyncTask<URL, Integer, Long>{

    private Context mContext;
    private ProgressDialog progress;

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
        int progres;
        for (progres=0;progres<=100;progres++)
        {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            publishProgress(progres);
        }
        return null;
    }

    protected void onProgressUpdate(Integer... progres) {
        super.onProgressUpdate(progres);
        progress.setProgress(progres[0]);
    }

    protected void onPostExecute(Long result) {
         progress.dismiss();
        ((MainActivity)mContext).fecthLevel();
    }
}
