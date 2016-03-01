package com.qualisystems.pythonDriverPlugin;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    private final String[] _filters;

    public ZipHelper(String... filters) {

        if (filters == null)
            filters = new String[0];

        _filters = filters;
    }

    public void zipDir(String dirName, String nameZipFile) throws IOException {

        ZipOutputStream zip = null;
        FileOutputStream fW = null;

        Files.createDirectories(Paths.get(new File(nameZipFile).getParent()));

        fW = new FileOutputStream(nameZipFile);
        zip = new ZipOutputStream(fW);

        initialAddFolderToZip(dirName, zip);

        zip.close();
        fW.close();
    }

    private void initialAddFolderToZip(String srcFolder, ZipOutputStream zip) throws IOException {

        File folder = new File(srcFolder);

        for (String fileName : folder.list()) {
            addFileToZip("", srcFolder + "/" + fileName, zip, false);
        }
    }

    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {
        File folder = new File(srcFolder);
        if (folder.list().length == 0) {
            addFileToZip(path , srcFolder, zip, true);
        }
        else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
                else {
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }

    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {

        File folder = new File(srcFile);

        if (flag) {

            zip.putNextEntry(new ZipEntry(path + "/" +folder.getName() + "/"));

        } else {

            String nameInZip = (path.isEmpty() ? path : path + "/") + folder.getName();

            for (String filter : _filters)
                if (nameInZip.equalsIgnoreCase(filter))
                    return;

            if (folder.isDirectory()) {

                addFolderToZip(path, srcFile, zip);

            } else {

                byte[] buf = new byte[1024];
                int len;

                FileInputStream in = new FileInputStream(srcFile);
                zip.putNextEntry(new ZipEntry(nameInZip));

                while ((len = in.read(buf)) > 0)
                    zip.write(buf, 0, len);
            }
        }
    }
}
