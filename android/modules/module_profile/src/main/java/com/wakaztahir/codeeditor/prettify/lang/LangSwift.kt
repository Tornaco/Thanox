package com.wakaztahir.codeeditor.prettify.lang


import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

class LangSwift : Lang() {


    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    companion object {
        val fileExtensions = listOf("swift")
    }

    override fun getFileExtensions(): List<String> = fileExtensions

    init {
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[ \\n\\r\\t\\v\\u000c\\\u0000]+"),
            null,
            " \\n\\r\\t\\v\\u000c\\0"
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"(?:[^\"\\\\]|(?:\\\\.)|(?:\\\\\\((?:[^\"\\\\)]|\\\\.)*\\)))*\""),
            null,
            "\""
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:(?:0x[\\da-fA-F][\\da-fA-F_]*\\.[\\da-fA-F][\\da-fA-F_]*[pP]?)|(?:\\d[\\d_]*\\.\\d[\\d_]*[eE]?))[+-]?\\d[\\d_]*"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^-?(?:(?:0(?:(?:b[01][01_]*)|(?:o[0-7][0-7_]*)|(?:x[\\da-fA-F][\\da-fA-F_]*)))|(?:\\d[\\d_]*))"),
            null

        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:_|Any|true|false|nil)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\b(?:__COLUMN__|__FILE__|__FUNCTION__|__LINE__|#available|#colorLiteral|#column|#else|#elseif|#endif|#file|#fileLiteral|#function|#if|#imageLiteral|#line|#selector|#sourceLocation|arch|arm|arm64|associatedtype|associativity|as|break|case|catch|class|continue|convenience|default|defer|deinit|didSet|do|dynamic|dynamicType|else|enum|extension|fallthrough|fileprivate|final|for|func|get|guard|import|indirect|infix|init|inout|internal|i386|if|in|iOS|iOSApplicationExtension|is|lazy|left|let|mutating|none|nonmutating|open|operator|optional|OSX|OSXApplicationExtension|override|postfix|precedence|prefix|private|protocol|Protocol|public|repeat|required|rethrows|return|right|safe|Self|self|set|static|struct|subscript|super|switch|throw|throws|try|Type|typealias|unowned|unsafe|var|weak|watchOS|where|while|willSet|x86_64)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\/.*?[\\n\\r]"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[\\s\\S]*?(?:\\*\\/|$)"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^<<=|<=|<<|>>=|>=|>>|===|==|\\.\\.\\.|&&=|\\.\\.<|!==|!=|&=|~=|~|\\(|\\)|\\[|\\]|\\{|}|@|#|;|\\.|,|:|\\|\\|=|\\?\\?|\\|\\||&&|&\\*|&\\+|&-|&=|\\+=|-=|\\/=|\\*=|\\^=|%=|\\|=|->|`|==|\\+\\+|--|\\/|\\+|!|\\*|%|<|>|&|\\||\\^|\\?|=|-|_"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^\\b(?:[@_]?[A-Z]+[a-z][A-Za-z_$@0-9]*|\\w+_t\\b)"),
            null
        )


    }
}