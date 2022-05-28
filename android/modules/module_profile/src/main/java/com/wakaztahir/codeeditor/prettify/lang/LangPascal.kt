// Copyright (C) 2009 Onno Hommes.
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
 * This is similar to the lang-appollo.js in JavaScript Prettify.
 *
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 *
 * Registers a language handler for the AGC/AEA Assembly Language as described
 * at http://virtualagc.googlecode.com
 *
 *
 * This file could be used by goodle code to allow syntax highlight for
 * Virtual AGC SVN repository or if you don't want to commonize
 * the header for the agc/aea html assembly listing.
 *
 * @author ohommes@alumni.cmu.edu
 */
class LangPascal : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf(("pascal"))
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        // 'single-line-string'
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\\'(?:[^\\\\\\'\\r\\n]|\\\\.)*(?:\\'|$))"),
            null,
            "'"
        )
        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^\\s+"), null, " \r\n\t" + 0xA0.toChar().toString()
        )

        // A cStyleComments comment (* *) or {}
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\(\\*[\\s\\S]*?(?:\\*\\)|$)|^\\{[\\s\\S]*?(?:\\}|$)"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex(
                "^(?:ABSOLUTE|AND|ARRAY|ASM|ASSEMBLER|BEGIN|CASE|CONST|CONSTRUCTOR|DESTRUCTOR|DIV|DO|DOWNTO|ELSE|END|EXTERNAL|FOR|FORWARD|FUNCTION|GOTO|IF|IMPLEMENTATION|IN|INLINE|INTERFACE|INTERRUPT|LABEL|MOD|NOT|OBJECT|OF|OR|PACKED|PROCEDURE|PROGRAM|RECORD|REPEAT|SET|SHL|SHR|THEN|TO|TYPE|UNIT|UNTIL|USES|VAR|VIRTUAL|WHILE|WITH|XOR)\\b",
                RegexOption.IGNORE_CASE
            ),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^(?:true|false|self|nil)", RegexOption.IGNORE_CASE),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[a-z][a-z0-9]*", RegexOption.IGNORE_CASE),
            null
        )
        // Literals .0, 0, 0.0 0E13
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "^(?:\\$[a-f0-9]+|(?:\\d+(?:\\.\\d*)?|\\.\\d+)(?:e[+\\-]?\\d+)?)",
                RegexOption.IGNORE_CASE
            ),
            null,
            "0123456789"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^.[^\\s\\w\\.$@\\'\\/]*"),
            null
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}