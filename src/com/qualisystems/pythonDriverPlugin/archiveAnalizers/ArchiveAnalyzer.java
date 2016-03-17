package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.AuthoringItemType;
import com.qualisystems.pythonDriverPlugin.deployment.SourcesType;

import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.util.*;

/**
 * Created by tom.h on 16/03/2016.
 */
public abstract  class ArchiveAnalyzer implements IArchiveAnalyzer {
    private List<AuthoringItemType> _authoringItems;
    private String[] _filters;
    protected String _baseZipPath;

    public ArchiveAnalyzer(List<AuthoringItemType> authoringItems, String[] filters, String baseZipPath) {
        _authoringItems = authoringItems;
        _filters = filters;
        _baseZipPath = baseZipPath;
    }

    public abstract String getDeploymentDirectory();

    public abstract void addToArchive(HashMap<String, String> targetToZipFilePathMap, ZipHelper zipHelper, String target, List<String> sources) throws  IOException;

    /// returns target name and corresponding zip file path
    @Override
    public HashMap<String, String> getArcivedFiles() throws IOException {
        HashMap<String, String> targetToZipFilePathMap = new HashMap<>();
        HashMap<String,List<String>> targetToSourcesMap = getTargetToSourceMap();
        ZipHelper zipHelper = new ZipHelper(_filters);

        targetToSourcesMap.forEach((target,sources)->{
            try {
                addToArchive(targetToZipFilePathMap, zipHelper, target, sources);
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        });

        return targetToZipFilePathMap;
    }


    private HashMap<String,List<String>> getTargetToSourceMap() {

        HashMap<String, List<String>> targetToSourcesMap = new HashMap<>();
        for (AuthoringItemType authoringItem: _authoringItems) {
            String targetName = authoringItem.getTargetName();
            SourcesType sources = authoringItem.getSources();
            if(sources != null)
            {
                targetToSourcesMap.put(targetName, sources.getSource());
            }
            else{
                throw new IllegalArgumentException(String.format("cannot find sources for %s", targetName));
            }

        }

        return targetToSourcesMap;
    }
    
    protected List<String> getFullPath(List<String> sources)
    {
        List<String> fullPathSources = new ArrayList<>();
        for (String source : sources) {
            String fullPath = _baseZipPath + "/" + source;
            File folder = new File(fullPath);

            if(folder.isDirectory())
            {
                String[] filePaths = folder.list();
                for (String path: filePaths) {
                    fullPathSources.add(_baseZipPath + "/"  + source + "/" + path);
                }
            }
            else {
                fullPathSources.add(fullPath);
            }
        }
        return fullPathSources;
    }
}
