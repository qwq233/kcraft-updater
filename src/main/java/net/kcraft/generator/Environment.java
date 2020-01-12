//--------------------------------------------------
// Class Environment
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator;

import net.kcraft.generator.jsonengine.engine.BalthildEngine;
import net.kcraft.generator.jsonengine.engine.NewMoeEngine;
import net.kcraft.generator.meta.scanner.FileScanner;
import net.kcraft.generator.updater.repo.AccountCenterRepoManager;
import net.kcraft.generator.updater.repo.LocalIntegratedRepoManager;
import net.kcraft.generator.updater.repo.Repo;
import net.kcraft.generator.updater.repo.RepoManager;
import net.kcraft.generator.updater.ui.cli.CommandLineUI;
import net.kcraft.generator.updater.ui.gui.FXGraphicalUI;
import org.apache.commons.cli.CommandLine;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.RuntimeMXBean;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Logger;



public final class Environment {

    private static CommandLine cmd;
    private final static Class[] generatorEngines = { BalthildEngine.class, NewMoeEngine.class };
    private static File basekcraftDir;
    private final static Class[] parserEngines = { NewMoeEngine.class };
    private static File generatorConfigFile;
    private static String basekcraftPath;
    private static String updateDescription;
    private static String updateVersion;
    private final static Class metaScanner = FileScanner.class;
    private final static Class[] repoManager = { AccountCenterRepoManager.class, LocalIntegratedRepoManager.class };
    private final static String dnsRepoDomain = "updater-repo.moecraft.net";//不确定有何用处，故不作更改
    /*
    节点仓库，如果你是群组服或者有非同一时间同步多个客户端需求，你也可以把他当做客户端列表。
    节点仓库的示例文件您可以在wiki文件夹找到
    该文件应为json格式，且不应该有注释。地址则为您的网页服务器（即客户端文件存放服务器）
     */
    private static final String repoManagerURL = "https://modpack.qwq2333.top/repo";
    private final static String appName = "KCraft Toolbox"; //应用名，你应该根据个人需求修改
    private final static String outJsonName = "kcraft.json";//作为生成器使用时输出的json文件，可改可不改
    private static Class uiProvider;
    private static Repo[] repos;
    private final static int downloadMaxTries = 5;
    private final static int dnsMaxTries = 20;
    private static Logger logger;
    private static boolean isUpdater;
    private static Path basePath;
    private static Path deployPath;
    private static Path updaterPath;
    private static Path cachePath;
    private static Path updaterObjectPath;
    private static Path userModsPath;
    private static boolean isConsoleWindowExists;
    private static boolean isRunningOnWindowsPlatform;
    private static int jvmPid = -1;

    static void loadEnvironment(final CommandLine cmd) throws IOException {
        Environment.cmd = cmd;

        uiProvider = cmd.hasOption("cli") ? CommandLineUI.class : FXGraphicalUI.class;

        basekcraftDir = new File(cmd.hasOption('p') ? cmd.getOptionValue('p') : "./KCraft");//客户端输出文件夹，你应该按自己的需求修改它
        generatorConfigFile = new File(cmd.hasOption('c') ? cmd.getOptionValue('c') : "./generator_config.json");
        basekcraftPath = basekcraftDir.getCanonicalPath().replace('\\', '/');
        updateDescription = cmd.hasOption('i') ? cmd.getOptionValue('i') : "";
        isUpdater = !cmd.hasOption('g');
        updateVersion = cmd.hasOption('l') ? cmd.getOptionValue('l') : "1.0";

        basePath = Paths.get(".");
        updaterPath = basePath.resolve("Updater");
        cachePath = updaterPath.resolve("Cache");
        deployPath = basePath.resolve("Deployment");
        updaterObjectPath = updaterPath.resolve("Objects");
        userModsPath = updaterPath.resolve("Mods");
        isConsoleWindowExists = System.console() != null;
        isRunningOnWindowsPlatform = System.getProperties().getProperty("os.name").toUpperCase().contains("WINDOWS");
    }

    public static int getDnsMaxTries() {
        return dnsMaxTries;
    }

    public static int getDownloadMaxTries() {
        return downloadMaxTries;
    }

    public static boolean isUpdater() {
        return isUpdater;
    }

