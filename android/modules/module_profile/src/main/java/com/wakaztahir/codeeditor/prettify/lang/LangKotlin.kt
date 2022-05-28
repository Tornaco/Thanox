package com.wakaztahir.codeeditor.prettify.lang


import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

class LangKotlin : Lang() {

    companion object {
        val fileExtensions = listOf("kt", "kotlin")
    }

    override fun getFileExtensions(): List<String> = fileExtensions

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[\\t\\n\\r \\xA0]+"),
            null,
            "\\t\\n\\r \\xA0"
        )
        shortcutStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[.!%&()*+,\\-;<=>?\\[\\\\\\]^{|}:]+"),
            null,
            ".!%&()*+,-;<=>?[\\\\]^{|}:"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\b(package|public|protected|external|internal|private|open|abstract|constructor|final|override|import|for|while|as|typealias|get|set|((data|enum|annotation|sealed) )?class|this|super|val|var|fun|is|in|throw|return|break|continue|(companion )?object|if|try|else|do|when|init|interface|typeof)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:true|false|null)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(0[xX][0-9a-fA-F_]+L?|0[bB][0-1]+L?|[0-9_.]+([eE]-?[0-9]+)?[fFL]?)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^(\\b[A-Z]+[a-z][a-zA-Z0-9_$@]*|`.*`)"),
            null
        )
        fallthroughStylePatterns.new(Prettify.PR_COMMENT, Regex("^\\/\\/.*"))
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[\\s\\S]*?(?:\\*\\/|$)")
        )
        fallthroughStylePatterns.new(Prettify.PR_STRING, Regex("'.'"))
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"([^\"\\\\]|\\\\[\\s\\S])*\"")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"{3}[\\s\\S]*?[^\\\\]\"{3}")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^@([a-zA-Z0-9_$@]*|`.*`)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^[a-zA-Z0-9_]+@")
        )


    }
}