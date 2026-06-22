package com.shawnyang.quicklineref

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import kotlin.math.max

object ReferenceBuilder {
    data class BuildResult(
        val reference: String? = null,
        val error: String? = null,
    )

    fun canBuildReference(editor: Editor?): Boolean {
        if (editor == null) {
            return false
        }

        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return false
        return PathResolver.resolveRealPath(file) != null
    }

    fun buildReference(editor: Editor): BuildResult {
        val file = FileDocumentManager.getInstance().getFile(editor.document)
            ?: return BuildResult(error = "当前文档不是本地文件，无法生成代码引用")

        val realPath = PathResolver.resolveRealPath(file)
            ?: return BuildResult(error = "当前文档没有可用的真实磁盘路径")

        val selectionModel = editor.selectionModel
        if (!selectionModel.hasSelection()) {
            return BuildResult(reference = ReferenceFormatter.format(realPath, null, null))
        }

        val document = editor.document
        val startOffset = selectionModel.selectionStart
        val endOffsetExclusive = selectionModel.selectionEnd
        val normalizedEndOffset = max(startOffset, endOffsetExclusive - 1)

        val startLine = document.getLineNumber(startOffset) + 1
        val endLine = document.getLineNumber(normalizedEndOffset) + 1

        return BuildResult(reference = ReferenceFormatter.format(realPath, startLine, endLine))
    }
}
