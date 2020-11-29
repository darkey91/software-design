package ru.akirakozov.sd.refactoring.html

import java.lang.StringBuilder

class HtmlStringBuilder {
    private val stringBuilder = StringBuilder("<html><body>\n")

    fun firstHeader(header: String): HtmlStringBuilder {
        stringBuilder.appendLine("<h1>$header</h1>")
        return this
    }

    fun breakLine(str: String = ""): HtmlStringBuilder {
        stringBuilder.appendLine("$str</br>")
        return this
    }

    fun line(str: String = ""): HtmlStringBuilder {
        stringBuilder.appendLine(str)
        return this
    }

    override fun toString(): String =
        stringBuilder.append("</body></html>").toString()
}