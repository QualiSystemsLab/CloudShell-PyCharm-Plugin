package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.DriverType;
import com.qualisystems.pythonDriverPlugin.deployment.ScriptType;
import com.qualisystems.pythonDriverPlugin.deployment.SourcesType;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom.h on 16/03/2016.
 */
public class ScriptsArchiveAnalyzer extends ArchiveAnalyzer {
    private static final String DIRECTORY = "scripts";
    private List<ScriptType> _scripts;

    public ScriptsArchiveAnalyzer(List<ScriptType> scripts, String[] filters, String baseZipPath) {
        super( filters, baseZipPath);
        _scripts = scripts;
    }

    @Override
    protected String getDeploymentDirectory() {
        return DIRECTORY;
    }

    @Override
    public String addToArchive(ZipHelper zipHelper, String target, List<String> sources) throws IOException {
        List<String> sourcesWithFullPath = getFullPath(sources);
        String outputFilePath = "";
        if(sourcesWithFullPath.size() == 0)
        {
            throw new IllegalArgumentException(String.format("sources for target %s where not found",target));
        }
        if(sourcesWithFullPath.size() == 1)
        {
            // do not compress a single script file
            String sourceFilePath = sourcesWithFullPath.get(0);
            String sourceFileName = new File(sourceFilePath).toPath().getFileName().toString();
            String targetFilePath = _basePath + "/deployment/" + getDeploymentDirectory() + "/" + sourceFileName;
            outputFilePath = copyToOutputDirectory(sourceFilePath, targetFilePath);
        }
        else {
            String zipFileName = _basePath + "/deployment/" + getDeploymentDirectory() + "/" + target + ".zip";
            zipHelper.zipFiles(zipFileName, sourcesWithFullPath, null);
            outputFilePath = zipFileName;
        }
        return outputFilePath;
    }

    @Override
    protected HashMap<String, List<String>> getTargetToSourceMap() {
        HashMap<String, List<String>> targetToSourcesMap = new HashMap<>();
        for (ScriptType scriptType: _scripts) {
            String targetName = scriptType.getTargetName();
            SourcesType sources = scriptType.getSources();
            if(sources != null)
            {
                targetToSourcesMap.put(targetName, sources.getSource());
            }
            else{
                String[] rootPathAsList = {""};
                targetToSourcesMap.put(targetName, Arrays.asList(rootPathAsList));
            }
        }

        return targetToSourcesMap;
    }

    private String copyToOutputDirectory(String sourceFileName, String targetFileName)throws IOException {
        Files.createDirectories(Paths.get(new File(targetFileName).getParent())).toString();

        Files.copy(new File(sourceFileName).toPath(),new File(targetFileName).toPath(), StandardCopyOption.REPLACE_EXISTING);

        return targetFileName;
    }
}
