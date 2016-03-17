package com.qualisystems.pythonDriverPlugin.archiveAnalizers;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by tom.h on 16/03/2016.
 */
public class NoItemsAnalyzer implements IArchiveAnalyzer {
    @Override
    public HashMap<String, String> getArcivedFiles() throws IOException {
        return new HashMap<>();
    }
}
