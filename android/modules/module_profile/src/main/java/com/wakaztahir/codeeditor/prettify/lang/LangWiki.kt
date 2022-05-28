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
 * This is similar to the lang-wiki.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for Wiki pages.
 *
 * Based on WikiSyntax at http://code.google.com/p/support/wiki/WikiSyntax
 *
 * @author mikesamuel@gmail.com
 */
class LangWiki : Lang() {

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()


    companion object {
        val fileExtensions: List<String>
            get() = listOf(("wiki"))
    }

    init {
        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[\\t \\xA0a-gi-z0-9]+"),
            null,
            "\t " + 0xA0.toChar().toString() + "abcdefgijklmnopqrstuvwxyz0123456789"
        )
        // Wiki formatting
        shortcutStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[=*~\\^\\[\\]]+"),
            null,
            "=*~^[]"
        )
        // Meta-info like #summary, #labels, etc.
        fallthroughStylePatterns.new(
            "lang-wiki.meta",
            Regex("(?:^^|\r\n?|\n)(#[a-z]+)\\b")
        )
        // A WikiWord
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:[A-Z][a-z][a-z0-9]+[A-Z][a-z][a-zA-Z0-9]+)\\b")
        )
        // A preformatted block in an unknown language
        fallthroughStylePatterns.new(
            "lang-",
            Regex("^\\{\\{\\{([\\s\\S]+?)\\}\\}\\}")
        )
        // A block of source code in an unknown language
        fallthroughStylePatterns.new("lang-", Regex("^`([^\r\n`]+)`"))
        // An inline URL.
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex(
                "^https?:\\/\\/[^\\/?#\\s]*(?:\\/[^?#\\s]*)?(?:\\?[^#\\s]*)?(?:#\\S*)?",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^(?:\r\n|[\\s\\S])[^#=*~^A-Zh\\{`\\[\r\n]*")
        )

    }

    override fun getFileExtensions(): List<String> = fileExtensions

    internal class LangWikiMeta : Lang() {
        companion object {
            val fileExtensions: List<String>
                get() = listOf(("wiki.meta"))
        }

        override val fallthroughStylePatterns = ArrayList<StylePattern>()
        override val shortcutStylePatterns = ArrayList<StylePattern>()

        init {

            val fallthroughStylePatterns: List<StylePattern> = ArrayList()
            shortcutStylePatterns.new(
                Prettify.PR_KEYWORD,
                Regex("^#[a-z]+", RegexOption.IGNORE_CASE),
                null,
                "#"
            )


        }

        override fun getFileExtensions(): List<String> {
            return fileExtensions
        }
    }
}