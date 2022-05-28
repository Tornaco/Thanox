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
 * This is similar to the lang-vb.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for various flavors of basic.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-vb"></pre>
 *
 *
 * http://msdn.microsoft.com/en-us/library/aa711638(VS.71).aspx defines the
 * visual basic grammar lexical grammar.
 *
 * @author mikesamuel@gmail.com
 */
class LangVb : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("vb", "vbs")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^[\\t\\n\\r \\xA0\\u2028\\u2029]+"),
            null,
            "\t\n\r " + 0xA0.toChar().toString() + "\u2028\u2029"
        )
        // A double quoted string with quotes escaped by doubling them.
        // A single character can be suffixed with C.
        shortcutStylePatterns.new(
            Prettify.PR_STRING, Regex(
                "^(?:[\\\"\\u201C\\u201D](?:[^\\\"\\u201C\\u201D]|[\\\"\\u201C\\u201D]{2})(?:[\\\"\\u201C\\u201D]c|$)|[\\\"\\u201C\\u201D](?:[^\\\"\\u201C\\u201D]|[\\\"\\u201C\\u201D]{2})*(?:[\\\"\\u201C\\u201D]|$))",
                RegexOption.IGNORE_CASE
            ), null, "\"\u201C\u201D"
        )
        // A comment starts with a single quote and runs until the end of the line.
        // VB6 apparently allows _ as an escape sequence for newlines though
        // this is not a documented feature of VB.net.
        // http://meta.stackoverflow.com/q/121497/137403
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^[\\'\\u2018\\u2019](?:_(?:\r\n?|[^\r]?)|[^\\r\\n_\\u2028\\u2029])*"),
            null,
            "'\u2018\u2019"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex(
                "^(?:AddHandler|AddressOf|Alias|And|AndAlso|Ansi|As|Assembly|Auto|Boolean|ByRef|Byte|ByVal|Call|Case|Catch|CBool|CByte|CChar|CDate|CDbl|CDec|Char|CInt|Class|CLng|CObj|Const|CShort|CSng|CStr|CType|Date|Decimal|Declare|Default|Delegate|Dim|DirectCast|Do|Double|Each|Else|ElseIf|End|EndIf|Enum|Erase|Error|Event|Exit|Finally|For|Friend|Function|Get|GetType|GoSub|GoTo|Handles|If|Implements|Imports|In|Inherits|Integer|Interface|Is|Let|Lib|Like|Long|Loop|Me|Mod|Module|MustInherit|MustOverride|MyBase|MyClass|Namespace|New|Next|Not|NotInheritable|NotOverridable|Object|On|Option|Optional|Or|OrElse|Overloads|Overridable|Overrides|ParamArray|Preserve|Private|Property|Protected|Public|RaiseEvent|ReadOnly|ReDim|RemoveHandler|Resume|Return|Select|Set|Shadows|Shared|Short|Single|Static|Step|Stop|String|Structure|Sub|SyncLock|Then|Throw|To|Try|TypeOf|Unicode|Until|Variant|Wend|When|While|With|WithEvents|WriteOnly|Xor|EndIf|GoSub|Let|Variant|Wend)\\b",
                RegexOption.IGNORE_CASE
            ),
            null
        )
        // A second comment form
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^REM\\b[^\\r\\n\\u2028\\u2029]*", RegexOption.IGNORE_CASE)
        )
        // A boolean, numeric, or date literal.
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL,
            Regex(
                "^(?:True\\b|False\\b|Nothing\\b|\\d+(?:E[+\\-]?\\d+[FRD]?|[FRDSIL])?|(?:&H[0-9A-F]+|&O[0-7]+)[SIL]?|\\d*\\.\\d+(?:E[+\\-]?\\d+)?[FRD]?|#\\s+(?:\\d+[\\-\\/]\\d+[\\-\\/]\\d+(?:\\s+\\d+:\\d+(?::\\d+)?(\\s*(?:AM|PM))?)?|\\d+:\\d+(?::\\d+)?(\\s*(?:AM|PM))?)\\s+#)",
                RegexOption.IGNORE_CASE
            )
        )
        // An identifier.  Keywords can be turned into identifers
        // with square brackets, and there may be optional type
        // characters after a normal identifier in square brackets.
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex(
                "^(?:(?:[a-z]|_\\w)\\w*(?:\\[[%&@!#]+\\])?|\\[(?:[a-z]|_\\w)\\w*\\])",
                RegexOption.IGNORE_CASE
            )
        )
        // A run of punctuation
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[^\\w\\t\\n\\r \\\"\\'\\[\\]\\xA0\\u2018\\u2019\\u201C\\u201D\\u2028\\u2029]+")
        )
        // Square brackets
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^(?:\\[|\\])")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}