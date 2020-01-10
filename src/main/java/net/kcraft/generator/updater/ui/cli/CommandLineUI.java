//--------------------------------------------------
// Class CommandLineUI
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.ui.cli;

import net.kcraft.generator.Environment;
import net.kcraft.generator.meta.MetaResult;
import net.kcraft.generator.updater.repo.Repo;
import net.kcraft.generator.updater.ui.UpdaterUI;
import net.kcraft.generator.updater.update.*;

import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Consumer;

/**
 * Command Line UI and basic of GraphicalUI
 */
public class CommandLineUI implements UpdaterUI, Observer {
    private Scanner scanner;
    private UpdateEvent incomingMessage;

    protected void logln(String text) {
        System.out.println(text);
    }

    protected void log(String text) {
        System.out.print(text);
    }

    protected void logf(String format, Object... args) {
        System.out.printf(format, args);
    }

    /**
     * Display command line UI.
     */
    @Override
    public void display() {
        scanner = new Scanner(System.in);

        showWelcomePage();
        Repo selectedRepo = showRepoSelectPage();

        new Thread(() -> {
            try {
                UpdaterCore core = new UpdaterCore(selectedRepo);
                core.addObserver(CommandLineUI.this);
                core.start();
            } catch (UpdateCriticalException ex) {
                ex.printStackTrace();

                logln("更新失败：严重错误：" + ex.getMessage());

                if (ex.getOriginalException() != null)
                    logln(ex.getOriginalException().getMessage());

                System.exit(ex.getExitCode());
            }
        }).start();
    }

    private void waitMessage(UpdateEventType eventType, Consumer<UpdateEvent> callback) {

    }

    @Override
    public void update(Observable o, Object arg) {
        incomingMessage = (UpdateEvent) arg;
    }

    final protected void showUpdateFinishedPage(MetaResult remoteResult) {
        printBoldBorderLine();
        logln("更新 KCraft 客户端完成。KCraft 客户端已被安装到此文件夹下：");
        logln(Environment.getBasekcraftPath());
        logln("请打开上述文件夹，启动启动器，即可开始游戏。（启动器的启动方法见用户中心的客户端下载页）");
        logln("");
        logln("注意：如果你需要添加自定义 Mod, 请打开 Updater/Mods 文件夹(注意大小写), 并把你的 Mod 放入这个文件夹中, 然后再次运行更新器. 不要把 Mod 直接放在 .minecraft/mods 中, 否则它们会被删除.");

        printNormalBorderLine();
        logf("KCraft 版本号：%s // 发行时间：%s\n", remoteResult.getVersion(),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(new Date(remoteResult.getTime())));
        logln(remoteResult.getDescription());
    }

    final protected Repo[] showWelcomePage() throws UpdateCriticalException {
        printBoldBorderLine();

        logln("欢迎使用 KCraft 客户端更新器");
        logln("正在获取更新节点列表，请稍候 ...");

        Repo[] repos = Environment.getRepos();
        if (repos == null)
            throw new UpdateCriticalException("错误：无法获取更新节点，请检查您的网络", 7);

        return repos;
    }

    final protected Repo showRepoSelectPage() {
        printBoldBorderLine();
        Repo[] repos = Environment.getRepos();

        UserFileRegister.createUserModsDir();
        logln("说明: 该程序将于它所在的文件夹下的 KCraft 文件夹安装本客户端, 并删除该文件夹内的其他 Minecraft 版本. 请勿把安装器与无关文件此文件夹内, 否则, 使用者需自行承担可能发生的数据损失.");
        logln("注意: 如果你需要添加自定义 Mod, 请打开 Updater/Mods 文件夹(注意大小写), 并把你的 Mod 放入这个文件夹中. 不要把 Mod 直接放在 .minecraft/mods 中, 否则它们会被删除.");

        printNormalBorderLine();
        logln("请选择一个下载源 (输入序号)：");

        Arrays.stream(repos).forEach(repo -> logf("[%d] %s\n", repo.getOrder(), repo.getDescription()));
        log("请输入下载源的序号: ");

        int repoID;
        Repo selectedRepo;

        if (repos.length >= 2) {
            while (true) {
                try {
                    repoID = scanner.nextInt();
                    selectedRepo = repos[repoID];
                    break;
                } catch (InputMismatchException ex) {
                    scanner.next();
                    log("输入的内容无效，请重新输入下载源的序号: ");
                } catch (IndexOutOfBoundsException ex) {
                    log("输入的序号无效，请重新输入下载源的序号: ");
                }
            }
        } else {
            repoID = 0;
            selectedRepo = repos[0];
        }

        logln("使用节点：" + selectedRepo.getDescription());

        return selectedRepo;
    }

    private void printBoldBorderLine() {
        System.out.println("=========================================================");
    }

    private void printNormalBorderLine() {
        System.out.println("---------------------------------------------------------");
    }

    private void pause() {

    }
}
