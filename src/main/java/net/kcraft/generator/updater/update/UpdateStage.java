//--------------------------------------------------
// Enum UpdateStage
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.update;

public enum UpdateStage {
    Prepare,
    DownloadMeta,
    ScanLocalFile,
    Compare,
    DownloadNewFile,
    MergeObject,
    ApplyNewFile,
    RegisterUserMod,
    CleanCache,
    Finish
}
