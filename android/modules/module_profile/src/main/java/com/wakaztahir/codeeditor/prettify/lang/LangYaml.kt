// Copyright (C) 2010 ribrdb @ code.google.com
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
 * This is similar to the lang-yaml.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for YAML.
 *
 * @author ribrdb
 */
class LangYaml : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("yaml", "yml")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {
        shortcutStylePatterns.new(
            Prettify.PR_PUNCTUATION,
            Regex("^[:|>?]+"),
            null,
            ":|>?"
        )
        shortcutStylePatterns.new(
            Prettify.PR_DECLARATION,
            Regex("^%(?:YAML|TAG)[^#\\r\\n]+"),
            null,
            "%"
        )
        shortcutStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^[&]\\S+"),
            null,
            "&"
        )
        shortcutStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^!\\S*"),
            null,
            "!"
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\"(?:[^\\\\\"]|\\\\.)*(?:\"|$)"),
            null,
            "\""
        )
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^'(?:[^']|'')*(?:'|$)"),
            null,
            "'"
        )
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^#[^\\r\\n]*"),
            null,
            "#"
        )
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN,
            Regex("^\\s+"),
            null,
            " \t\r\n"
        )
        fallthroughStylePatterns.new(
            Prettify.PR_DECLARATION,
            Regex("^(?:---|\\.\\.\\.)(?:[\\r\\n]|$)")
        )
        fallthroughStylePatterns.new(Prettify.PR_PUNCTUATION, Regex("^-"))
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^\\w+:[ \\r\\n]")
        )
        fallthroughStylePatterns.new(Prettify.PR_PLAIN, Regex("^\\w+"))


    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}