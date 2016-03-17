package com.qualisystems.pythonDriverPlugin.updaters;

import java.util.Map;

/**
 * Created by tom.h on 15/03/2016.
 */
public interface IUpdater {
    void updateFiles(Map<String, String> arcivedDriverFiles) throws Exception;
}

