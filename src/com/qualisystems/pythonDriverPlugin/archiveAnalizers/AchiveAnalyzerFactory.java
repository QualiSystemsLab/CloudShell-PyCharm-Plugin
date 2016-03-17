package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import com.qualisystems.pythonDriverPlugin.deployment.DriversType;
import com.qualisystems.pythonDriverPlugin.deployment.ScriptsType;

/**
 * Created by tom.h on 16/03/2016.
 */
public class AchiveAnalyzerFactory {
    public static IArchiveAnalyzer createAnalyzer(DriversType drivers, String[] fileFilters, String basePath)
    {
        if(drivers == null)
            return new NoItemsAnalyzer();

        return new DriversArchiveAnalyzer(drivers.getAuthoringItem(),fileFilters,basePath);
    }

    public static IArchiveAnalyzer createAnalyzer(ScriptsType scripts, String[] fileFilters, String basePath)
    {
        if(scripts == null)
            return new NoItemsAnalyzer();

        return new ScriptsArchiveAnalyzer(scripts.getAuthoringItem(),fileFilters,basePath);
    }
}
