package com.qualisystems.pythonDriverPlugin.updaters;

import com.qualisystems.pythonDriverPlugin.DriverPublisherSettings;
import com.qualisystems.pythonDriverPlugin.deployment.AuthoringItemType;

import java.io.File;
import java.util.List;
import java.util.Map;

public class DriversUpdater extends Updater {


    protected DriversUpdater(DriverPublisherSettings settings) {
        super(settings);
    }

    @Override
    void update(String targetName, String fileToUpload) throws Exception {
        _resourceManagementService.updateDriver(targetName, new File(fileToUpload));
    }
}


