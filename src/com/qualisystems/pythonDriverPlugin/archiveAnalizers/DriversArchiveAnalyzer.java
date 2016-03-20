package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.ZipHelper;
import com.qualisystems.pythonDriverPlugin.deployment.DriverType;
import com.qualisystems.pythonDriverPlugin.deployment.DriversType;
import com.qualisystems.pythonDriverPlugin.deployment.SourcesType;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tom.h on 16/03/2016.
 */
public class DriversArchiveAnalyzer extends ArchiveAnalyzer {

    public static final String DebugSettingsFileName = "debug.xml";

    public static final String DebugSettingsFormatString =
            "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>\n" +
                    "<properties>\n" +
                    "<entry key=\"loadFrom\">%s</entry>\n" +
                    "<entry key=\"waitForDebugger\">%s</entry>\n" +
                    "</properties>\n";

    private static final String DIRECTORY = "drivers";
    private List<DriverType> _drivers;

    public DriversArchiveAnalyzer(List<DriverType> drivers, String[] filters, String basePath) {
        super(filters, basePath);
        _drivers = drivers;
    }

    @Override
    protected String getDeploymentDirectory() {
        return DIRECTORY;
    }

    @Override
    public String addToArchive(ZipHelper zipHelper, String target, List<String> sources) throws IOException {
        String zipFileName = _basePath + "/deployment/" + getDeploymentDirectory() + "/" + target + ".zip";
        DriverType theDriver = getDriverByTarget(target);

        HashMap<String, ByteBuffer> extraFiles = new HashMap<>();
        if(theDriver.isRunFromLocalProject())
        {
            String directory = _basePath +  "/" + theDriver.getSourceRootFolder();
            String debugSettingsFileContent = String.format(DebugSettingsFormatString, directory, Boolean.toString(theDriver.isWaitForDebugger()));
            extraFiles.put(DebugSettingsFileName, StandardCharsets.UTF_8.encode(debugSettingsFileContent));
        }

        zipHelper.zipFiles(zipFileName,getFullPath(sources), extraFiles);
        return  zipFileName;
    }

    private DriverType getDriverByTarget(String target) {
        for (DriverType driver : _drivers)
        {
            if(driver.getTargetName() == target)
                return driver;
        }
        return null;
    }

    @Override
    protected HashMap<String,List<String>> getTargetToSourceMap() {

        HashMap<String, List<String>> targetToSourcesMap = new HashMap<>();
        for (DriverType driver: _drivers) {
            String targetName = driver.getTargetName();
            SourcesType sources = driver.getSources();

            if(sources != null)
            {
                targetToSourcesMap.put(targetName, sources.getSource());
            }
            else{
                String[] rootPathAsList = {""}; // gets all the project's files
                targetToSourcesMap.put(targetName, Arrays.asList(rootPathAsList));
            }
        }

        return targetToSourcesMap;
    }



}
