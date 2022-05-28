package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

/**
 * Registers a language handler for markdown.
 *
 * @author Kirill Biakov (kbiakov@gmail.com)
 */
class LangMd : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("md", "markdown")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        fallthroughStylePatterns.new(
            Prettify.PR_DECLARATION,
            Regex("^#.*?[\\n\\r]")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^```[\\s\\S]*?(?:```|$)")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}