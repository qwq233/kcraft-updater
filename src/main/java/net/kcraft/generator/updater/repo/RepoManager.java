//--------------------------------------------------
// Interface RepoManager
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.repo;

@FunctionalInterface
public interface RepoManager {
    Repo[] getRepos() throws Exception;
}
