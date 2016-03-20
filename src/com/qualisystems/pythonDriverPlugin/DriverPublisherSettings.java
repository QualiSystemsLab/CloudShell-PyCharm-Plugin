package com.qualisystems.pythonDriverPlugin;

import com.qualisystems.pythonDriverPlugin.deployment.DriversType;
import com.qualisystems.pythonDriverPlugin.deployment.PropertiesType;
import com.qualisystems.pythonDriverPlugin.deployment.ScriptsType;
import org.apache.commons.lang.ArrayUtils;

public class DriverPublisherSettings {

    private static final String[] DefaultFileFilters = new String[] { ".idea", "deployment", "deployment.xml", "debug_info.txt" };

    public String serverRootAddress;
    public int port;
    public String username;
    public String password;
    public String domain;
    public String[] fileFilters;
    public boolean waitForDebugger;
    public boolean runFromLocalProject;
    public DriversType drivers;
    public ScriptsType scripts;

    public static DriverPublisherSettings fromProperties(PropertiesType deploymentProperties) throws IllegalArgumentException {

        ensureDeploymentProperties(deploymentProperties);
        if (deploymentProperties.getServerRootAddress() == null || deploymentProperties.getServerRootAddress().isEmpty())
            throw new IllegalArgumentException("Missing `serverRootAddress` key in project's deployment.xml");

        DriverPublisherSettings settings = new DriverPublisherSettings();

        settings.serverRootAddress = deploymentProperties.getServerRootAddress();
        settings.port = Integer.parseInt(deploymentProperties.getPort());
        settings.username = deploymentProperties.getUsername();
        settings.password = deploymentProperties.getPassword();
        settings.domain = deploymentProperties.getDomain();

        String fileFiltersValue = deploymentProperties.getFileFilters();
        String[] extraFilters = fileFiltersValue.isEmpty() ? new String[0] : fileFiltersValue.split(";");
        settings.fileFilters = (String[])ArrayUtils.addAll(DefaultFileFilters, extraFilters);

        settings.drivers = deploymentProperties.getDrivers();
        settings.scripts = deploymentProperties.getScripts();

        return settings;
    }

    private static void ensureDeploymentProperties(PropertiesType deploymentProperties) throws IllegalArgumentException{

        if (deploymentProperties.getServerRootAddress() == null || deploymentProperties.getServerRootAddress().isEmpty())
            throw new IllegalArgumentException("Missing `serverRootAddress` key in project's deployment.xml");

        if(deploymentProperties.getPort() == null || deploymentProperties.getPort().isEmpty())
            deploymentProperties.setPort("8029");

        if(deploymentProperties.getUsername() == null || deploymentProperties.getUsername().isEmpty())
            deploymentProperties.setUsername("admin");

        if(deploymentProperties.getPassword() == null || deploymentProperties.getPassword().isEmpty())
            deploymentProperties.setPassword("admin");

        if(deploymentProperties.getDomain() == null || deploymentProperties.getDomain().isEmpty())
            deploymentProperties.setDomain("Global");

        if(deploymentProperties.getWaitForDebugger() == null || deploymentProperties.getWaitForDebugger().isEmpty())
            deploymentProperties.setWaitForDebugger("false");

    }


}
