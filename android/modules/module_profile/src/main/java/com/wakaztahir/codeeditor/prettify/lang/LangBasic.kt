// Contributed by peter dot kofler at code minus cop dot org
package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

/**
 * This is similar to the lang-basic.js in JavaScript Prettify.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-basic">(my BASIC code)</pre>
 *
 * @author peter dot kofler at code minus cop dot org
 */
class LangBasic : Lang() {

    companion object {
        val fileExtensions = listOf("basic", "cbm")
    }

    override fun getFileExtensions(): List<String> = fileExtensions

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        // "single-line-string"
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\"(?:[^\\\\\"\\r\\n]|\\\\.)*(?:\"|$))"),
            null,
            "\""
        )
        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^\\s+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )

        // A line comment that starts with REM
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^REM[^\\r\\n]*"), null
        )

        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\b(?:AND|CLOSE|CLR|CMD|CONT|DATA|DEF ?FN|DIM|END|FOR|GET|GOSUB|GOTO|IF|INPUT|LET|LIST|LOAD|NEW|NEXT|NOT|ON|OPEN|OR|POKE|PRINT|READ|RESTORE|RETURN|RUN|SAVE|STEP|STOP|SYS|THEN|TO|VERIFY|WAIT)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[A-Z][A-Z0-9]?(?:\\$|%)?", RegexOption.IGNORE_CASE), null
        )
        // Literals .0, 0, 0.0 0E13
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:e[+\\-]?\\d+)?", RegexOption.IGNORE_CASE),
            null,
            "0123456789"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^.[^\\s\\w\\.$%\"]*"), null
        )
    }
}