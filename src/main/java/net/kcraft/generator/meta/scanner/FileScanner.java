//--------------------------------------------------
// Class FileScanner
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.meta.scanner;

import net.kcraft.generator.Environment;
import net.kcraft.generator.meta.*;

import java.io.File;
import java.util.logging.Level;

public class FileScanner implements CommonScanner {
    private final static GeneratorConfig config = GeneratorConfig.getInstance();

    public MetaResult scan(File dir, MetaNodeType type, MetaScanner in) {
        MetaResult result = in.getResult();
        DirectoryNode directoryNode = result.addDirectoryNode(type, dir);
        Environment.getLogger().log(Level.FINER, "Scanning directory " + dir.getPath());
        scan(dir, directoryNode, true);
        return result;
    }

    public DirectoryNode scan(File dir, DirectoryNode parentNode, boolean isRootDirectory) {
        DirectoryNode directoryNode = isRootDirectory ? parentNode : parentNode.addDirectoryNode(dir);
        File[] list = dir.listFiles();
        if (list != null) {
            for (File file : list) {
                if (file.isFile()) {
                    if (!config.isFileExcluded(file)) {
                        Environment.getLogger().log(Level.FINEST, "+ File: " + file.getName());
                        directoryNode.addFileNode(file);
                    }
                } else {
                    if (!config.isDirectoryExcluded(file))
                        scan(file, directoryNode, false);
                }
            }
        } else {
            Environment.getLogger().warning("Failed to open directory " + dir.getPath());
        }
        return directoryNode;
    }
}
