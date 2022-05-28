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
 * This is similar to the lang-go.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for the Go language..
 *
 *
 * Based on the lexical grammar at
 * http://golang.org/doc/go_spec.html#Lexical_elements
 *
 *
 * Go uses a minimal style for highlighting so the below does not distinguish
 * strings, keywords, literals, etc. by design.
 * From a discussion with the Go designers:
 * <pre>
 * On Thursday, July 22, 2010, Mike Samuel <...> wrote:
 * > On Thu, Jul 22, 2010, Rob 'Commander' Pike <...> wrote:
 * >> Personally, I would vote for the subdued style godoc presents at http://golang.org
 * >>
 * >> Not as fancy as some like, but a case can be made it's the official style.
 * >> If people want more colors, I wouldn't fight too hard, in the interest of
 * >> encouragement through familiarity, but even then I would ask to shy away
 * >> from technicolor starbursts.
 * >
 * > Like http://golang.org/pkg/go/scanner/ where comments are blue and all
 * > other content is black?  I can do that.
</pre> *
 *
 * @author mikesamuel@gmail.com
 */
class LangGo : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("go")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // Whitespace is made up of spaces, tabs and newline characters.
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // Not escaped as a string.  See note on minimalism above.
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^(?:\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S])*(?:\\\"|$)|\\'(?:[^\\'\\\\]|\\\\[\\s\\S])+(?:\\'|$)|`[^`]*(?:`|$))"),
            null,
            "\"'"
        )
        // Block comments are delimited by /* and */.
        // Single-line comments begin with // and extend to the end of a line.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^(?:\\/\\/[^\\r\\n]*|\\/\\*[\\s\\S]*?\\*\\/)")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^(?:[^\\/\\\"\\'`]|\\/(?![\\/\\*]))+", RegexOption.IGNORE_CASE)
        )
    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}