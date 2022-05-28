// Copyright (C) 2011 Kitware Inc.
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
 * This is similar to the lang-mumps.js in JavaScript Prettify.
 *
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-mumps">(my SQL code)</pre>
 *
 *
 * Commands, intrinsic functions and variables taken from ISO/IEC 11756:1999(E)
 *
 * @author chris.harris@kitware.com
 *
 *
 * Known issues:
 *
 *
 * - Currently can't distinguish between keywords and local or global variables having the same name
 * for exampe SET IF="IF?"
 * - m file are already used for MatLab hence using mumps.
 */
class LangMumps : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("mumps")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {


        val commands = "B|BREAK|" +
                "C|CLOSE|" +
                "D|DO|" +
                "E|ELSE|" +
                "F|FOR|" +
                "G|GOTO|" +
                "H|HALT|" +
                "H|HANG|" +
                "I|IF|" +
                "J|JOB|" +
                "K|KILL|" +
                "L|LOCK|" +
                "M|MERGE|" +
                "N|NEW|" +
                "O|OPEN|" +
                "Q|QUIT|" +
                "R|READ|" +
                "S|SET|" +
                "TC|TCOMMIT|" +
                "TRE|TRESTART|" +
                "TRO|TROLLBACK|" +
                "TS|TSTART|" +
                "U|USE|" +
                "V|VIEW|" +
                "W|WRITE|" +
                "X|XECUTE"
        val intrinsicVariables = "D|DEVICE|" +
                "EC|ECODE|" +
                "ES|ESTACK|" +
                "ET|ETRAP|" +
                "H|HOROLOG|" +
                "I|IO|" +
                "J|JOB|" +
                "K|KEY|" +
                "P|PRINCIPAL|" +
                "Q|QUIT|" +
                "ST|STACK|" +
                "S|STORAGE|" +
                "SY|SYSTEM|" +
                "T|TEST|" +
                "TL|TLEVEL|" +
                "TR|TRESTART|" +
                "X|" +
                "Y|" +
                "Z[A-Z]*|"
        val intrinsicFunctions = "A|ASCII|" +
                "C|CHAR|" +
                "D|DATA|" +
                "E|EXTRACT|" +
                "F|FIND|" +
                "FN|FNUMBER|" +
                "G|GET|" +
                "J|JUSTIFY|" +
                "L|LENGTH|" +
                "NA|NAME|" +
                "O|ORDER|" +
                "P|PIECE|" +
                "QL|QLENGTH|" +
                "QS|QSUBSCRIPT|" +
                "Q|QUERY|" +
                "R|RANDOM|" +
                "RE|REVERSE|" +
                "S|SELECT|" +
                "ST|STACK|" +
                "T|TEXT|" +
                "TR|TRANSLATE|" +
                "V|VIEW|" +
                "Z[A-Z]*|"
        val intrinsic = intrinsicVariables + intrinsicFunctions

        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\t\n\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // A double or single quoted, possibly multi-line, string.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^(?:\"(?:[^\"]|\\\\.)*\")"),
            null,
            "\""
        )

        // A line comment that starts with ;
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^;[^\\r\\n]*"),
            null,
            ";"
        )
        // Add intrinsic variables and functions as declarations, there not really but it mean
        // they will hilighted differently from commands.
        fallthroughStylePatterns.new(
            Prettify.PR_DECLARATION, Regex(
                "^(?:\\$(?:$intrinsic))\\b", RegexOption.IGNORE_CASE
            ), null
        )
        // Add commands as keywords
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD, Regex(
                "^(?:[^\\$]$commands)\\b", RegexOption.IGNORE_CASE
            ), null
        )
        // A number is a decimal real literal or in scientific notation.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex("^[+-]?(?:(?:\\.\\d+|\\d+(?:\\.\\d*)?)(?:E[+\\-]?\\d+)?)", RegexOption.IGNORE_CASE)
        )
        // An identifier
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[a-z][a-zA-Z0-9]*", RegexOption.IGNORE_CASE)
        )
        // Exclude $ % and ^
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[^\\w\\t\\n\\r\\xA0\\\"\\$;%\\^]|_")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}