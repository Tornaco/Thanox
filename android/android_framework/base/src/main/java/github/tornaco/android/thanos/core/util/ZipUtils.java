package github.tornaco.android.thanos.core.util;

import android.text.TextUtils;

import java.io.*;
import java.util.Enumeration;
import java.util.zip.*;

/**
 * Created by stuart on 2017/7/14.
 * https://raw.githubusercontent.com/Tornaco/DataMigration/master/app/src/main/java/org/newstand/datamigration/utils/ZipUtils.java
 */

public class ZipUtils {

    private static void zip(String srcRootDir, File file, ZipOutputStream zos) throws Exception {
        if (file == null) {
            return;
        }

        if (file.isFile()) {
            int count, bufferLen = 1024;
            byte data[] = new byte[bufferLen];

            String subPath = file.getAbsolutePath();
            int index = subPath.indexOf(srcRootDir);
            if (index != -1) {
                subPath = subPath.substring(srcRootDir.length() + File.separator.length());
            }
            ZipEntry entry = new ZipEntry(subPath);
            zos.putNextEntry(entry);
            BufferedInputStream bis = new BufferedInputStream(new FileInputStream(file));
            while ((count = bis.read(data, 0, bufferLen)) != -1) {
                zos.write(data, 0, count);
            }
            bis.close();
            zos.closeEntry();
        } else {
            File[] childFileList = file.listFiles();
            if (childFileList != null) for (File aChildFileList : childFileList) {
                aChildFileList.getAbsolutePath().indexOf(file.getAbsolutePath());
                zip(srcRootDir, aChildFileList, zos);
            }
        }
    }

    public static void zip(String srcPath, String zipPath, String zipFileName) throws Exception {
        if (TextUtils.isEmpty(srcPath) || TextUtils.isEmpty(zipPath) || TextUtils.isEmpty(zipFileName)) {
            throw new NullPointerException();
        }
        CheckedOutputStream cos = null;
        ZipOutputStream zos = null;
        try {
            File srcFile = new File(srcPath);

            if (srcFile.isDirectory() && zipPath.contains(srcPath)) {
                throw new IllegalStateException(String.format("zipPath %s must not be the child directory of srcPath %s", zipPath, srcPath));
            }

            File zipDir = new File(zipPath);
            if (!zipDir.exists() || !zipDir.isDirectory()) {
                zipDir.mkdirs();
            }

            String zipFilePath = zipPath + File.separator + zipFileName;
            File zipFile = new File(zipFilePath);
            if (zipFile.exists()) {
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkDelete(zipFilePath);
                zipFile.delete();
            }

            cos = new CheckedOutputStream(new FileOutputStream(zipFile), new CRC32());
            zos = new ZipOutputStream(cos);

            String srcRootDir = srcPath;
            if (srcFile.isFile()) {
                int index = srcPath.lastIndexOf(File.separator);
                if (index != -1) {
                    srcRootDir = srcPath.substring(0, index);
                }
            }
            zip(srcRootDir, srcFile, zos);
            zos.flush();
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static void unzip(String zipFilePath, String unzipFilePath, boolean includeZipFileName) throws Exception {
        if (TextUtils.isEmpty(zipFilePath) || TextUtils.isEmpty(unzipFilePath)) {
            throw new NullPointerException();
        }
        File zipFile = new File(zipFilePath);
        if (includeZipFileName) {
            String fileName = zipFile.getName();
            if (!TextUtils.isEmpty(fileName)) {
                fileName = fileName.substring(0, fileName.lastIndexOf("."));
            }
            unzipFilePath = unzipFilePath + File.separator + fileName;
        }
        File unzipFileDir = new File(unzipFilePath);
        if (!unzipFileDir.exists() || !unzipFileDir.isDirectory()) {
            unzipFileDir.mkdirs();
        }

        ZipEntry entry = null;
        String entryFilePath = null, entryDirPath = null;
        File entryFile = null, entryDir = null;
        int index = 0, count = 0, bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        ZipFile zip = new ZipFile(zipFile);
        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
        while (entries.hasMoreElements()) {
            entry = entries.nextElement();
            entryFilePath = unzipFilePath + File.separator + entry.getName();
            index = entryFilePath.lastIndexOf(File.separator);
            if (index != -1) {
                entryDirPath = entryFilePath.substring(0, index);
            } else {
                entryDirPath = "";
            }
            entryDir = new File(entryDirPath);
            if (!entryDir.exists() || !entryDir.isDirectory()) {
                entryDir.mkdirs();
            }

            entryFile = new File(entryFilePath);
            if (entryFile.exists()) {
                SecurityManager securityManager = new SecurityManager();
                securityManager.checkDelete(entryFilePath);
                entryFile.delete();
            }

            bos = new BufferedOutputStream(new FileOutputStream(entryFile));
            bis = new BufferedInputStream(zip.getInputStream(entry));
            while ((count = bis.read(buffer, 0, bufferSize)) != -1) {
                bos.write(buffer, 0, count);
            }
            bos.flush();
            bos.close();
        }
    }
}