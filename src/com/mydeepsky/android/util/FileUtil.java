package com.mydeepsky.android.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

public class FileUtil {
    private static final int BUFFER_SIZE = 4 * 1024;

    public static String parseFilename(String url) {
        try {
            String[] tmp = url.split("/");
            String filename = tmp[tmp.length - 1];
            if (WebUtil.isLocalUri(url))
                return filename;
            else
                return URLDecoder.decode(filename, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseFileDir(String filepath) {
        int index = filepath.lastIndexOf('/');
        return filepath.substring(0, index + 1);
    }

    public static String getFrontname(String filename) {
        return filename.split("\\.")[0];
    }

    public static String getExtension(String filename) {
        String[] tmp = filename.split("\\.");
        return "." + tmp[tmp.length - 1];
    }

    public static File copyFile(String oldPath, String newPath) throws IOException {
        InputStream inStream = new FileInputStream(oldPath);
        return copyFile(inStream, newPath);
    }

    public static File copyFile(InputStream inStream, String target) throws IOException {
        FileOutputStream fs = new FileOutputStream(target);
        byte[] buffer = new byte[BUFFER_SIZE];
        int byteread;
        while ((byteread = inStream.read(buffer)) != -1) {
            fs.write(buffer, 0, byteread);
        }
        inStream.close();
        fs.close();
        return new File(target);
    }

    public static void deleteAllFiles(File file) {
        if (file != null && file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File files[] = file.listFiles();
                for (File subfile : files)
                    deleteAllFiles(subfile);
            }
        }
    }

    public static File getFileFromUrl(String url, String target) throws IOException {
        InputStream inputStream = (InputStream) new URL(url).getContent();
        return FileUtil.copyFile(inputStream, target);
    }
}
