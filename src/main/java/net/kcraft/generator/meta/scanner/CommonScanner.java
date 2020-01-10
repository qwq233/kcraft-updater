//--------------------------------------------------
// Interface CommonScanner
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.meta.scanner;

import net.kcraft.generator.meta.DirectoryNode;
import net.kcraft.generator.meta.MetaNodeType;
import net.kcraft.generator.meta.MetaResult;
import net.kcraft.generator.meta.MetaScanner;

import java.io.File;

public interface CommonScanner {
    MetaResult scan(File dir, MetaNodeType type, MetaScanner in);

    DirectoryNode scan(File dir, DirectoryNode parentNode, boolean isRootDirectory);
}
