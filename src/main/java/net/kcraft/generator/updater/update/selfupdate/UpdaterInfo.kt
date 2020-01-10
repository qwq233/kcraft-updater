//--------------------------------------------------
// Class UpdaterInfo
//--------------------------------------------------
// Written by Kenvix <i@kenvix.com>
//--------------------------------------------------

package net.kcraft.generator.updater.update.selfupdate

import net.kcraft.generator.meta.FileNode

data class UpdaterInfo(val versionCode: Int) {
    var versionName: String = "Unknown"
    var objectFile: FileNode? = null
}