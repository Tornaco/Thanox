// Copyright (C) 2008 Google Inc.
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
 *
 * Registers a language handler for OCaml, SML, F# and similar languages.
 *
 * Based on the lexical grammar at
 * http://research.microsoft.com/en-us/um/cambridge/projects/fsharp/manual/spec.html#_Toc270597388
 *
 * @author mikesamuel@gmail.com
 */
class LangMl : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("fs", "ml")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // Whitespace is made up of spaces, tabs and newline characters.
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // #if ident/#else/#endif directives delimit conditional compilation
        // sections
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex(
                "^#(?:if[\\t\\n\\r \\xA0]+(?:[a-z_$][\\w\\']*|``[^\\r\\n\\t`]*(?:``|$))|else|endif|light)",
                RegexOption.IGNORE_CASE
            ),
            null,
            "#"
        )
        // A double or single quoted, possibly multi-line, string.
        // F# allows escaped newlines in strings.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S])*(?:\\\"|$)|\\'(?:[^\\'\\\\]|\\\\[\\s\\S])(?:\\'|$))"),
            null,
            "\"'"
        )
        // Block comments are delimited by (* and *) and may be
        // nested. Single-line comments begin with // and extend to
        // the end of a line.
        // TODO: (*...*) comments can be nested.  This does not handle that.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^(?:\\/\\/[^\\r\\n]*|\\(\\*[\\s\\S]*?\\*\\))")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:abstract|and|as|assert|begin|class|default|delegate|do|done|downcast|downto|elif|else|end|exception|extern|false|finally|for|fun|function|if|in|inherit|inline|interface|internal|lazy|let|match|member|module|mutable|namespace|new|null|of|open|or|override|private|public|rec|return|static|struct|then|to|true|try|type|upcast|use|val|void|when|while|with|yield|asr|land|lor|lsl|lsr|lxor|mod|sig|atomic|break|checked|component|const|constraint|constructor|continue|eager|event|external|fixed|functor|global|include|method|mixin|object|parallel|process|protected|pure|sealed|trait|virtual|volatile)\\b")
        )
        // A number is a hex integer literal, a decimal real literal, or in
        // scientific notation.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex(
                "^[+\\-]?(?:0x[\\da-f]+|(?:(?:\\.\\d+|\\d+(?:\\.\\d*)?)(?:e[+\\-]?\\d+)?))",
                RegexOption.IGNORE_CASE
            )
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^(?:[a-z_][\\w']*[!?#]?|``[^\\r\\n\\t`]*(?:``|$))", RegexOption.IGNORE_CASE)
        )
        // A printable non-space non-special character
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^[^\\t\\n\\r \\xA0\\\"\\'\\w]+")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}