// Copyright (C) 2011 Martin S.
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
 * This is similar to the lang-tex.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Support for tex highlighting as discussed on
 * [meta.tex.stackexchange.com](http://meta.tex.stackexchange.com/questions/872/text-immediate-following-double-backslashes-is-highlighted-as-macro-inside-a-code/876#876).
 *
 * @author Martin S.
 */
class LangTex : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("latex", "tex")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // all comments begin with '%'
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^%[^\\r\\n]*"),
            null,
            "%"
        )
        //[PR['PR_DECLARATION'], /^\\([egx]?def|(new|renew|provide)(command|environment))\b/],
        // any command starting with a \ and contains
        // either only letters (a-z,A-Z), '@' (internal macros)
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\\\[a-zA-Z@]+")
        )
        // or contains only one character
        fallthroughStylePatterns.new(Prettify.PR_KEYWORD, Regex("^\\\\."))
        // Highlight dollar for math mode and ampersam for tabular
        fallthroughStylePatterns.new(Prettify.PR_TYPE, Regex("^[$&]"))
        // numeric measurement values with attached units
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "[+-]?(?:\\.\\d+|\\d+(?:\\.\\d*)?)(cm|em|ex|in|pc|pt|bp|mm)",
                RegexOption.IGNORE_CASE
            )
        )
        // punctuation usually occurring within commands
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[{}()\\[\\]=]+")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}