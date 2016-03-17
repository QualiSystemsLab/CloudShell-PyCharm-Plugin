package com.qualisystems.pythonDriverPlugin.updaters;

import com.qualisystems.pythonDriverPlugin.DriverPublisherSettings;
import com.qualisystems.pythonDriverPlugin.deployment.DriversType;
import com.qualisystems.pythonDriverPlugin.deployment.ScriptsType;

/**
 * Created by tom.h on 15/03/2016.
 */
public class UpdaterFactory {
    public static IUpdater createDriversUpdater(DriverPublisherSettings settings)
    {
        return new DriversUpdater(settings);
    }

    public static IUpdater createScriptsUpdater(DriverPublisherSettings settings){
        return new ScriptsUpdater(settings);
    }
}
