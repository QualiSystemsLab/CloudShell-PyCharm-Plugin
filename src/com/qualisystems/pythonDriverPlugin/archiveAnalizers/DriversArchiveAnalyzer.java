package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.AuthoringItemType;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom.h on 16/03/2016.
 */
public class DriversArchiveAnalyzer extends ArchiveAnalyzer {
    private static final String DIRECTORY = "drivers";

    public DriversArchiveAnalyzer(List<AuthoringItemType> authoringItems, String[] filters, String baseZipPath) {
        super(authoringItems, filters, baseZipPath);
    }

    @Override
    public String getDeploymentDirectory() {
        return DIRECTORY;
    }

    public void addToArchive(HashMap<String, String> targetToZipFilePathMap, ZipHelper zipHelper, String target, List<String> sources) throws IOException {
        String zipFileName = _baseZipPath + "/deployment/" + getDeploymentDirectory() + "/" + target + ".zip";
        zipHelper.zipFiles(zipFileName,getFullPath(sources));
        targetToZipFilePathMap.put(target, zipFileName);
    }
}
