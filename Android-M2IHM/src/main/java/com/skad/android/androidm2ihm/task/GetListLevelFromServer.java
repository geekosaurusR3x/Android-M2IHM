package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.DownloadLevelActivity;
import com.skad.android.androidm2ihm.model.HttpClient;

/**
 * Created by skad on 14/01/14.
 */
public class GetListLevelFromServer extends AsyncTaskWithPopUp {
    private String list;

    public GetListLevelFromServer(Context c) {
        super(c, c.getString(R.string.donwload_list_lvl));

    }

    protected Long doInBackground(String... dirname) {

        String url = "http://skad.no-ip.org/Android-M2ihm/listfile.php";
        try {
            HttpClient client = new HttpClient(url);
            client.requestFile("");
            list = new String(client.getResponse());
            Log.d("Http :", "code " + list);
        } catch (Throwable t) {
            Log.e("Http :", t.toString());
        }

        publishProgress(100);
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        ((DownloadLevelActivity) mContext).parsingList(list);
    }
}
