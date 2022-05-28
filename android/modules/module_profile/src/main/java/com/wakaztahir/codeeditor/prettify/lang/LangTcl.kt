// Copyright (C) 2012 Pyrios.
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
 * This is similar to the lang-tcl.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-tcl">proc foo {} {puts bar}</pre>
 *
 * I copy-pasted lang-lisp.js, so this is probably not 100% accurate.
 * I used http://wiki.tcl.tk/1019 for the keywords, but tried to only
 * include as keywords that had more impact on the program flow
 * rather than providing convenience. For example, I included 'if'
 * since that provides branching, but left off 'open' since that is more
 * like a proc. Add more if it makes sense.
 *
 * @author pyrios@gmail.com
 */
class LangTcl : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("tcl")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {


        shortcutStylePatterns.new("opn", Regex("^\\{+"), null, "{")
        shortcutStylePatterns.new("clo", Regex("^\\}+"), null, "}")
        // A line comment that starts with ;
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^#[^\\r\\n]*"), null, "#"
        )
        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // A double quoted, possibly multi-line, string.
        shortcutStylePatterns.new(
            Prettify.PR_STRING, Regex("^\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S])*(?:\\\"|$)"), null, "\""
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:after|append|apply|array|break|case|catch|continue|error|eval|exec|exit|expr|for|foreach|if|incr|info|proc|return|set|switch|trace|uplevel|upvar|while)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex(
                "^[+\\-]?(?:[0#]x[0-9a-f]+|\\d+\\/\\d+|(?:\\.\\d+|\\d+(?:\\.\\d*)?)(?:[ed][+\\-]?\\d+)?)",
                RegexOption.IGNORE_CASE
            )
        )
        // A single quote possibly followed by a word that optionally ends with
        // = ! or ?.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^\\'(?:-*(?:\\w|\\\\[\\x21-\\x7e])(?:[\\w-]*|\\\\[\\x21-\\x7e])[=!?]?)?")
        )
        // A word that optionally ends with = ! or ?.
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^-*(?:[a-z_]|\\\\[\\x21-\\x7e])(?:[\\w-]*|\\\\[\\x21-\\x7e])[=!?]?")
        )
        // A printable non-space non-special character
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^[^\\w\\t\\n\\r \\xA0()\\\"\\\\\\';]+")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}