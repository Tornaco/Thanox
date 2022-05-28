package com.wakaztahir.codeeditor.prettify.lang


import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

class LangLasso : Lang() {

    companion object {
        val fileExtensions = listOf("lasso", "ls", "lassoscript")
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
            Prettify.PR_STRING,
            Regex("^\\'[^\\'\\\\]*(?:\\\\[\\s\\S][^\\'\\\\]*)*(?:\\'|$)"),
            null,
            "\'"
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"[^\\\"\\\\]*(?:\\\\[\\s\\S][^\\\"\\\\]*)*(?:\\\"|$)"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\`[^\\`]*(?:\\`|$)"),
            null,
            "`"
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^0x[\\da-f]+|\\d+", RegexOption.IGNORE_CASE),
            null,
            "0123456789"
        )
        shortcutStylePatterns.new(
            Prettify.PR_ATTRIB_NAME,
            Regex("^[#$][a-z_][\\w.]*|#\\d+\\b|#![ \\S]+lasso9\\b", RegexOption.IGNORE_CASE),
            null,
            "#$"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TAG,
            Regex(
                "^[\\[\\]]|<\\?(?:lasso(?:script)?|=)|\\?>|(no_square_brackets|noprocess)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\/[^\\r\\n]*|\\/\\*[\\s\\S]*?\\*\\/")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_ATTRIB_NAME,
            Regex(
                "^-(?!infinity)[a-z_][\\w.]*|\\.\\s*'[a-z_][\\w.]*'|\\.{3}",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\d*\\.\\d+(?:e[-+]?\\d+)?|(infinity|NaN)\\b", RegexOption.IGNORE_CASE)
        )
        fallthroughStylePatterns.new(
            Prettify.PR_ATTRIB_VALUE,
            Regex("^::\\s*[a-z_][\\w.]*", RegexOption.IGNORE_CASE)
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "^(?:true|false|none|minimal|full|all|void|and|or|not|bw|nbw|ew|new|cn|ncn|lt|lte|gt|gte|eq|neq|rx|nrx|ft)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex(
                "^(?:array|date|decimal|duration|integer|map|pair|string|tag|xml|null|boolean|bytes|keyword|list|locale|queue|set|stack|staticarray|local|var|variable|global|data|self|inherited|currentcapture|givenblock)\\b|^\\.\\.?",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD, Regex(
                "^(?:cache|database_names|database_schemanames|database_tablenames|define_tag|define_type|email_batch|encode_set|html_comment|handle|handle_error|header|if|inline|iterate|ljax_target|link|link_currentaction|link_currentgroup|link_currentrecord|link_detail|link_firstgroup|link_firstrecord|link_lastgroup|link_lastrecord|link_nextgroup|link_nextrecord|link_prevgroup|link_prevrecord|log|loop|namespace_using|output_none|portal|private|protect|records|referer|referrer|repeating|resultset|rows|search_args|search_arguments|select|sort_args|sort_arguments|thread_atomic|value_list|while|abort|case|else|fail_if|fail_ifnot|fail|if_empty|if_false|if_null|if_true|loop_abort|loop_continue|loop_count|params|params_up|return|return_value|run_children|soap_definetag|soap_lastrequest|soap_lastresponse|tag_name|ascending|average|by|define|descending|do|equals|frozen|group|handle_failure|import|in|into|join|let|match|max|min|on|order|parent|protected|provide|public|require|returnhome|skip|split_thread|sum|take|thread|to|trait|type|where|with|yield|yieldhome)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[a-z_][\\w.]*(?:=\\s*(?=\\())?", RegexOption.IGNORE_CASE)
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^:=|[-+*\\/%=<>&|!?\\\\]+")
        )


    }
}