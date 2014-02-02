package com.skad.android.androidm2ihm.utils;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * Utilities functions for manipulating files
 * Created by skad on 13/01/14.
 */
public class FileUtils {
    private FileUtils() {

    }

    /* Checks if external storage is available for read and write */
    public static boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state);
    }

    /* Checks if external storage is available to at least read */
    public static boolean isExternalStorageReadable() {
        String state = Environment.getExternalStorageState();
        return Environment.MEDIA_MOUNTED.equals(state) ||
                Environment.MEDIA_MOUNTED_READ_ONLY.equals(state);
    }

    /**
     * Create an OutputStream from nameFile into path Dir and write InputSream in into it
     *
     * @param in
     * @param path
     * @param nameFile
     * @throws IOException
     * @see com.skad.android.androidm2ihm.utils.FileUtils#writeDataFile(java.io.InputStream, java.io.OutputStream)
     */
    public static void writeFile(InputStream in, String path, String nameFile) throws IOException {
        File file = new File(path, nameFile);
        OutputStream out = new FileOutputStream(file);
        writeDataFile(in, out);
        out.flush();
        out.close();
    }

    /**
     * Write the InputStream in into the OutputStream out
     *
     * @param in
     * @param out
     * @throws IOException
     */
    private static void writeDataFile(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[1024];
        int read;
        while ((read = in.read(buffer)) != -1) {
            out.write(buffer, 0, read);
        }
    }

    /**
     * Read a byte from fileName
     *
     * @param fileName
     * @return byte array
     * @throws IOException
     */
    public static byte[] bytesFromFile(String fileName) throws IOException {
        File file = new File(fileName);
        byte[] buffer = new byte[(int) file.length()];
        InputStream is;
        is = new FileInputStream(file);
        while (is.read(buffer) != -1) ;
        is.close();

        return buffer;
    }

    /**
     * Create the directory if it does not exist yet
     *
     * @param name The name of the new directory
     */
    public static void makeDir(String name) {
        if (!fileExist(name)) {
            File dir = new File(name);
            dir.mkdir();
        }
    }

    /**
     * Check if a file exist
     *
     * @param name
     * @return boolean value
     */
    public static boolean fileExist(String name) {
        File file = new File(name);
        return file.exists();
    }

    public static String basename(String path) {
        int basenameIndex = path.lastIndexOf(File.separator);
        return path.substring(basenameIndex + 1);
    }

    /**
     * Generates a List of Strings holding all Level file names
     *
     * @param context The current context
     * @return Level List
     */
    public static List<String> listLvl(Context context) {
        File dir = new File(context.getExternalFilesDir(null), "");
        String[] temp = dir.list();
        List<String> list_lvl = new ArrayList<String>();
        if (temp != null) {
            for (String dirname : temp) {
                if (!dirname.matches("default")) {
                    list_lvl.add(dirname);
                }
            }
        }
        return list_lvl;
    }

    /**
     * Explode the list into arrayList from the delimiteur
     *
     * @param list
     * @param delimiteur
     * @return String ArrayList
     */
    public static ArrayList<String> listLvlfromstring(String list, String delimiteur) {
        ArrayList<String> list_lvl = new ArrayList<String>();
        StringTokenizer tokens = new StringTokenizer(list, delimiteur);
        while (tokens.hasMoreTokens()) {
            list_lvl.add(tokens.nextToken());
        }
        return list_lvl;
    }

    /**
     * Delete a lvldir from the name given
     *
     * @param context
     * @param name
     */
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

    /**
     * Get the absolute file path for the fileName into the path or return default file if file not exist
     *
     * @param context
     * @param path
     * @param fileName
     * @return
     */
    public static String getfileordefault(Context context, String path, String fileName) {
        String file = path + File.separator + fileName;
        if (!FileUtils.fileExist(file)) {
            file = context.getExternalFilesDir(null) + File.separator + "default" + File.separator + fileName;
        }
        return file;
    }
}
