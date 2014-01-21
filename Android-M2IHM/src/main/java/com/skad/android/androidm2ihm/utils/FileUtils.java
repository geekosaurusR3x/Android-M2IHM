package com.skad.android.androidm2ihm.utils;

import android.content.Context;
import android.os.Environment;

import java.io.*;
import java.util.ArrayList;

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

    public static void writeFile(InputStream in, String path, String nameFile) throws IOException {
        File file = new File(path, nameFile);
        OutputStream out = new FileOutputStream(file);
        writeDataFile(in, out);
        out.flush();
        out.close();
    }

    private static void writeDataFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    public static void makeDir(String name) {

        if (!fileExist(name)) {
            File dir = new File(name);
            dir.mkdir();
        }
    }

    public static boolean fileExist(String name) {
        File file = new File(name);
        return file.exists();
    }

    public static ArrayList<String> listLvl(Context context) {
        File dir = new File(context.getExternalFilesDir(null), "");
        String[] temp = dir.list();
        ArrayList<String> list_lvl = new ArrayList<String>();
        for (String dirname : temp) {
            if (!dirname.matches("default")) {
                list_lvl.add(dirname);
            }
        }
        return list_lvl;
    }

    public static void deleteLvl(Context context, String name) {
        String path = context.getExternalFilesDir(null) + File.separator + name;
        File dir = new File(path);
        if (dir.isDirectory() && fileExist(path)) {
            String[] listFiles = dir.list();
            for (int i = 0; i < listFiles.length; i++) {
                new File(dir, listFiles[i]).delete();
            }
        }
        dir.delete();
    }

    public static String getfileordefault(Context context, String path, String fileName) {
        String file = path + File.separator + fileName;
        if (!FileUtils.fileExist(file)) {
            file = context.getExternalFilesDir(null) + File.separator + "default" + File.separator + fileName;
        }
        return file;
    }
}
