package com.qualisystems.pythonDriverPlugin;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipHelper {

    private final String[] _filters;
    private final Map<String, ByteBuffer> _extraFiles;

    public ZipHelper(Map<String, ByteBuffer> extraFiles, String... filters) {

        if (filters == null)
            filters = new String[0];

        if (extraFiles == null)
            extraFiles = new HashMap<>();

        _filters = filters;
        _extraFiles = extraFiles;
    }

    public ZipHelper(String... filters) {

        this(null, filters);
    }

    public void zipDir(String dirName, String nameZipFile) throws IOException {

        ZipOutputStream zip = null;
        FileOutputStream fW = null;

        Files.createDirectories(Paths.get(new File(nameZipFile).getParent()));

        fW = new FileOutputStream(nameZipFile);
        zip = new ZipOutputStream(fW);

        initialAddFolderToZip(dirName, zip);

        for (Map.Entry<String, ByteBuffer> extraFile : _extraFiles.entrySet()) {
            addFileToZip("", extraFile.getKey(), zip, false, new ByteArrayInputStream(extraFile.getValue().array(),extraFile.getValue().arrayOffset(), extraFile.getValue().limit()));
        }

        zip.close();
        fW.close();
    }

    private void initialAddFolderToZip(String srcFolder, ZipOutputStream zip) throws IOException {

        File folder = new File(srcFolder);
        String[] list = folder.list();
        if(list == null)
        {
            throw new IOException(String.format("error: folder %s does not exist in the project",srcFolder));
        }
        for (String fileName : list) {
            addFileToZip("", srcFolder + "/" + fileName, zip, false);
        }
    }

    private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws IOException {

        File folder = new File(srcFolder);

        if (folder.list().length == 0) {

            addFileToZip(path , srcFolder, zip, true);

        } else {
            for (String fileName : folder.list()) {
                if (path.equals("")) {
                    addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip, false);
                } else {
                    addFileToZip(path + "/" + folder.getName(), srcFolder + "/" + fileName, zip, false);
                }
            }
        }
    }

    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag) throws IOException {
        addFileToZip(path, srcFile, zip, flag, null);
    }

    private void addFileToZip(String path, String srcFile, ZipOutputStream zip, boolean flag, InputStream fileDataStream) throws IOException {

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

                InputStream in = fileDataStream == null ? new FileInputStream(srcFile) : fileDataStream;

                zip.putNextEntry(new ZipEntry(nameInZip));

                while ((len = in.read(buf)) > 0)
                    zip.write(buf, 0, len);
            }
        }
    }
}
