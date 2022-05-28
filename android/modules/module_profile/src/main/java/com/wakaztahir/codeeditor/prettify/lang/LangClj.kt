/**
 * @license Copyright (C) 2011 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.parser.Prettify
import com.wakaztahir.codeeditor.prettify.parser.StylePattern
import com.wakaztahir.codeeditor.utils.new

/**
 * This is similar to the lang-clj.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 *
 *
 * Registers a language handler for Clojure.
 *
 *
 * To use, include prettify.js and this file in your HTML page.
 * Then put your code in an HTML tag like
 * <pre class="prettyprint lang-lisp">(my lisp code)</pre>
 * The lang-cl class identifies the language as common lisp.
 * This file supports the following language extensions:
 * lang-clj - Clojure
 *
 *
 * I used lang-lisp.js as the basis for this adding the clojure specific
 * keywords and syntax.
 *
 * "Name"    = 'Clojure'
 * "Author"  = 'Rich Hickey'
 * "Version" = '1.2'
 * "About"   = 'Clojure is a lisp for the jvm with concurrency primitives and a richer set of types.'
 *
 *
 * I used [Clojure.org Reference](http://clojure.org/Reference) as
 * the basis for the reserved word list.
 *
 *
 * @author jwall@google.com
 */
class LangClj : Lang() {
    companion object {
        val fileExtensions: List<String>
            get() = listOf("clj")
    }

    override val fallthroughStylePatterns = ArrayList<StylePattern>()
    override val shortcutStylePatterns = ArrayList<StylePattern>()

    init {

        // clojure has more paren types than minimal lisp.
        shortcutStylePatterns.new("opn", Regex("^[\\(\\{\\[]+"), null, "([{")
        shortcutStylePatterns.new("clo", Regex("^[\\)\\}\\]]+"), null, ")]}")
        // A line comment that starts with ;
        shortcutStylePatterns.new(
            Prettify.PR_COMMENT,
            Regex("^;[^\r\n]*"),
            null,
            ";"
        )
        // Whitespace
        shortcutStylePatterns.new(
            Prettify.PR_PLAIN, Regex("^[\t\n\r \\xA0]+"), null, "\t\n\r " + 0xA0.toChar().toString()
        )
        // A double quoted, possibly multi-line, string.
        shortcutStylePatterns.new(
            Prettify.PR_STRING,
            Regex("^\\\"(?:[^\\\"\\\\]|\\\\[\\s\\S])*(?:\\\"|$)"),
            null,
            "\""
        )
        // clojure has a much larger set of keywords
        fallthroughStylePatterns.new(
            Prettify.PR_KEYWORD,
            Regex("^(?:def|if|do|let|quote|var|fn|loop|recur|throw|try|monitor-enter|monitor-exit|defmacro|defn|defn-|macroexpand|macroexpand-1|for|doseq|dosync|dotimes|and|or|when|not|assert|doto|proxy|defstruct|first|rest|cons|defprotocol|deftype|defrecord|reify|defmulti|defmethod|meta|with-meta|ns|in-ns|create-ns|import|intern|refer|alias|namespace|resolve|ref|deref|refset|new|set!|memfn|to-array|into-array|aset|gen-class|reduce|map|filter|find|nil?|empty?|hash-map|hash-set|vec|vector|seq|flatten|reverse|assoc|dissoc|list|list?|disj|get|union|difference|intersection|extend|extend-type|extend-protocol|prn)\\b"),
            null
        )
        fallthroughStylePatterns.new(
            Prettify.PR_TYPE,
            Regex("^:[0-9a-zA-Z\\-]+")
        )
    }

    override fun getFileExtensions(): List<String> {
        return fileExtensions
    }
}