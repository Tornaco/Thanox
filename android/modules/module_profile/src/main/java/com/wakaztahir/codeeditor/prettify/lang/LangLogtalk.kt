package com.wakaztahir.codeeditor.prettify.lang


import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new
import java.util.*

class LangLogtalk : Lang() {

    companion object {
        val fileExtensions = listOf("logtalk", "lgt")
    }

    override fun getFileExtensions(): List<String> = fileExtensions

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"(?:[^\\\"\\\\\\n\\x0C\\r]|\\\\[\\s\\S])*(?:\\\"|$)"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^[a-z][a-zA-Z0-9_]*")
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\'(?:[^\\'\\\\\\n\\x0C\\r]|\\\\[^&])+\\'?"),
            null,
            "\'"
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "^(?:0'.|0b[0-1]+|0o[0-7]+|0x[\\da-f]+|\\d+(?:\\.\\d+)?(?:e[+\\-]?\\d+)?)",
                RegexOption.IGNORE_CASE
            ),
            null,
            "0123456789"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^%[^\\r\\n]*"),
            null,
            "%"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[\\s\\S]*?\\*\\/")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\s*:-\\s(c(a(lls|tegory)|oinductive)|p(ublic|r(ot(ocol|ected)|ivate))|e(l(if|se)|n(coding|sure_loaded)|xport)|i(f|n(clude|itialization|fo))|alias|d(ynamic|iscontiguous)|m(eta_(non_terminal|predicate)|od(e|ule)|ultifile)|reexport|s(et_(logtalk|prolog)_flag|ynchronized)|o(bject|p)|use(s|_module))")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\s*:-\\s(e(lse|nd(if|_(category|object|protocol)))|built_in|dynamic|synchronized|threaded)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^[A-Z_][a-zA-Z0-9_]*")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[.,;{}:^<>=\\\\/+*?#!-]")
        )


    }
}