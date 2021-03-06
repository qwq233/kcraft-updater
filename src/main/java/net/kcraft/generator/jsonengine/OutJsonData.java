//--------------------------------------------------
// Class OutputJson
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.jsonengine;

import net.kcraft.generator.meta.MetaResult;

import java.io.IOException;

@FunctionalInterface
public interface OutJsonData {
    String encode(MetaResult result) throws IOException;
}
