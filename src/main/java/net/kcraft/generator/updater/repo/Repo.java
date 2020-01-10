//--------------------------------------------------
// Class Repo
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.repo;

public class Repo implements Comparable<Repo>, Cloneable {
    private String url;
    private String name;
    private String description;
    private String metaFileName;
    private int order;

    public String getName() {
        return name;
    }

    public Repo(int order, String url, String name, String metaFileName, String description) {
        this.url = url.endsWith("/") ? url : url + "/";
        this.description = description;
        this.name = name;
        this.order = order;
        this.metaFileName = metaFileName;
    }

    public String getDescription() {
        return description;
    }

    public int getOrder() {
        return order;
    }

    public String getMetaFileName() {
        return metaFileName;
    }

    public String getUrl() {
        return url;
    }

    @Override
    public int compareTo(Repo o) {
        return Integer.compare(order, o.order);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
