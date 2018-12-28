//--------------------------------------------------
// Class Environment
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.moecraft.generator;

import net.moecraft.generator.jsonengine.CommonEngine;
import net.moecraft.generator.jsonengine.engine.BalthildEngine;
import net.moecraft.generator.jsonengine.engine.NewMoeEngine;
import net.moecraft.generator.meta.GeneratorConfig;
import net.moecraft.generator.meta.scanner.FileScanner;
import org.apache.commons.cli.*;

import java.io.File;
import java.io.IOException;

public final class Environment {

    private final static Class[] generatorEngines = {BalthildEngine.class, NewMoeEngine.class};
    private static       File    baseMoeCraftDir;
    private final static Class[] parserEngines    = {NewMoeEngine.class};
    private static       File    generatorConfigFile;
    private static       String  baseMoeCraftPath;
    private static       String  updateDescription;
    private static       String  updateVersion;
    private final static Class metaScanner = FileScanner.class;

    static void loadEnvironment(CommandLine cmd) throws IOException {
        baseMoeCraftDir = new File(cmd.hasOption('p') ? cmd.getOptionValue('p') : "./MoeCraft");
        generatorConfigFile = new File(cmd.hasOption('c') ? cmd.getOptionValue('c') : "./generator_config.json");
        baseMoeCraftPath = baseMoeCraftDir.getCanonicalPath().replace('\\', '/');
        updateDescription = cmd.hasOption('i') ? cmd.getOptionValue('i') : "";
        updateVersion = cmd.hasOption('l') ? cmd.getOptionValue('l') : "1.0";
    }

    public static Class getMetaScanner() {
        return metaScanner;
    }

    public static Class[] getParserEngines() {
        return parserEngines;
    }

    public static Class[] getGeneratorEngines() {
        return generatorEngines;
    }

    public static File getBaseMoeCraftDir() {
        return baseMoeCraftDir;
    }

    public static File getGeneratorConfigFile() {
        return generatorConfigFile;
    }

    public static String getBaseMoeCraftPath() {
        return baseMoeCraftPath;
    }

    public static String getUpdateVersion() {
        return updateVersion;
    }

    public static void setUpdateVersion(String updateVersion) {
        Environment.updateVersion = updateVersion;
    }

    public static String getUpdateDescription() {
        return updateDescription;
    }

    public static void setUpdateDescription(String updateDescription) {
        Environment.updateDescription = updateDescription;
    }

}