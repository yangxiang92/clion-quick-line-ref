package com.shawnyang.quicklineref

object ReferenceFormatter {
    fun format(path: String, startLine: Int?, endLine: Int?): String {
        if (startLine == null || endLine == null) {
            return "@$path"
        }

        return if (startLine == endLine) {
            "@$path#L$startLine"
        } else {
            "@$path#L$startLine-$endLine"
        }
    }
}
