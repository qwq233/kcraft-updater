package net.kcraft.generator.updater.repo;

/**
 *  * LocalIntegratedRepoManager
 *   * Fallback into this manager if all methods failed.
 *    */
public class LocalIntegratedRepoManager implements RepoManager {
	    @Override
	        public Repo[] getRepos() throws Exception {
			        return new Repo[]{
		                new Repo(0, "https://modpack.qwq2333.top/tech", "tech", "tech.json", "[推荐] KCraft 网页节点"),
				    //new Repo(1, "https://gitlab.com/Kenvix/moxbin/raw/master/object/", "gitlab", "kcraft.json", "GitLab 国外节点")
									    
				};
				}			
		}
