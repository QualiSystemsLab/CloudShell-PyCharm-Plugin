package com.qualisystems.pythonDriverPlugin;

import org.apache.commons.lang.ArrayUtils;

import java.util.Properties;

public class DriverPublisherSettings {

    String serverRootAddress;
    int port;
    String driverUniqueName;
    String username;
    String password;
    String domain;
    String[] fileFilters;
    String sourceRootFolder;

    private static final String[] DefaultFileFilters = new String[] { ".idea", "deployment", "deployment.xml" };

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

        String fileFiltersValue = deploymentProperties.getProperty("fileFilters", "");

        String[] extraFilters = fileFiltersValue.isEmpty() ? new String[0] : fileFiltersValue.split(";");

        settings.fileFilters = (String[])ArrayUtils.addAll(DefaultFileFilters, extraFilters);

        return settings;
    }
}
