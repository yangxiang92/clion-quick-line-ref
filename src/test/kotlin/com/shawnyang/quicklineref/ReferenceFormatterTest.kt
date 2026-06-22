package com.shawnyang.quicklineref

import kotlin.test.Test
import kotlin.test.assertEquals

class ReferenceFormatterTest {
    @Test
    fun `no selection only contains file path`() {
        val reference = ReferenceFormatter.format("/tmp/example.cpp", null, null)
        assertEquals("@/tmp/example.cpp", reference)
    }

    @Test
    fun `single line selection appends one line number`() {
        val reference = ReferenceFormatter.format("/tmp/example.cpp", 64, 64)
        assertEquals("@/tmp/example.cpp#L64", reference)
    }

    @Test
    fun `multi line selection appends line range`() {
        val reference = ReferenceFormatter.format("/tmp/example.cpp", 64, 70)
        assertEquals("@/tmp/example.cpp#L64-70", reference)
    }
}
