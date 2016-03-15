package com.qualisystems.pythonDriverPlugin;

import org.apache.commons.lang.ArrayUtils;

import java.util.Properties;

public class DriverPublisherSettings {

    private static final String[] DefaultFileFilters = new String[] { ".idea", "deployment", "deployment.xml", "debug_info.txt" };

    public String serverRootAddress;
    public int port;
    public String driverUniqueName;
    public String username;
    public String password;
    public String domain;
    public String[] fileFilters;
    public String sourceRootFolder;
    public boolean waitForDebugger;
    public boolean runFromLocalProject;
    public ProvisioningItemData[] drivers;
    public ProvisioningItemData[] scripts;

    public static DriverPublisherSettings fromProperties(Properties deploymentProperties) throws IllegalArgumentException {

        if (!deploymentProperties.containsKey("driverUniqueName"))
            throw new IllegalArgumentException("Missing `driverUniqueName` key in project's deployment.xml");

        if (!deploymentProperties.containsKey("serverRootAddress"))
            throw new IllegalArgumentException("Missing `serverRootAddress` key in project's deployment.xml");

        DriverPublisherSettings settings = new DriverPublisherSettings();

        settings.serverRootAddress = deploymentProperties.getProperty("serverRootAddress");
        settings.port = Integer.parseInt(deploymentProperties.getProperty("port", "8029"));
        settings.driverUniqueName = deploymentProperties.getProperty("driverUniqueName");
        settings.username = deploymentProperties.getProperty("username", "admin");
        settings.password = deploymentProperties.getProperty("password", "admin");
        settings.domain = deploymentProperties.getProperty("domain", "Global");
        settings.sourceRootFolder = deploymentProperties.getProperty("sourceRootFolder", null);
        settings.waitForDebugger = Boolean.parseBoolean(deploymentProperties.getProperty("waitForDebugger", "false"));
        settings.runFromLocalProject = Boolean.parseBoolean(deploymentProperties.getProperty("runFromLocalProject", "false"));

        String fileFiltersValue = deploymentProperties.getProperty("fileFilters", "");

        String[] extraFilters = fileFiltersValue.isEmpty() ? new String[0] : fileFiltersValue.split(";");

        settings.fileFilters = (String[])ArrayUtils.addAll(DefaultFileFilters, extraFilters);

        return settings;
    }

    public class ProvisioningItemData{
        public String sourceFolder;
        public String targetName;
    }
}
