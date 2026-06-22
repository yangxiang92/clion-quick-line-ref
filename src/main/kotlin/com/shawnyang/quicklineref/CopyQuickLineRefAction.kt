package com.shawnyang.quicklineref

import com.intellij.codeInsight.hint.HintManager
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.ide.CopyPasteManager
import com.intellij.openapi.project.DumbAwareAction
import com.intellij.openapi.wm.StatusBar
import java.awt.datatransfer.StringSelection

class CopyQuickLineRefAction : DumbAwareAction() {
    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR)
        event.presentation.isEnabledAndVisible = ReferenceBuilder.canBuildReference(editor)
    }

    override fun actionPerformed(event: AnActionEvent) {
        val editor = event.getData(CommonDataKeys.EDITOR) ?: return
        val result = ReferenceBuilder.buildReference(editor)

        val reference = result.reference
        if (reference == null) {
            val message = result.error ?: "无法生成代码引用"
            HintManager.getInstance().showErrorHint(editor, message)
            return
        }

        CopyPasteManager.getInstance().setContents(StringSelection(reference))
        StatusBar.Info.set("已复制代码引用: $reference", event.project)
    }
}
