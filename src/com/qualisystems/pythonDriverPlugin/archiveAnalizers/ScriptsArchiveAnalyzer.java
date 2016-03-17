package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.intellij.util.io.IOUtil;
import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.AuthoringItemType;
import org.apache.sanselan.util.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom.h on 16/03/2016.
 */
public class ScriptsArchiveAnalyzer extends ArchiveAnalyzer {
    private static final String DIRECTORY = "scripts";

    public ScriptsArchiveAnalyzer(List<AuthoringItemType> authoringItems, String[] filters, String baseZipPath) {
        super(authoringItems, filters, baseZipPath);
    }

    @Override
    public String getDeploymentDirectory() {
        return DIRECTORY;
    }

    @Override
    public void addToArchive(HashMap<String, String> targetToZipFilePathMap, ZipHelper zipHelper, String target, List<String> sources) throws IOException {
        List<String> sourcesWithFullPath = getFullPath(sources);
        String outputFilePath = "";
        if(sourcesWithFullPath.size() == 0)
        {
            throw new IllegalArgumentException(String.format("sources for target %s where not found",target));
        }
        if(sourcesWithFullPath.size() == 1)
        {
            String sourceFilePath = sourcesWithFullPath.get(0);
            String sourceFileName = new File(sourceFilePath).toPath().getFileName().toString();
            String targetFilePath = _baseZipPath + "/deployment/" + getDeploymentDirectory() + "/" + sourceFileName;
            outputFilePath = copyToOutputDirectory(sourceFilePath, targetFilePath);
        }
        else {
            String zipFileName = _baseZipPath + "/deployment/" + getDeploymentDirectory() + "/" + target + ".zip";
            zipHelper.zipFiles(zipFileName, sourcesWithFullPath);
            outputFilePath = zipFileName;

        }

        targetToZipFilePathMap.put(target, outputFilePath);
    }

    private String copyToOutputDirectory(String sourceFileName, String targetFileName)throws IOException {
        Files.createDirectories(Paths.get(new File(targetFileName).getParent())).toString();


        Files.copy(new File(sourceFileName).toPath(),new File(targetFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return targetFileName;
    }
}
