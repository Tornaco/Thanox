// Copyright (C) 2011 Chan Wai Shing
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
package com.wakaztahir.codeeditor.prettify.parser

/**
 * Common Utilities.
 * Some of the functions are port from JavaScript.
 *
 * @author Chan Wai Shing <cws1989></cws1989>@gmail.com>
 */
object Util {
    /**
     * Treat a variable as an boolean in JavaScript style. Note this function can
     * only handle string, integer and boolean currently. All other data type, if
     * null, return false, not null return true.
     *
     * @param var the variable to get value from
     * @return the boolean value
     */
    fun getVariableValueAsBoolean(`var`: Any?): Boolean {
        return if (`var` == null) {
            false
        } else if (`var` is String) {
            `var`.isNotEmpty()
        } else if (`var` is Int) {
            `var` != 0
        } else if (`var` is Boolean) {
            `var`
        } else {
            true
        }
    }

    /**
     * Treat a variable as an integer in JavaScript style. Note this function can
     * only handle integer and boolean currently.
     *
     * @param var the variable to get value from
     * @return the integer value
     * @throws IllegalArgumentException the data type of `var` is neither
     * integer nor boolean.
     */
    fun getVariableValueAsInteger(`var`: Any?): Int {
        if (`var` == null) {
            throw NullPointerException("argument 'var' cannot be null")
        }
        var returnResult: Int? = -1
        returnResult = if (`var` is Int) {
            `var`
        } else if (`var` is Boolean) {
            // Javascript treat true as 1
            if (`var`) 1 else 0
        } else {
            throw IllegalArgumentException("'var' is neither integer nor boolean")
        }
        return returnResult
    }

    /**
     * Get all the matches for `string` compiled by `pattern`. If
     * `isGlobal` is true, the return results will only include the
     * group 0 matches. It is similar to string.match(regexp) in JavaScript.
     *
     * @param regex the regexp
     * @param string the string
     * @param isGlobal similar to JavaScript /g flag
     *
     * @return all matches
     */
    fun match(regex: Regex, string: String, isGlobal: Boolean): MutableList<String> {
//        val matcher = Pattern.compile(regex.pattern).matcher(string)
//        val matchesList = mutableListOf<String>()
//        while (matcher.find()) {
//            matchesList.add(matcher.group(0))
//            if (!isGlobal) {
//                var i : Int = 1
//                val iEnd : Int = matcher.groupCount()
//                while (i <= iEnd) {
//                    matchesList.add(matcher.group(i))
//                    i++
//                }
//            }
//        }
//        println("His Matches : ${matchesList.joinToString(",")}")
//        println("Myy Matches : ${myMatchesList.joinToString(",")}")

        //This code replaces the above commented code
        val matchesList = mutableListOf<String>()
        var startIndex = 0
        var matchResult = regex.find(string, startIndex)
        while (matchResult != null) {
            matchResult.groups[0]?.value?.let { matchesList.add(it) }
            if (!isGlobal) {
                var i = 1
                while (i < matchResult.groups.size) {
                    matchResult.groups[i]?.value?.let { matchesList.add(it) }
                    i++
                }
            }
            startIndex = matchResult.range.last + 1
            matchResult = regex.find(string, startIndex)
        }
        return matchesList
    }

    /**
     * Test whether the `string` has at least one match by
     * `pattern`.
     *
     * @param pattern the regexp
     * @param string the string to test
     *
     * @return true if at least one match, false if no match
     */
    fun test(pattern: Regex, string: String): Boolean {
        return pattern.find(string) != null
    }

    /**
     * Join the `strings` into one string.
     *
     * @param strings the string list to join
     *
     * @return the joined string
     */
    fun join(strings: List<String>): String {
        return join(strings.toTypedArray())
    }

    /**
     * Join the `strings` into one string with `delimiter` in
     * between.
     *
     * @param strings the string list to join
     * @param delimiter the delimiter
     *
     * @return the joined string
     */
    fun join(strings: List<String>?, delimiter: String?): String {
        if (strings == null) {
            throw NullPointerException("argument 'strings' cannot be null")
        }
        return join(strings.toTypedArray(), delimiter)
    }
    /**
     * Join the `strings` into one string with `delimiter` in
     * between. It is similar to RegExpObject.test(string) in JavaScript.
     *
     * @param strings the string list to join
     * @param delimiter the delimiter
     *
     * @return the joined string
     */
    /**
     * Join the `strings` into one string.
     *
     * @param strings the string list to join
     *
     * @return the joined string
     */
    fun join(strings: Array<String>?, delimiter: String? = null): String {
        if (strings == null) {
            throw NullPointerException("argument 'strings' cannot be null")
        }
        val sb = StringBuilder()
        if (strings.size != 0) {
            sb.append(strings[0])
            var i = 1
            val iEnd = strings.size
            while (i < iEnd) {
                if (delimiter != null) {
                    sb.append(delimiter)
                }
                sb.append(strings[i])
                i++
            }
        }
        return sb.toString()
    }

    /**
     * Remove identical adjacent tags from `decorations`.
     *
     * @param decorations see [prettify.parser.Job.decorations]
     * @param source the source code
     *
     * @return the `decorations` after treatment
     *
     * @throws IllegalArgumentException the size of `decoration` is not
     * a multiple of 2
     */
    fun removeDuplicates(decorations: List<Any>?, source: String?): List<Any> {
        if (decorations == null) {
            throw NullPointerException("argument 'decorations' cannot be null")
        }
        if (source == null) {
            throw NullPointerException("argument 'source' cannot be null")
        }
        require(decorations.size and 0x1 == 0) { "the size of argument 'decorations' should be a multiple of 2" }
        val returnList: MutableList<Any> = ArrayList()

        // use TreeMap to remove entrys with same pos
        val orderedMap: MutableMap<Int, Any> = mutableMapOf()
        var i = 0
        val iEnd = decorations.size
        while (i < iEnd) {
            orderedMap[decorations[i] as Int] = decorations[i + 1]
            i += 2
        }

        // remove adjacent style
        var previousStyle: String? = null
        for (pos in orderedMap.keys) {
            val style = orderedMap[pos] as String?
            if (previousStyle != null && previousStyle == style) {
                continue
            }
            returnList.add(pos)
            if (style != null) {
                returnList.add(style)
            }
            previousStyle = style
        }

        // remove last zero length tag
        val returnListSize = returnList.size
        if (returnListSize >= 4 && returnList[returnListSize - 2] == source.length) {
            returnList.removeAt(returnListSize - 2)
            returnList.removeAt(returnListSize - 2)
        }
        return returnList
    }
}