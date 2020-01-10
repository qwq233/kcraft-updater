//--------------------------------------------------
// Class CommonEngine
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.jsonengine.engine;

import com.kenvix.utils.FileTool;
import net.kcraft.generator.Environment;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class CommonEngine {
    protected static final String basePath = Environment.getBasekcraftPath();

    protected void writeJson(File target, String result) throws IOException {
        if (target.exists())
            if (!target.delete()) {
                Environment.getLogger().warning("Unable to delete: " + target.getName() + " . Generation failed");
                return;
            }
        FileWriter writer = new FileWriter(target);
        writer.write(result);
        writer.close();
    }

    protected String getRelativePath(String path) {
        return FileTool.getRelativePath(basePath, path);
    }
}
