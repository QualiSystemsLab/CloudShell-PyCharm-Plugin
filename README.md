# CloudShell-PyCharm-Plugin
A plugin for publishing drivers developed with JetBrains PyCharm products to CloudShell

# How to install Quali PyCharm python driver plugin

When working with Python resource drivers, a plugin enables updating Python drivers straight from PyCharm.  
If you don't have PyCharm you can get it from here: https://www.jetbrains.com/pycharm/download/

## Step-by-step installation guide

1. First get the latest plugin release from GitHub over here:  
    https://github.com/QualiSystemsLab/CloudShell-PyCharm-Plugin/releases
2. Once you have the plugin `.jar` file (`intelli-qs-plugin.jar`) open the settings window from within PyCharm.  
    Pressing <kbd>Ctrl</kbd> + <kbd>Alt</kbd> + <kbd>S</kbd> will open the settings window.  
    ![Settings Window](/docs/images/settings_window.png)
3. Then, choose "Plugins", and click "Install plugin from disk...".  
4. Browse and choose to the .jar plugin file (intelli-qs-plugin.jar) you just downloaded.  
5. Then, click "Restart PyCharm".  
    ![Restart Button](/docs/images/restart_button.png)
6. After PyCharm restarts the plugin would be installed.

## Using the Quali python driver plugin
1. The plugin adds the update and deploy action to two places:
    1. The module menu, opened up by right clicking the module in the project tool window on the left:  
        ![Project Tool Window](/docs/images/module_menu.png)
    2. The run and build toolbar on the top right:  
        ![Runner Actions](/docs/images/build_menu.png)
2. Click the Publish button whenever you want to update and deploy the current project. 
3. Before running the plugin, you'll need to have a file named `deployment.xml` in the project root folder,  
    this file tells the plugin how and which driver you're trying to update, you can use this snippet as a template:
    
    ``` xml
    <?xml version="1.0" encoding="UTF-8" standalone="no"?>
    <properties>

        <!-- The address of the Quali server on which to deploy, mandatory -->
        <serverRootAddress>localhost</serverRootAddress>

        <!-- The port of the Quali server on which to deploy, defaults to "8029" -->
        <port>8029</port>

        <!-- The server admin username, password and domain to use when deploying, defaults to "admin","admin" and "Global" -->
        <username>admin</username>
        <password>admin</password>
        <domain>Global</domain>

        <!-- Simple patterns to filter when sending the driver to the server separated by semicolons (e.g. "file.xml;logs/", also supports regular expressions),
             on top of the patterns specified here the plugin will automatically filter the "deployment/" and ".idea/" folders and the "deployment.xml" file -->
        <fileFilters>dont_upload_me.xml</fileFilters>

        <-- The drivers to update, holds one or more drivers -->
        <drivers>
           <!-- runFromLocalProject - Decides whether to run the driver from the current project directory for debugging purposes, defaults to "false" -->
           <!-- waitForDebugger - When `runFromLocalProject` is enabled, decides whether to wait for a debugger to attach before running any Python driver code, defaults to "false" -->
           <!-- sourceRootFolder - The folder to refer to as the project source root (if specified, the folder will be zipped and deployed instead of the whole project), defaults to the root project folder -->
            <driver runFromLocalProject="true" waitForDebugger="true" sourceRootFolder="driver1">
                <!-- A list of paths to the driver's files or folders relative to the project's root.
                     may be a path to a directory, in which case all the files and folders under the directory are added into the driver's zip file.
                     if the <sources> element is not specified, all the files under the project are added to the driver's zip file -->
                <sources>
                     <source>driver1/drivermetadata.xml</source>
                     <source>driver1/qualidriver.py</source>
                     <source>driver1/SampleResourceDriver.py</source>
                     <source>driver1/requirements.txt</source>
                </sources>
                <!-- the driver name of the driver to update -->
                <targetName>driverToUpdate</targetName>
            </driver>
            <driver>
                <sources>
                    <source>driver2</source>
                </sources>
                <targetName>driverToUpdate2</targetName>
            </driver>
        </drivers>

        <-- The scripts to update, holds one or more scripts -->
        <scripts>
            <script>
                <!-- A list of paths to the script's files or folders relative to the project's root.
                    if the <sources> element is not specified, all the files under the project are added to the script's zip file.
                    if only one file is specified, the file will not be compressed into a zip file.
                 -->
                <sources>
                    <source>script1.py</source>
                </sources>
                 <!-- the script name of the script to update -->
                <targetName>scriptToUpdate</targetName>
            </script>
        </scripts>
    </properties>
    ```

4. Upon activation the plugin collects and zipps the contents of the project (excluding filtered files, see `deployment.xml` `fileFilters` property),
    the new zip is placed in a folder named `deployment/` in the project root.
    Then, the plugin uses the settings in the `deployment.xml` file to contact the server and update the driver files currently online.
