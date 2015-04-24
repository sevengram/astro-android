package com.mydeepsky.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

public class ZipUtil {
    private static final int BUFFER_SIZE = 4 * 1024;

    public static void zip(String inputFileName, String outputFileName) throws IOException {
        ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outputFileName));
        File f = new File(inputFileName);
        if (f.isDirectory())
            zip(out, f, "");
        else
            zip(out, f, f.getName());
        out.close();

    }

    public static void zip(String inputFileName) throws IOException {
        zip(inputFileName, inputFileName + ".zip");
    }

    private static void zip(ZipOutputStream out, File f, String base) throws IOException {
        if (f.isDirectory()) {
            File[] fl = f.listFiles();
            out.putNextEntry(new ZipEntry(base + "/"));
            base = base.length() == 0 ? "" : base + "/";
            for (int i = 0; i < fl.length; i++) {
                zip(out, fl[i], base + fl[i].getName());
            }
        } else {
            out.putNextEntry(new ZipEntry(base));
            FileInputStream in = new FileInputStream(f);
            int bytes;
            while ((bytes = in.read()) != -1) {
                out.write(bytes);
            }
            in.close();
        }
    }

    public static void unzip(String outputFileName) throws IOException {
        int index = outputFileName.indexOf(".");
        String outputDirectory = outputFileName.substring(0, index);
        unzip(outputFileName, outputDirectory);
    }

    public static void unzip(String zipFilename, String outputDirectory) throws IOException {
        File outFile = new File(outputDirectory);
        if (!outFile.exists()) {
            outFile.mkdirs();
        }
        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration<? extends ZipEntry> en = zipFile.entries();
        ZipEntry zipEntry = null;
        while (en.hasMoreElements()) {
            zipEntry = (ZipEntry) en.nextElement();
            if (zipEntry.isDirectory()) {
                // mkdir directory
                String dirName = zipEntry.getName();
                dirName = dirName.substring(0, dirName.length() - 1);
                new File(outFile.getPath() + "/" + dirName).mkdirs();
            } else {
                // unzip file
                File f = new File(outFile.getPath() + "/" + zipEntry.getName());
                f.createNewFile();
                InputStream in = zipFile.getInputStream(zipEntry);
                FileOutputStream out = new FileOutputStream(f);
                int count;
                byte[] bytes = new byte[BUFFER_SIZE];
                while ((count = in.read(bytes)) != -1) {
                    out.write(bytes, 0, count);
                }
                out.close();
                in.close();
            }
        }
    }
}
