// Copyright (C) 2012 Jeffrey Arnold
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
 * This is similar to the lang-rd.js in JavaScript Prettify.
 *
 *
 * Support for R documentation (Rd) files
 *
 *
 * Minimal highlighting or Rd files, basically just highlighting
 * macros. It does not try to identify verbatim or R-like regions of
 * macros as that is too complicated for a lexer.  Descriptions of the
 * Rd format can be found
 * http://cran.r-project.org/doc/manuals/R-exts.html and
 * http://developer.r-project.org/parseRd.pdf.
 *
 * @author Jeffrey Arnold
 */
class LangRd : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("Rd", "rd")
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

        // special macros with no args
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\\\(?:cr|l?dots|R|tab)\\b")
        )
        // macros
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\\\[a-zA-Z@]+")
        )
        // highlighted as macros, since technically they are
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^#(?:ifn?def|endif)")
        )
        // catch escaped brackets
        fallthroughStylePatterns.new(Prettify.PR_PLAIN, Regex("^\\\\[{}]"))
        // punctuation
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[{}()\\[\\]]+")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}