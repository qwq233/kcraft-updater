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
