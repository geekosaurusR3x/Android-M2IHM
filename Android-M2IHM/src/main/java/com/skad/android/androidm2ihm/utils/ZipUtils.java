package com.skad.android.androidm2ihm.utils;

/**
 * Created by skad on 21/01/14.
 */

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class ZipUtils {
    private static final int BUFFER = 2048;

    private ZipUtils() {
    }

    public static void zip(String File, String zipFile) {
        try {
            BufferedInputStream origin = null;
            FileOutputStream dest = new FileOutputStream(zipFile);

            ZipOutputStream out = new ZipOutputStream(new BufferedOutputStream(dest));

            byte data[] = new byte[BUFFER];

            Log.d("Compress", "Adding: " + File);
            FileInputStream fi = new FileInputStream(File);
            origin = new BufferedInputStream(fi, BUFFER);
            ZipEntry entry = new ZipEntry(File.substring(File.lastIndexOf("/") + 1));
            out.putNextEntry(entry);
            int count;
            while ((count = origin.read(data, 0, BUFFER)) != -1) {
                out.write(data, 0, count);
            }
            origin.close();

            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}