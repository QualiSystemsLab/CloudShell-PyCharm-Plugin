package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.SourcesType;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

/**
 * Created by tom.h on 16/03/2016.
 */
public abstract  class ArchiveAnalyzer implements IArchiveAnalyzer {
    private String[] _filters;
    protected String _basePath;

    public ArchiveAnalyzer(String[] filters, String basePath) {
        _filters = filters;
        _basePath = basePath;
    }

    public abstract String addToArchive(ZipHelper zipHelper, String target, List<String> sources) throws  IOException;

    protected abstract HashMap<String,List<String>> getTargetToSourceMap();

    protected abstract String getDeploymentDirectory();

    /// returns target name and corresponding zip file path
    @Override
    public HashMap<String, String> getArcivedFiles() throws IOException {
        HashMap<String, String> targetToZipFilePathMap = new HashMap<>();
        HashMap<String,List<String>> targetToSourcesMap = getTargetToSourceMap();
        ZipHelper zipHelper = new ZipHelper(_filters);

        targetToSourcesMap.forEach((target,sources)->{
            try {
                String zipFileName = addToArchive(zipHelper, target, sources);
                targetToZipFilePathMap.put(target, zipFileName);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        return targetToZipFilePathMap;
    }

    protected List<String> getFullPath(List<String> sources)
    {
        List<String> fullPathSources = new ArrayList<>();
        for (String source : sources) {

            String fullPath = _basePath + "/" + source;
            File folder = new File(fullPath);
            if(folder.isDirectory())
            {
                String[] filePaths = folder.list();
                for (String path: filePaths) {
                    fullPathSources.add(_basePath + "/"  + source + "/" + path);
                }
            }
            else {
                fullPathSources.add(fullPath);
            }

        }
        return fullPathSources;
    }
}
