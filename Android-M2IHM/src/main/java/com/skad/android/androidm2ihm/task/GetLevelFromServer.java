package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.DownloadLevelActivity;
import com.skad.android.androidm2ihm.model.HttpClient;
import com.skad.android.androidm2ihm.utils.FileUtils;
import com.skad.android.androidm2ihm.utils.ZipUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by skad on 14/01/14.
 */
public class GetLevelFromServer extends AsyncTaskWithPopUp {
    private byte[] file;

    public GetLevelFromServer(Context c) {
        super(c, c.getString(R.string.donwload_file_lvl));

    }

    protected Long doInBackground(String... name) {

        String url = "http://skad.no-ip.org/Android-M2ihm/uploads/" + name[0];
        try {
            HttpClient client = new HttpClient(url);
            file = client.getResponse();
        } catch (Throwable t) {
            Log.e("Http :", t.toString());
        }
        publishProgress(20);
        InputStream in = new ByteArrayInputStream(file);
        String path = mContext.getExternalFilesDir(null).getPath();
        try {
            FileUtils.writeFile(in, path, name[0]);
        } catch (IOException e) {
            Log.e("Writing :", e.toString());
        }
        publishProgress(40);
        try {
            ZipUtils.unpackZip(path, name[0]);
        } catch (IOException e) {
            Log.e("Unziping :", e.toString());
        }
        publishProgress(60);
        FileUtils.deleteLvl(mContext, name[0]);
        publishProgress(100);
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        ((DownloadLevelActivity) mContext).finish();
    }
}
