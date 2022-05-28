// Copyright (C) 2013 Andrew Allen
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
 * This is similar to the lang-erlang.js in JavaScript Prettify.
 *
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 *
 *
 *
 * Derived from https://raw.github.com/erlang/otp/dev/lib/compiler/src/core_parse.yrl
 * Modified from Mike Samuel's Haskell plugin for google-code-prettify
 *
 * @author achew22@gmail.com
 */
class LangErlang : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("erlang", "erl")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {


        // Whitespace
        // whitechar    ->    newline | vertab | space | tab | uniWhite
        // newline      ->    return linefeed | return | linefeed | formfeed
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("\\t\\n\\x0B\\x0C\\r ]+"), null, "\t\n" + 0x0B.toChar().toString() + 0x0C.toChar()
                .toString() + "\r "
        )
        // Single line double-quoted strings.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"(?:[^\\\"\\\\\\n\\x0C\\r]|\\\\[\\s\\S])*(?:\\\"|$)"),
            null,
            "\""
        )

        // Handle atoms
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL, Regex("^[a-z][a-zA-Z0-9_]*")
        )
        // Handle single quoted atoms
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\'(?:[^\\'\\\\\\n\\x0C\\r]|\\\\[^&])+\\'?"),
            null,
            "'"
        )

        // Handle macros. Just to be extra clear on this one, it detects the ?
        // then uses the regexp to end it so be very careful about matching
        // all the terminal elements
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^\\?[^ \\t\\n({]+"),
            null,
            "?"
        )

        // decimal      ->    digit{digit}
        // octal        ->    octit{octit}
        // hexadecimal  ->    hexit{hexit}
        // integer      ->    decimal
        //               |    0o octal | 0O octal
        //               |    0x hexadecimal | 0X hexadecimal
        // float        ->    decimal . decimal [exponent]
        //               |    decimal exponent
        // exponent     ->    (e | E) [+ | -] decimal
        shortcutStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "^(?:0o[0-7]+|0x[\\da-f]+|\\d+(?:\\.\\d+)?(?:e[+\\-]?\\d+)?)",
                RegexOption.IGNORE_CASE
            ),
            null,
            "0123456789"
        )


        // TODO: catch @declarations inside comments

        // Comments in erlang are started with % and go till a newline
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^%[^\\n\\r]*")
        )

        // Catch macros
        //[PR['PR_TAG'], /?[^( \n)]+/],
        /**
         * %% Keywords (atoms are assumed to always be single-quoted).
         * 'module' 'attributes' 'do' 'let' 'in' 'letrec'
         * 'apply' 'call' 'primop'
         * 'case' 'of' 'end' 'when' 'fun' 'try' 'catch' 'receive' 'after'
         */
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:module|attributes|do|let|in|letrec|apply|call|primop|case|of|end|when|fun|try|catch|receive|after|char|integer|float,atom,string,var)\\b")
        )
        /**
         * Catch definitions (usually defined at the top of the file)
         * Anything that starts -something
         */
        fallthroughStylePatterns.new(Prettify.PR_KEYWORD, Regex("^-[a-z_]+"))

        // Catch variables
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^[A-Z_][a-zA-Z0-9_]*")
        )

        // matches the symbol production
        fallthroughStylePatterns.new(Prettify.PR_PUNCTUATION, Regex("^[.,;]"))
    }

    override fun getFileExtensions(): List<String> = fileExtensions

}