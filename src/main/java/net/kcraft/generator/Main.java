//--------------------------------------------------
// Class net.kcraft.generator.Main
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator;

import net.kcraft.generator.jsonengine.GeneratorEngine;
import net.kcraft.generator.jsonengine.ParserEngine;
import net.kcraft.generator.meta.GeneratorConfig;
import net.kcraft.generator.meta.MetaResult;
import net.kcraft.generator.meta.MetaScanner;
import net.kcraft.generator.meta.ObjectEngine;
import net.kcraft.generator.meta.scanner.CommonScanner;
import net.kcraft.generator.updater.ui.UpdaterUI;
import net.kcraft.generator.updater.update.selfupdate.SelfUpdateApplier;

import java.io.File;
import java.lang.reflect.Modifier;
import java.util.logging.Level;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {

        try {
            Init.initSystem(args);

            // SelfUpdateApplier selfUpdateApplier = new SelfUpdateApplier();
            // selfUpdateApplier.start("generator-1.0-SNAPSHOT.jar");

            String basePath = Environment.getBasekcraftPath();
            Environment.getLogger().log(Level.FINEST, "Current path: " + basePath);

            if (Environment.isUpdater())
                runAsUpdater();
            else
                runAsGenerator();
        } catch (NoClassDefFoundError ex) {
            Environment.showErrorMessage(
                    "KCraft-Toolbox 缺少必要的组件，无法启动。\n请确保你下载的是完整的客户端。尝试前往用户中心下载最新版本客户端\n\n" + ex.toString());
            System.exit(2);
        } catch (Throwable ex) {
            Environment.showErrorMessage("KCraft-Toolbox 遇到严重错误，即将退出：\n" + ex.toString());
            Environment.getLogger().log(Level.SEVERE, "Unexpected Exception.");
            ex.printStackTrace();
            System.exit(1);
        }
    }

    private static void runAsGenerator() throws Exception {
        File generatorConfigFile = Environment.getGeneratorConfigFile();
        GeneratorConfig.initialize(generatorConfigFile).startScan();

        GeneratorConfig config = GeneratorConfig.getInstance();
        MetaScanner scanner = new MetaScanner((CommonScanner) Environment.getMetaScanner().newInstance());

        if (!Environment.getBasekcraftDir().exists()) {
            Environment.getLogger().log(Level.SEVERE,
                    "generator_config.json not found on '" + generatorConfigFile.getCanonicalPath()
                            + "'. Please specify where generator_config.json is and run this program again.");
            System.exit(8);
        }

        MetaResult result = scanner.scan();
        result.setDescription(Environment.getUpdateDescription().isEmpty() ? config.getDescription()
                : Environment.getUpdateDescription());
        result.setVersion(
                Environment.getUpdateVersion().isEmpty() ? config.getVersion() : Environment.getUpdateVersion());

        if (config.getObjectSize() > 0) {
            Environment.getLogger().info("Generating objects....");
            ObjectEngine objectEngine = new ObjectEngine(result);
            objectEngine.startMakeObjects();
        }

        generateAll(result);
    }

    private static void runAsUpdater() throws Exception {
        // testParser(new NewMoeEngine(), result);
        Environment.getLogger().finest("Starting Updater UI Thread ...");

        UpdaterUI uiProvider = (UpdaterUI) Environment.getUiProvider().newInstance();
        uiProvider.display();
    }

    private static void generateAll(MetaResult result) throws Exception {
        for (Class engine : Environment.getGeneratorEngines()) {
            if (!Modifier.isAbstract(engine.getModifiers())) {
                Environment.getLogger().info("Generating result using " + engine.getSimpleName());

                GeneratorEngine instance = (GeneratorEngine) engine.newInstance();
                String generateResult = instance.encode(result);
                instance.save(generateResult);
                Environment.getLogger().log(Level.FINE, "Write result formatted in " + engine.getSimpleName() + "  to "
                        + Environment.getBasekcraftPath());
            } else {
                Environment.getLogger().info("Detected invalid generator engine: " + engine.getSimpleName());
            }
        }
    }

    private static <T extends GeneratorEngine & ParserEngine> void testParser(T instance, MetaResult result)
            throws Exception {
        String generateResult1 = instance.encode(result);
        MetaResult decodeResult = instance.decode(generateResult1);
        String generateResult2 = instance.encode(decodeResult);
        out.println(generateResult1);
        out.println(generateResult2);
        out.println(generateResult2.equals(generateResult1));
    }

}
