package com.skad.android.androidm2ihm.utils;

import android.os.Environment;

import java.io.*;

/**
 * Created by skad on 13/01/14.
 */
public class FileUtils {
    private FileUtils() {

    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public static void writeFile(InputStream in,String path,String nameFile) throws IOException {
        File file = new File(path, nameFile);
        OutputStream out = new FileOutputStream(file);
        writeDataFile(in,out);
        out.flush();
        out.close();
    }

    private static void writeDataFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1)
        {
            out.write(buffer, 0, read);
        }
    }

    public static void makeDir(String name)
    {

        if (!fileExist(name))
        {
            File dir = new File(name);
            dir.mkdir();
        }
    }

    public static boolean fileExist(String name)
    {
        File file = new File(name);
        return file.exists();
    }
}
