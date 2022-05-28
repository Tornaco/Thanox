// Copyright (C) 2010 benoit@ryder.fr
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
 * This is similar to the lang-vhdl.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for VHDL '93.
 *
 * Based on the lexical grammar and keywords at
 * http://www.iis.ee.ethz.ch/~zimmi/download/vhdl93_syntax.html
 *
 * @author benoit@ryder.fr
 */
class LangVhdl : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("vhdl", "vhd")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {



        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\\t\\n\\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // String, character or bit string
        fallthroughStylePatterns.new(
            Prettify.PR_STRING, Regex("^(?:[BOX]?\"(?:[^\\\"]|\"\")*\"|'.')", RegexOption.IGNORE_CASE)
        )
        // Comment, from two dashes until end of line.
        fallthroughStylePatterns.new(
            Prettify.PR_COMMENT, Regex("^--[^\\r\\n]*")
        )
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD, Regex(
                "^(?:abs|access|after|alias|all|and|architecture|array|assert|attribute|begin|block|body|buffer|bus|case|component|configuration|constant|disconnect|downto|else|elsif|end|entity|exit|file|for|function|generate|generic|group|guarded|if|impure|in|inertial|inout|is|label|library|linkage|literal|loop|map|mod|nand|new|next|nor|not|null|of|on|open|or|others|out|package|port|postponed|procedure|process|pure|range|record|register|reject|rem|report|return|rol|ror|select|severity|shared|signal|sla|sll|sra|srl|subtype|then|to|transport|type|unaffected|units|until|use|variable|wait|when|while|with|xnor|xor)(?=[^\\w-]|$)",
                RegexOption.IGNORE_CASE
            ), null
        )
        // Type, predefined or standard
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE, Regex(
                "^(?:bit|bit_vector|character|boolean|integer|real|time|string|severity_level|positive|natural|signed|unsigned|line|text|std_u?logic(?:_vector)?)(?=[^\\w-]|$)",
                RegexOption.IGNORE_CASE
            ), null
        )
        // Predefined attributes
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE, Regex(
                "^\\'(?:ACTIVE|ASCENDING|BASE|DELAYED|DRIVING|DRIVING_VALUE|EVENT|HIGH|IMAGE|INSTANCE_NAME|LAST_ACTIVE|LAST_EVENT|LAST_VALUE|LEFT|LEFTOF|LENGTH|LOW|PATH_NAME|POS|PRED|QUIET|RANGE|REVERSE_RANGE|RIGHT|RIGHTOF|SIMPLE_NAME|STABLE|SUCC|TRANSACTION|VAL|VALUE)(?=[^\\w-]|$)",
                RegexOption.IGNORE_CASE
            ), null
        )
        // Number, decimal or based literal
        fallthroughStylePatterns.new(
            Prettify.PR_LITERAL, Regex(
                "^\\d+(?:_\\d+)*(?:#[\\w\\\\.]+#(?:[+\\-]?\\d+(?:_\\d+)*)?|(?:\\.\\d+(?:_\\d+)*)?(?:E[+\\-]?\\d+(?:_\\d+)*)?)",
                RegexOption.IGNORE_CASE
            )
        )
        // Identifier, basic or extended
        fallthroughStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^(?:[a-z]\\w*|\\\\[^\\\\]*\\\\)", RegexOption.IGNORE_CASE)
        )
        // Punctuation
        fallthroughStylePatterns.new(
            Prettify.PR_PUNCTUATION, Regex("^[^\\w\\t\\n\\r \\xA0\\\"\\'][^\\w\\t\\n\\r \\xA0\\-\\\"\\']*")
        )


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}