// Copyright (C) 2011 Zimin A.V.
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
 * This is similar to the lang-n.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for the Nemerle language.
 * http://nemerle.org
 * @author Zimin A.V.
 */
class LangN : Lang() {
    companion object {
        protected var keywords = ("abstract|and|as|base|catch|class|def|delegate|enum|event|extern|false|finally|"
                + "fun|implements|interface|internal|is|macro|match|matches|module|mutable|namespace|new|"
                + "null|out|override|params|partial|private|protected|public|ref|sealed|static|struct|"
                + "syntax|this|throw|true|try|type|typeof|using|variant|virtual|volatile|when|where|with|"
                + "assert|assert2|async|break|checked|continue|do|else|ensures|for|foreach|if|late|lock|new|nolate|"
                + "otherwise|regexp|repeat|requires|return|surroundwith|unchecked|unless|using|while|yield")
        val fileExtensions: List<String>
            get() = listOf("n", "nemerle")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\\'(?:[^\\\\\\'\\r\\n]|\\\\.)*\\'|\\\"(?:[^\\\\\\\"\\r\\n]|\\\\.)*(?:\\\"|$))"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^#(?:(?:define|elif|else|endif|error|ifdef|include|ifndef|line|pragma|undef|warning)\\b|[^\\r\\n]*)"),
            null,
            "#"
        )
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^\\s+"), null, " \r\n\t" + 0xA0.toChar().toString()
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^@\\\"(?:[^\\\"]|\\\"\\\")*(?:\\\"|$)"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^<#(?:[^#>])*(?:#>|$)"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^<(?:(?:(?:\\.\\.\\/)*|\\/?)(?:[\\w-]+(?:\\/[\\w-]+)+)?[\\w-]+\\.h|[a-z]\\w*)>"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\/[^\\r\\n]*"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^\\/\\*[\\s\\S]*?(?:\\*\\/|$)"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:" + keywords + ")\\\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^(?:array|bool|byte|char|decimal|double|float|int|list|long|object|sbyte|short|string|ulong|uint|ufloat|ulong|ushort|void)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^@[a-z_$][a-z_$@0-9]*", RegexOption.IGNORE_CASE),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^@[A-Z]+[a-z][A-Za-z_$@0-9]*"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^'?[A-Za-z_$][a-z_$@0-9]*", RegexOption.IGNORE_CASE),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex(
                "^(?:" // A hex number
                        + "0x[a-f0-9]+" // or an octal or decimal number,
                        + "|(?:\\\\d(?:_\\\\d+)*\\\\d*(?:\\\\.\\\\d*)?|\\\\.\\\\d\\\\+)" // possibly in scientific notation
                        + "(?:e[+\\\\-]?\\\\d+)?"
                        + ")" // with an optional modifier like UL for unsigned long
                        + "[a-z]*", RegexOption.IGNORE_CASE
            ), null, "0123456789"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^.[^\\s\\w\\.$@\\'\\\"\\`\\/\\#]*"),
            null
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}