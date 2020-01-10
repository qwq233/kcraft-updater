//--------------------------------------------------
// Enum DownloadEventAction
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.update.events;

public enum DownloadEventAction {
    CreateDirectory,
    FileIsCached,
    PreparingDownload,
    Downloading,
    DownloadFailed,
    DownloadProgressChanged,
}
