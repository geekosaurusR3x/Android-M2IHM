package com.skad.android.androidm2ihm.task;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

/**
 * Created by skad on 21/01/14.
 */

public class AsyncTaskWithPopUp extends AsyncTask<String, Integer, Long> {

    protected Context mContext;
    private ProgressDialog mProgress;
    protected int mProgresPrecent;
    private String mMsg;

    public AsyncTaskWithPopUp(Context c, String msg) {
        super();
        mContext = c;
        mMsg = msg;

    }

    @Override
    protected Long doInBackground(String... strings) {
        return null;
    }

    protected void onPreExecute() {
        super.onPreExecute();
        mProgress = new ProgressDialog(mContext);
        mProgress.setMessage(mMsg);
        mProgress.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgress.setProgress(0);
        mProgress.show();
    }


    protected void onProgressUpdate(Integer... progres) {
        super.onProgressUpdate(progres);
        mProgress.setProgress(progres[0]);
    }

    protected void onPostExecute(Long result) {
        mProgress.dismiss();
    }
}
