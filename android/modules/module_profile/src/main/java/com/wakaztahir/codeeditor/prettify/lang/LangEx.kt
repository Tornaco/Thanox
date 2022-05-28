package com.wakaztahir.codeeditor.prettify.lang


import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new


class LangEx : Lang() {

    companion object {
        val fileExtensions = listOf("ex", "exs")
    }

    override fun getFileExtensions(): List<String> = fileExtensions

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[\\t\\n\\r \\xA0]+"),
            null,
            "\t\n\r \\xA0"
        )
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^#.*"),
            null,
            "#"
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^'(?:[^'\\\\]|\\\\(?:.|\\n|\\r))*'?"),
            null,
            "\\'"
        )
        shortcutStylePatterns.new(
            Prettify.PR_ATTRIB_NAME,
            Regex("^@\\w+"),
            null,
            "@"
        )
        shortcutStylePatterns.new(

            Prettify.PR_PUNCTUATION,
            Regex("^[!%&()*+,\\-;<=>?\\[\\\\\\]^{|}]+"),
            null,
            "!%&()*+,-;<=>?[\\\\]^{|}"

        )
        shortcutStylePatterns.new(

            Prettify.PR_LITERAL,
            Regex("^(?:0o[0-7](?:[0-7]|_[0-7])*|0x[\\da-fA-F](?:[\\da-fA-F]|_[\\da-fA-F])*|\\d(?:\\d|_\\d)*(?:\\.\\d(?:\\d|_\\d)*)?(?:[eE][+\\-]?\\d(?:\\d|_\\d)*)?)"),
            null,
            "0123456789"

        )
        fallthroughStylePatterns.new(
            Prettify.PR_ATTRIB_NAME,
            Regex("^iex(?:\\(\\d+\\))?> ")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^::"),
            null

        )
        fallthroughStylePatterns.new(

            Prettify.PR_LITERAL,
            Regex("^:(?:\\w+[\\!\\?\\@]?|\"(?:[^\"\\\\]|\\\\.)*\"?)")
        )
        fallthroughStylePatterns.new(

            Prettify.PR_ATTRIB_NAME,
            Regex("^(?:__(?:CALLER|ENV|MODULE|DIR)__)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:alias|case|catch|def(?:delegate|exception|impl|macrop?|module|overridable|p?|protocol|struct)|do|else|end|fn|for|if|in|import|quote|raise|require|rescue|super|throw|try|unless|unquote(?:_splicing)?|use|when|with|yield)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:true|false|nil)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:\\w+[\\!\\?\\@]?|\"(?:[^\"\\\\]|\\\\.)*\"):(?!:)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"\"\"\\s*(\\r|\\n)+(?:\"\"?(?!\")|[^\\\\\"]|\\\\(?:.|\\n|\\r))*\"{0,3}")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"(?:[^\"\\\\]|\\\\(?:.|\\n|\\r))*\"?(?!\")")
        )
        fallthroughStylePatterns.new(Prettify.PR_TYPE, Regex("^[A-Z]\\w*"))
        fallthroughStylePatterns.new(Prettify.PR_COMMENT, Regex("^_\\w*"))
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[\$a-z]\\w*[\\!\\?]?")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_ATTRIB_VALUE,
            Regex(
                "^~[A-Z](?:\\/(?:[^\\/\\r\\n\\\\]|\\\\.)+\\/|\\|(?:[^\\|\\r\\n\\\\]|\\\\.)+\\||\"(?:[^\"\\r\\n\\\\]|\\\\.)+\"|'(?:[^'\\r\\n\\\\]|\\\\.)+')[A-Z]*",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_ATTRIB_VALUE,
            Regex(
                "^~[A-Z](?:\\((?:[^\\)\\r\\n\\\\]|\\\\.)+\\)|\\[(?:[^\\]\\r\\n\\\\]|\\\\.)+\\]|\\{(?:[^\\}\\r\\n\\\\]|\\\\.)+\\}|\\<(?:[^\\>\\r\\n\\\\]|\\\\.)+\\>)[A-Z]*",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^(?:\\.+|\\/|[:~])")
        )


    }
}