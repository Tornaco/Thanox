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
 * This is similar to the lang-hs.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for Haskell.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-hs">(my lisp code)</pre>
 * The lang-cl class identifies the language as common lisp.
 * This file supports the following language extensions:
 * lang-cl - Common Lisp
 * lang-el - Emacs Lisp
 * lang-lisp - Lisp
 * lang-scm - Scheme
 *
 *
 * I used http://www.informatik.uni-freiburg.de/~thiemann/haskell/haskell98-report-html/syntax-iso.html
 * as the basis, but ignore the way the ncomment production nests since this
 * makes the lexical grammar irregular.  It might be possible to support
 * ncomments using the lookbehind filter.
 *
 *
 * @author mikesamuel@gmail.com
 */
class LangHs : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf(("hs"))
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {

        // Whitespace
        // whitechar    ->    newline | vertab | space | tab | uniWhite
        // newline      ->    return linefeed | return | linefeed | formfeed
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\x0B\\x0C\\r ]+"), null, "\t\n" + 0x0B.toChar()
                .toString() + 0x0C.toChar().toString() + "\r "
        )
        // Single line double and single-quoted strings.
        // char         ->    ' (graphic<' | \> | space | escape<\&>) '
        // string       ->    " {graphic<" | \> | space | escape | gap}"
        // escape       ->    \ ( charesc | ascii | decimal | o octal
        //                        | x hexadecimal )
        // charesc      ->    a | b | f | n | r | t | v | \ | " | ' | &
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"(?:[^\\\"\\\\\\n\\x0C\\r]|\\\\[\\s\\S])*(?:\\\"|$)"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\'(?:[^\\'\\\\\\n\\x0C\\r]|\\\\[^&])\\'?"),
            null,
            "'"
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
        // Haskell does not have a regular lexical grammar due to the nested
        // ncomment.
        // comment      ->    dashes [ any<symbol> {any}] newline
        // ncomment     ->    opencom ANYseq {ncomment ANYseq}closecom
        // dashes       ->    '--' {'-'}
        // opencom      ->    '{-'
        // closecom     ->    '-}'
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^(?:(?:--+(?:[^\\r\\n\\x0C]*)?)|(?:\\{-(?:[^-]|-+[^-\\}])*-\\}))")
        )
        // reservedid   ->    case | class | data | default | deriving | do
        //               |    else | if | import | in | infix | infixl | infixr
        //               |    instance | let | module | newtype | of | then
        //               |    type | where | _
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:case|class|data|default|deriving|do|else|if|import|in|infix|infixl|infixr|instance|let|module|newtype|of|then|type|where|_)(?=[^a-zA-Z0-9\\']|$)"),
            null
        )
        // qvarid       ->    [ modid . ] varid
        // qconid       ->    [ modid . ] conid
        // varid        ->    (small {small | large | digit | ' })<reservedid>
        // conid        ->    large {small | large | digit | ' }
        // modid        ->    conid
        // small        ->    ascSmall | uniSmall | _
        // ascSmall     ->    a | b | ... | z
        // uniSmall     ->    any Unicode lowercase letter
        // large        ->    ascLarge | uniLarge
        // ascLarge     ->    A | B | ... | Z
        // uniLarge     ->    any uppercase or titlecase Unicode letter
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^(?:[A-Z][\\w\\']*\\.)*[a-zA-Z][\\w\\']*")
        )
        // matches the symbol production
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[^\\t\\n\\x0B\\x0C\\r a-zA-Z0-9\\'\\\"]+")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}