/**
 * @license Copyright (C) 2013 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

/**
 * This is similar to the lang-dart.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for Dart.
 *
 *
 * Loosely structured based on the DartLexer in Pygments: http://pygments.org/.
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-dart">(Dart code)</pre>
 *
 * @author armstrong.timothy@gmail.com
 */
class LangDart : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf(("dart"))
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        // Whitespace.
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\t\n\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )

        // Script tag.
        fallthroughStylePatterns.new(Prettify.PR_COMMENT, Regex("^#!(?:.*)"))
        // `import`, `library`, `part of`, `part`, `as`, `show`, and `hide`
        // keywords.
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\b(?:import|library|part of|part|as|show|hide)\\b", RegexOption.IGNORE_CASE)
        )
        // Single-line comments.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\/(?:.*)")
        )
        // Multiline comments.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[^*]*\\*+(?:[^\\/*][^*]*\\*+)*\\/")
        )
        // `class` and `interface` keywords.
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\b(?:class|interface)\\b", RegexOption.IGNORE_CASE)
        )
        // General keywords.
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex(
                "^\\b(?:assert|break|case|catch|continue|default|do|else|finally|for|if|in|is|new|return|super|switch|this|throw|try|while)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        // Declaration keywords.
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex(
                "^\\b(?:abstract|const|extends|factory|final|get|implements|native|operator|set|static|typedef|var)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        // Keywords for types.
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex(
                "^\\b(?:bool|double|Dynamic|int|num|Object|String|void)\\b",
                RegexOption.IGNORE_CASE
            )
        )
        // Keywords for constants.
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("\\b(?:false|null|true)\\b", RegexOption.IGNORE_CASE)
        )
        // Multiline strings, single- and double-quoted.
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^r?[\\']{3}[\\s|\\S]*?[^\\\\][\\']{3}")
        )

        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^r?[\\\"]{3}[\\s|\\S]*?[^\\\\][\\\"]{3}")
        )

        // Normal and raw strings, single- and double-quoted.
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^r?\\'(\\'|(?:[^\\n\\r\\f])*?[^\\\\]\\')")
        )

        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^r?\\\"(\\\"|(?:[^\\n\\r\\f])*?[^\\\\]\\\")")
        )
        // Identifiers.
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[a-z_$][a-z0-9_]*", RegexOption.IGNORE_CASE)
        )
        // Operators.
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[~!%^&*+=|?:<>/-]")
        )
        // Hex numbers.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\b0x[0-9a-f]+", RegexOption.IGNORE_CASE)
        )
        // Decimal numbers.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\b\\d+(?:\\.\\d*)?(?:e[+-]?\\d+)?", RegexOption.IGNORE_CASE)
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\b\\.\\d+(?:e[+-]?\\d+)?", RegexOption.IGNORE_CASE)
        )
        // Punctuation.
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[(){}\\[\\],.;]")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}