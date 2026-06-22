package com.shawnyang.quicklineref

import com.intellij.openapi.vfs.VirtualFile
import java.io.IOException
import java.nio.file.InvalidPathException

object PathResolver {
    fun resolveRealPath(file: VirtualFile): String? {
        if (!file.isInLocalFileSystem) {
            return null
        }

        val nioPath = try {
            file.toNioPath()
        } catch (_: UnsupportedOperationException) {
            return file.path
        } catch (_: InvalidPathException) {
            return file.path
        }

        return try {
            nioPath.toRealPath().toString()
        } catch (_: IOException) {
            file.path
        }
    }
}
