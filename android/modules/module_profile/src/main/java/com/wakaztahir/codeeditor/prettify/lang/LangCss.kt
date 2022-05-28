// Copyright (C) 2009 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

/**
 * This is similar to the lang-css.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for CSS.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-css"></pre>
 *
 *
 * http://www.w3.org/TR/CSS21/grammar.html Section G2 defines the lexical
 * grammar.  This scheme does not recognize keywords containing escapes.
 *
 * @author mikesamuel@gmail.com
 */
class LangCss : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("css")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {

        // The space production <s>
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[ \t\r\n\\f]+"),
            null,
            " \t\r\n\\f"
        )
        // Quoted strings.  <string1> and <string2>
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"(?:[^\n\r\\f\\\\\\\"]|\\\\(?:\r\n?|\n|\\f)|\\\\[\\s\\S])*\\\""),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\'(?:[^\n\r\\f\\\\\\']|\\\\(?:\r\n?|\n|\\f)|\\\\[\\s\\S])*\\'"),
            null
        )
        fallthroughStylePatterns.new(
            "lang-css-str",
            Regex("^url\\(([^\\)\\\"\\']+)\\)", RegexOption.IGNORE_CASE)
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex(
                "^(?:url|rgb|\\!important|@import|@page|@media|@charset|inherit)(?=[^\\-\\w]|$)",
                RegexOption.IGNORE_CASE
            ),
            null
        )
        // A property name -- an identifier followed by a colon.
        fallthroughStylePatterns.new(
            "lang-css-kw",
            Regex(
                "^(-?(?:[_a-z]|(?:\\\\[0-9a-f]+ ?))(?:[_a-z0-9\\-]|\\\\(?:\\\\[0-9a-f]+ ?))*)\\s*:",
                RegexOption.IGNORE_CASE
            )
        )
        // A C style block comment.  The <comment> production.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[^*]*\\*+(?:[^\\/*][^*]*\\*+)*\\/")
        )
        // Escaping text spans
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^(?:<!--|-->)")
        )
        // A number possibly containing a suffix.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:\\d+|\\d*\\.\\d+)(?:%|[a-z]+)?", RegexOption.IGNORE_CASE)
        )
        // A hex color
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^#(?:[0-9a-f]{3}){1,2}\\b", RegexOption.IGNORE_CASE)
        )
        // An identifier
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex(
                "^-?(?:[_a-z]|(?:\\\\[\\da-f]+ ?))(?:[_a-z\\d\\-]|\\\\(?:\\\\[\\da-f]+ ?))*",
                RegexOption.IGNORE_CASE
            )
        )
        // A run of punctuation
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[^\\s\\w\\'\\\"]+", RegexOption.IGNORE_CASE)
        )
    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }

    class LangCssKeyword : Lang() {
        companion object {
            val fileExtensions: List<String>
                get() = listOf("css-kw")
        }

        override val fallthroughStylePatterns = ArrayList<StylePattern>()
        override val shortcutStylePatterns = ArrayList<StylePattern>()

        init {
            fallthroughStylePatterns.new(
                Prettify.PR_KEYWORD,
                Regex(
                    "^-?(?:[_a-z]|(?:\\\\[\\da-f]+ ?))(?:[_a-z\\d\\-]|\\\\(?:\\\\[\\da-f]+ ?))*",
                    RegexOption.IGNORE_CASE
                )
            )
        }

        override fun getFileExtensions(): List<String> {
            return fileExtensions
        }
    }

    class LangCssString : Lang() {
        companion object {
            val fileExtensions: List<String> = listOf("css-str")
        }

        override val fallthroughStylePatterns = ArrayList<StylePattern>()
        override val shortcutStylePatterns = ArrayList<StylePattern>()

        init {
            fallthroughStylePatterns.new(
                Prettify.PR_STRING,
                Regex("^[^\\)\\\"\\']+")
            )
        }

        override fun getFileExtensions(): List<String> {
            return fileExtensions
        }
    }

}