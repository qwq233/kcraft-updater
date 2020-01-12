# KCraft-updater
KCraft Updater and Generator

# 协议
根据作者要求，本项目采用GPL 2.0 协议<br />
[授权图片](https://s2.ax1x.com/2020/01/10/l4nFpQ.png)

# 如何部署在您自己的服务器

## 1.准备工作

1.首先，您应该有一台能够正常访问的网页服务器，端口不定，但应当使用**HTTP Over TLS(HTTPS)协议**并且TLS协议至少应为**TLS 1.2**。<br />
2.您应该安装了**Java JDK**且版本至少为**1.8.0_221**以上。<br />
3.安装了git<br />
4.安装了IDEA(可选)(IDE)<br />
5.安装了Java Scene Builder(Javafx)<br />

## 2.修改文件
#### 1.```src\main\java\net\kcraft\generator\Environment.java```
```java
...
    private final static Class[] repoManager = { AccountCenterRepoManager.class, LocalIntegratedRepoManager.class };
    private final static String dnsRepoDomain = "updater-repo.moecraft.net";//不确定有何用处，故不作更改
    /*
    节点仓库，如果你是群组服或者有非同一时间同步多个客户端需求，你也可以把他当做客户端列表。
    节点仓库的示例文件您可以在wiki文件夹找到
    该文件应为json格式，且不应该有注释。地址则为您的网页服务器（即客户端文件存放服务器）
     */
    private static final String repoManagerURL = "https://modpack.qwq2333.top/repo";//节点仓库，如果你是群组服或者有非同一时间同步多个客户端需求，你也可以把他当做客户端列表。
    private final static String appName = "KCraft Toolbox"; //应用名，你应该根据个人需求修改
    private final static String outJsonName = "kcraft.json";//作为生成器使用时输出的json文件，可改可不改
...
```
除此之外，该文件还有一处需要更改。
```java
...
    static void loadEnvironment(final CommandLine cmd) throws IOException {
    ...
        basekcraftDir = new File(cmd.hasOption('p') ? cmd.getOptionValue('p') : "./KCraft");//客户端输出文件夹，你应该按自己的需求修改它
        generatorConfigFile = new File(cmd.hasOption('c') ? cmd.getOptionValue('c') : "./generator_config.json");
        basekcraftPath = basekcraftDir.getCanonicalPath().replace('\\', '/');
        updateDescription = cmd.hasOption('i') ? cmd.getOptionValue('i') : "";
        isUpdater = !cmd.hasOption('g');
        updateVersion = cmd.hasOption('l') ? cmd.getOptionValue('l') : "1.0";
    }
...
```

#### 2.```src\main\java\net\kcraft\generator\updater\repo\LocalIntegratedRepoManager.java```

```java
package net.kcraft.generator.updater.repo;

public class LocalIntegratedRepoManager implements RepoManager {
	    @Override
	        public Repo[] getRepos() throws Exception {
			        return new Repo[]{
					/*
					这里是如果未获取到节点列表默认显示的节点
			        第一个数字0代表了该节点的序号，序号也代表了显示顺序，该数字最小为0,最大未测试，但推荐不超过10
					第一个逗号后的内容是客户端下载地址的根目录。别忘了地址要用引号引住
					第二个逗号后的内容是名字。同上，别忘了引号
					第三个逗号后的内容是填写Environment.java时里面的生成器输出json
					第四个逗号后的内容是显示的信息
					*/
					new Repo(0, "https://modpack.qwq2333.top/tech", "tech", "tech.json", "[推荐] KCraft 格雷服")
					/*
					如上面的示例
					当未获取到节点列表时就会输出
					[0] [推荐] KCraft 格雷服
					并自动从https://modpack.qwq2333.top/tech下载tech.json与其他客户端文件
					*/
				};
				}
		}
```
#### 3.```src\main\java\net\kcraft\generator\updater\ui\cli\CommandLineUI```**（可选）**

此处为当以命令行模式(-cli)启动时输出的界面，一般用户不会用到此功能，根据个人需求修改即可。

#### 4.```src\main\resources\net\kcraft\generator\updater\ui\gui\nodeselect.fxml```与该文件夹下的favicon.png

此处为当以GUI模式下启动时输出的界面，使用Java Scene Builder按照需求编辑即可。

#### 5.网页服务器下的repo

此处为节点列表，要注意的是，json是不允许注释的，因此当您部署时需要删掉注释。

```json
[
	[
		0,//显示的顺序和序号
		"https:\/\/modpack.qwq2333.top\/tech\/",//客户端存放的目录
		"tech",//名字
		"kcraft.json",//填写Environment.java时里面的生成器输出json
		"[\u65b0\u73a9\u5bb6\u63a8\u8350]KCraft \u79d1\u6280\u670d"//输出的内容，要注意的是中文必须转换成Unicode编码
	],//当这不是最后一个节点时需要填写逗号，若是，则必须删去逗号
	[
		1,//显示的顺序和序号
		"https:\/\/modpack.qwq2333.top\/old\/",//客户端存放的目录
		"old",//名字
		"kcraft.json",//填写Environment.java时里面的生成器输出json
		"[\u767d\u540d\u5355]KCraft \u517b\u8001\u670d"//输出的内容，要注意的是中文必须转换成Unicode编码
	]//当这是最后一个节点时不应该填写逗号，若只有一个节点也如此。
]
```
至此，要修改的文件已经全部修改完成。

## 3.生成更新文件

生成器将会依照生成文件来生成客户端。本文简略介绍了生成器配置文件的书写方法。
示例的生成配置见wiki目录下的 `generator_config.json`。

#### 生成配置文件说明
```json
{
  "description": "kcraft 5 / Update 1", //版本说明，可以书写任意内容。
  "version": "5.1", //版本号，更新器不会将其作为检测依据，仅用作显示
  "object_size": 3145727, //文件对象分块阈值。超过此大小的文件将会被切成多个块，单位：字节
  "name_rule": "%s/%s-%d.txt", //object文件命名规则，请勿随意修改
  "synced_dirs": [ //强制同步的文件夹列表，位于这里的文件夹会被强制同步到与服务器相同的状态
    "HMCLData/Library", //请勿将 .minecraft/config 文件夹设为强制同步，否则用户运行更新器会丢失所有minecraft mod 设置。您应该单独设置需要同步哪些config文件
    ".minecraft/versions",
    ".minecraft/mods",
    ".minecraft/scripts",
    ".minecraft/config/cofh",
    ".minecraft/config/unidict"
  ],
  "synced_files": [ //强制同步的文件列表
    "Launcher.jar" //请在这里声明需要与服务器强制同步的config文件
  ],
  "default_files": [ //默认文件，仅当客户端没有这些文件时才从服务器上下载
    "HMCLData/hmcl.json"
  ],
  "excluded_files": [ //生成器会跳过符合这些特征的文件，可以使用通配符
    "*.class",
    "*.log"
  ],
  "excluded_dir": [ //生成器会跳过这些文件夹，不可以使用通配符
    ".minecraft/crash-reports",
    ".minecraft/logs",
    ".minecraft/saves",
    ".minecraft/journeymap",
    ".minecraft/local"
  ]
}
```

**注意：** json是不可以有注释的，您应当在编写完成后删除所有注释。

#### 使用方法
1. 将上述文件存放到与更新器相同的目录，然后打开命令提示符(终端)，运行：
```batch
java -jar 构建出的jar文件 -g -c 指定的配置文件
```

2. 打开 Deployment 文件夹，将此文件夹的内容直接全部上传到 填写repo时输入的目录。

# PGP签名

```
-----BEGIN PGP PUBLIC KEY BLOCK-----

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
=oSWY
-----END PGP PUBLIC KEY BLOCK-----
```


# 作者<br />
 编程：[kenvix](https://kenvix.com)<br />
 界面：newbieZBX<br />
 教程编写：[gao_cai_sheng](https://github.com/qwq233/)<br />




