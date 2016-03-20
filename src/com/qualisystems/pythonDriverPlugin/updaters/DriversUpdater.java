package com.qualisystems.pythonDriverPlugin.updaters;

import com.qualisystems.pythonDriverPlugin.DriverPublisherSettings;

import java.io.File;

public class DriversUpdater extends Updater {


    protected DriversUpdater(DriverPublisherSettings settings) {
        super(settings);
    }

    @Override
    void update(String targetName, String fileToUpload) throws Exception {
        _resourceManagementService.updateDriver(targetName, new File(fileToUpload));
    }
}


