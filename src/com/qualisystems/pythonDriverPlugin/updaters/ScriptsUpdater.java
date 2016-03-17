package com.qualisystems.pythonDriverPlugin.updaters;

import com.qualisystems.pythonDriverPlugin.DriverPublisherSettings;
import com.qualisystems.pythonDriverPlugin.deployment.AuthoringItemType;

import java.io.File;
import java.util.List;

public class ScriptsUpdater extends Updater {

    protected ScriptsUpdater(DriverPublisherSettings settings) {
        super(settings);
    }

    @Override
    void update(String targetName, String fileToUpload)throws Exception {
         _resourceManagementService.updateScript(targetName, new File(fileToUpload));
    }
}
