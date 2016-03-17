package com.qualisystems.pythonDriverPlugin.updaters;

import com.qualisystems.pythonDriverPlugin.DriverPublisherSettings;
import com.qualisystems.pythonDriverPlugin.ResourceManagementService;

import java.io.File;
import java.util.Map;

/**
 * Created by tom.h on 16/03/2016.
 */
public abstract class Updater implements IUpdater {

    private DriverPublisherSettings _settings;
    protected ResourceManagementService _resourceManagementService;

    protected Updater(DriverPublisherSettings settings){

        _settings = settings;
    }

    abstract void update(String targetName, String fileToUpload) throws Exception;

    @Override
    public void updateFiles(Map<String, String> arcivedDriverFiles) throws Exception {

        _resourceManagementService = ResourceManagementService.OpenConnection(_settings.serverRootAddress, _settings.port, _settings.username, _settings.password, _settings.domain);

        arcivedDriverFiles.forEach((targetName, fileToUpload)->{
            try {
                update(targetName, fileToUpload);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });
    }
}
