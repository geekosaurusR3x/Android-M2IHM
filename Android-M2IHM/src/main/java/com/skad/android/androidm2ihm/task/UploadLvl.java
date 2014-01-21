package com.skad.android.androidm2ihm.task;

import android.content.Context;
import android.util.Log;
import com.skad.android.androidm2ihm.R;
import com.skad.android.androidm2ihm.activity.MainActivity;
import com.skad.android.androidm2ihm.model.HttpClient;
import com.skad.android.androidm2ihm.utils.FileUtils;
import com.skad.android.androidm2ihm.utils.ZipUtils;

import java.io.File;

/**
 * Created by skad on 14/01/14.
 */
public class UploadLvl extends AsyncTaskWithPopUp {

    public UploadLvl(Context c) {
        super(c, c.getString(R.string.uploading_content_server));

    }

    protected Long doInBackground(String... dirname) {
        //compressing file
        String path = mContext.getExternalFilesDir(null).toString() + File.separator + dirname[0];
        //deleting old upload
        if (FileUtils.fileExist(path + File.separator + dirname[0] + ".zip")) {
            FileUtils.deleteLvl(mContext, dirname[0] + File.separator + dirname[0] + ".zip");
        }
        Log.d("Files", "Path: " + path);
        File f = new File(path);
        File file[] = f.listFiles();
        Log.d("Files", "Size: " + file.length);
        for (int i = 0; i < file.length; i++) {
            Log.d("Files", "FileName:" + file[i].getName());
            ZipUtils.zip(path + File.separator + file[i].getName(), path + File.separator + dirname[0] + ".zip");
            mProgresPrecent++;
            publishProgress(mProgresPrecent);
        }

        String url = "http://skad.no-ip.org/Android-M2ihm/upload.php";
        String fileName = path + File.separator + dirname[0] + ".zip";
        try {
            HttpClient client = new HttpClient(url);
            client.setMultipart();
            client.addFilePart(dirname[0] + ".zip", FileUtils.bytesFromFile(fileName));
            client.finishMultipart();
            String data = client.getResponse();
            Log.d("Http :", "code " + data);
        } catch (Throwable t) {
            Log.e("Http :", t.toString());
        }

        publishProgress(100);
        return null;
    }

    @Override
    protected void onPostExecute(Long result) {
        super.onPostExecute(result);
        ((MainActivity) mContext).onFinishMoveFile();
    }
}
