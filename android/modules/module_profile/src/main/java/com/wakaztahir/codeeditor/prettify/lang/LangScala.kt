// Copyright (C) 2010 Google Inc.
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
 * This is similar to the lang-scala.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for Scala.
 *
 * Derived from http://lampsvn.epfl.ch/svn-repos/scala/scala-documentation/trunk/src/reference/SyntaxSummary.tex
 *
 * @author mikesamuel@gmail.com
 */
class LangScala : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("scala")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {

        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // A double or single quoted string 
        // or a triple double-quoted multi-line string.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\"(?:(?:\"\"(?:\"\"?(?!\")|[^\\\\\"]|\\\\.)*\"{0,3})|(?:[^\"\\r\\n\\\\]|\\\\.)*\"?))"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^`(?:[^\\r\\n\\\\`]|\\\\.)*`?"), null, "`"
        )
        shortcutStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[!#%&()*+,\\-:;<=>?@\\[\\\\\\]^{|}~]+"),
            null,
            "!#%&()*+,-:;<=>?@[\\\\]^{|}~"
        )
        // A symbol literal is a single quote followed by an identifier with no
        // single quote following
        // A character literal has single quotes on either side
        fallthroughStylePatterns.new(
            Prettify.PR_STRING, Regex("^'(?:[^\\r\\n\\\\']|\\\\(?:'|[^\\r\\n']+))'")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^'[a-zA-Z_$][\\w$]*(?!['$\\w])")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:abstract|case|catch|class|def|do|else|extends|final|finally|for|forSome|if|implicit|import|lazy|match|new|object|override|package|private|protected|requires|return|sealed|super|throw|trait|try|type|val|var|while|with|yield)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^(?:true|false|null|this)\\b")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex(
                "^(?:(?:0(?:[0-7]+|X[0-9A-F]+))L?|(?:(?:0|[1-9][0-9]*)(?:(?:\\.[0-9]+)?(?:E[+\\-]?[0-9]+)?F?|L?))|\\\\.[0-9]+(?:E[+\\-]?[0-9]+)?F?)",
                RegexOption.IGNORE_CASE
            )
        )
        // Treat upper camel case identifiers as types.
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE, Regex("^[\$_]*[A-Z][_\$A-Z0-9]*[a-z][\\w$]*")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\$a-zA-Z_][\\w$]*")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^\\/(?:\\/.*|\\*(?:\\/|\\**[^*/])*(?:\\*+\\/?)?)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^(?:\\.+|\\/)")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}