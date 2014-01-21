package com.skad.android.androidm2ihm.utils;

/**
 * Created by skad on 21/01/14.
 */

import android.util.Log;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class ZipUtils {
    private static final int BUFFER = 2048;

    private ZipUtils() {
    }

    public static void zip(String File, String zipFile) throws IOException {
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

    }

    public static void unpackZip(String path, String zipname) throws IOException {
        InputStream in = new FileInputStream(path + File.separator + zipname);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(in));
        ZipEntry entry;
        String filename;
        String dirname = zipname.substring(0, zipname.length() - 4);
        FileUtils.makeDir(path + File.separator + dirname);
        path = path + File.separator + dirname;

        byte[] buffer = new byte[1024];
        int count;

        while ((entry = zis.getNextEntry()) != null) {
            filename = entry.getName();
            if (entry.isDirectory()) {
                FileUtils.makeDir(path + File.separator + filename);

            } else {
                FileOutputStream fout = new FileOutputStream(path + File.separator + filename);

                while ((count = zis.read(buffer)) != -1) {
                    fout.write(buffer, 0, count);
                }

                fout.close();
                zis.closeEntry();
            }
        }

        zis.close();
    }
}