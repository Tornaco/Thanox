// Copyright (C) 2013 Nikhil Dabas
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
 * This is similar to the lang-ml.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *  Registers a language handler for LLVM. From
 * https://gist.github.com/ndabas/2850418
 *
 *
 * To use, include prettify.js and this file in your HTML page. Then put your
 * code in an HTML tag like <pre class="prettyprint lang-llvm">(my LLVM code)</pre>
 *
 *
 * The regular expressions were adapted from:
 * https://github.com/hansstimer/llvm.tmbundle/blob/76fedd8f50fd6108b1780c51d79fbe3223de5f34/Syntaxes/LLVM.tmLanguage
 *
 * http://llvm.org/docs/LangRef.html#constants describes the language grammar.
 *
 * @author Nikhil Dabas
 */
class LangLlvm : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("llvm", "ll")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\t\n\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // A double quoted, possibly multi-line, string.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^!?\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S])*(?:\\\"|$)"),
            null,
            "\""
        )
        // comment.llvm
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^;[^\r\n]*"),
            null,
            ";"
        )
        // variable.llvm
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[%@!](?:[-a-zA-Z$._][-a-zA-Z$._0-9]*|\\d+)")
        )
        // According to http://llvm.org/docs/LangRef.html#well-formedness
        // These reserved words cannot conflict with variable names, because none of them start with a prefix character ('%' or '@').
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^[A-Za-z_][0-9A-Za-z_]*"),
            null
        )
        // constant.numeric.float.llvm
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^\\d+\\.\\d+")
        )
        // constant.numeric.integer.llvm
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^(?:\\d+|0[xX][a-fA-F0-9]+)")
        )
        // punctuation
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^[()\\[\\]{},=*<>:]|\\.\\.\\.$")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}