    @NotNull
    public static Logger getLogger() {
        if (logger == null) {
            synchronized (Environment.class) {
                if (logger != null)
                    return logger;

                logger = Logger.getGlobal();
            }
        }
        return logger;
    }

    public static Path getUpdaterPath() {
        return updaterPath;
    }

    public static String getRepoManagerURL() {
        return repoManagerURL;
    }

    public static boolean isIsConsoleWindowExists() {
        return isConsoleWindowExists;
    }

    public static Path getCachePath() {
        return cachePath;
    }

    public static Path getBasePath() {
        return basePath;
    }

    public static Class getUiProvider() {
        return uiProvider;
    }

    public static Path getUpdaterObjectPath() {
        return updaterObjectPath;
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

    public static File getBasekcraftDir() {
        return basekcraftDir;
    }

    public static File getGeneratorConfigFile() {
        return generatorConfigFile;
    }

    public static String getBasekcraftPath() {
        return basekcraftPath;
    }

    public static String getUpdateVersion() {
        return updateVersion;
    }

    public static void setUpdateVersion(final String updateVersion) {
        Environment.updateVersion = updateVersion;
    }

    public static String getUpdateDescription() {
        return updateDescription;
    }

    public static void setUpdateDescription(final String updateDescription) {
        Environment.updateDescription = updateDescription;
    }

    public static String getDnsRepoDomain() {
        return dnsRepoDomain;
    }

    public static String getOutJsonName() {
        return outJsonName;
    }

    public static Path getDeployPath() {
        return deployPath;
    }

    public static Class[] getRepoManager() {
        return repoManager;
    }

    public static boolean isGeneratorMode() {
        return cmd.hasOption('g');
    }

    public static CommandLine getCommandLine() {
        return cmd;
    }

    public static String getAppName() {
        return appName;
    }

    public static Repo[] getRepos() {
        if (repos == null) {
            final Class[] repoManagers = Environment.getRepoManager();
            for (final Class repoManager : repoManagers) {
                try {
                    return repos = ((RepoManager) repoManager.newInstance()).getRepos();
                } catch (final Exception ex) {
                    Environment.getLogger()
                            .warning("Repo manager " + repoManager.getSimpleName() + " Failed! Fallback...");
                }
            }
        }
        return repos;
    }

    public static boolean isRunningOnWindowsPlatform() {
        return isRunningOnWindowsPlatform;
    }

    public static void setRepos(final Repo[] repos) {
        Environment.repos = repos;
    }

    public static Path getUserModsPath() {
        return userModsPath;
    }

    public static void showErrorMessage(final String message) {
        if (!isConsoleWindowExists) {
            JOptionPane.showMessageDialog(null, message, "错误", JOptionPane.ERROR_MESSAGE);
        }

        System.err.println(message);
    }

    /**
     * Get JVM PID
     *
     * @return int JVM PID
     * @throws UnsupportedOperationException Getting PID is not support on current
     *                                       JVM
     */
    @SuppressWarnings("all")
    public static int getJvmPid() {
        if (jvmPid != -1)
            return jvmPid;

        final RuntimeMXBean runtime = ManagementFactory.getRuntimeMXBean();
        try {
            final java.lang.reflect.Field jvm = runtime.getClass().getDeclaredField("jvm");
            jvm.setAccessible(true);

            final sun.management.VMManagement mgmt = (sun.management.VMManagement) jvm.get(runtime);
            final java.lang.reflect.Method pid_method = mgmt.getClass().getDeclaredMethod("getProcessId");
            pid_method.setAccessible(true);

            return jvmPid = (Integer) pid_method.invoke(mgmt);
        } catch (final Throwable ignored1) {
            // Fallback
            final String jvmName = runtime.getName();
            final String pidString = jvmName.split("@")[0];

            if (pidString == null || pidString.isEmpty())
                throw new UnsupportedOperationException();

            try {
                return jvmPid = Integer.parseInt(pidString);
            } catch (final NumberFormatException exception) {
                throw new UnsupportedOperationException();
            }
        }
    }

    public static String getJvmPath(final boolean useJavaW) {
        if (isRunningOnWindowsPlatform)
            return System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator
                    + (useJavaW ? "javaw.exe" : "java.exe");
        else
            return System.getProperties().getProperty("java.home") + File.separator + "bin" + File.separator + "java";
    }

    public static String getJvmPath() {
        return getJvmPath(false);
    }
}
