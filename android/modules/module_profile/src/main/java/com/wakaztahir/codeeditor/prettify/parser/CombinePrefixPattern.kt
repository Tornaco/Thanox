// Copyright (C) 2006 Google Inc.
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

import com.wakaztahir.codeeditor.prettify.parser.Util.join
import kotlin.Char.Companion.MIN_HIGH_SURROGATE
import kotlin.Char.Companion.MIN_LOW_SURROGATE
import kotlin.math.abs

/**
 * This is similar to the combinePrefixPattern.js in JavaScript Prettify.
 *
 * All comments are adapted from the JavaScript Prettify.
 *
 * @author mikesamuel@gmail.com
 */
class CombinePrefixPattern {
    private var capturedGroupIndex = 0
    private var needToFoldCase = false

    private val escapeCharToCodeUnit: MutableMap<Char, Int> = HashMap<Char, Int>().apply {
        this['b'] = 8
        this['t'] = 9
        this['n'] = 0xa
        this['v'] = 0xb
        this['f'] = 0xc
        this['r'] = 0xf
    }

    /**
     * Given a group of [java.util.regex.Pattern]s, returns a `RegExp` that globally
     * matches the union of the sets of strings matched by the input RegExp.
     * Since it matches globally, if the input strings have a start-of-input
     * anchor (/^.../), it is ignored for the purposes of unioning.
     * @param regexs non multiline, non-global regexs.
     * @return Pattern a global regex.
     */
    @Throws(Exception::class)
    fun combinePrefixPattern(regexs: List<Regex>): Regex {
        var ignoreCase = false
        run {
            var i: Int = 0
            val n: Int = regexs.size
            while (i < n) {
                val regex: Regex = regexs.get(i)
                if (regex.options.contains(RegexOption.IGNORE_CASE)) {
                    ignoreCase = true
                } else if (Util.test(
                        Regex("[a-z]", RegexOption.IGNORE_CASE),
                        regex.pattern.replace("\\\\[Uu][0-9A-Fa-f]{4}|\\\\[Xx][0-9A-Fa-f]{2}|\\\\[^UuXx]".toRegex(), "")
                    )
                ) {
                    needToFoldCase = true
                    ignoreCase = false
                    break
                }
                ++i
            }
        }
        val rewritten: MutableList<String> = ArrayList()
        var i = 0
        val n = regexs.size
        while (i < n) {
            val regex = regexs[i]
            if (regex.options.contains(RegexOption.MULTILINE)) {
                throw Exception("Multiline Regex : " + regex.pattern)
            }
            rewritten.add("(?:" + allowAnywhereFoldCaseAndRenumberGroups(regex) + ")")
            ++i
        }
        return if (ignoreCase) Regex(
            join(rewritten, "|"),
            RegexOption.IGNORE_CASE
        ) else Regex(
            join(rewritten, "|")
        )
    }

    internal fun decodeEscape(charsetPart: String): Int {
        val cc0: Int = charsetPart[0].code
        if (cc0 != 92  /* \\ */) {
            return cc0
        }
        val c1 = charsetPart[1]
        val charCode = escapeCharToCodeUnit[c1]
        return when {
            charCode != null -> charCode
            c1 in '0'..'7' -> charsetPart.substring(1).toInt(8)
            c1 == 'u' || c1 == 'x' -> charsetPart.substring(2).toInt(16)
            else -> charsetPart[1].code
        }
    }

    internal fun encodeEscape(charCode: Int): String {
        if (charCode < 0x20) {
            return (if (charCode < 0x10) "\\x0" else "\\x") + charCode.toString(16)
        }
//        val ch = String(Character.toChars(charCode))
        val ch = toChars(charCode).joinToString("")
        return if (((charCode == '\\'.code) || (charCode == '-'.code) || (charCode == ']'.code) || (charCode == '^'.code))) "\\" + ch else ch
    }

    internal fun toChars(codePoint: Int): CharArray {
        return if (codePoint ushr 16 == 0) {
            charArrayOf(codePoint.toChar())
        } else if (codePoint ushr 16 < 0X10FFFF + 1 ushr 16) {
            val charArray = CharArray(2)
            charArray[1] = ((codePoint and 0x3ff) + MIN_LOW_SURROGATE.code).toChar()
            charArray[0] =
                ((codePoint ushr 10) + (MIN_HIGH_SURROGATE.code - (0x010000 ushr 10))).toChar()
            charArray
        } else {
            throw IllegalArgumentException("Not a valid unicode code point")
        }
    }

    internal fun caseFoldCharset(charSet: String?): String {
        val charsetParts = Util.match(
            Regex(
                ("\\\\u[0-9A-Fa-f]{4}"
                        + "|\\\\x[0-9A-Fa-f]{2}"
                        + "|\\\\[0-3][0-7]{0,2}"
                        + "|\\\\[0-7]{1,2}"
                        + "|\\\\[\\s\\S]"
                        + "|-"
                        + "|[^-\\\\]")
            ), charSet!!.substring(1, charSet.length - 1), true
        )
        val ranges: MutableList<MutableList<Int>> = ArrayList()
        val inverse = charsetParts[0] == "^"
        val out: MutableList<String> = ArrayList(listOf("["))
        if (inverse) {
            out.add("^")
        }
        run {
            var i: Int = if (inverse) 1 else 0
            val n: Int = charsetParts.size
            while (i < n) {
                val p: String = charsetParts[i]
                if (Util.test(Regex("\\\\[bdsw]", RegexOption.IGNORE_CASE), p)) {  // Don't muck with named groups.
                    out.add(p)
                } else {
                    val start: Int = decodeEscape(p)
                    var end: Int
                    if (i + 2 < n && ("-" == charsetParts[i + 1])) {
                        end = decodeEscape(charsetParts[i + 2])
                        i += 2
                    } else {
                        end = start
                    }
                    ranges.add(mutableListOf(start, end))
                    // If the range might intersect letters, then expand it.
                    // This case handling is too simplistic.
                    // It does not deal with non-latin case folding.
                    // It works for latin source code identifiers though.
                    if (!(end < 65 || start > 122)) {
                        if (!(end < 65 || start > 90)) {
                            ranges.add(mutableListOf(65.coerceAtLeast(start) or 32, end.coerceAtMost(90) or 32))
                        }
                        if (!(end < 97 || start > 122)) {
                            ranges.add(
                                mutableListOf(
                                    97.coerceAtLeast(start) and 32.inv(), end.coerceAtMost(122) and 32.inv()
                                )
                            )
                        }
                    }
                }
                ++i
            }
        }

        // [[1, 10], [3, 4], [8, 12], [14, 14], [16, 16], [17, 17]]
        // -> [[1, 12], [14, 14], [16, 17]]
        ranges.sortWith(Comparator { a, b -> if (a[0] != b[0]) (a[0] - b[0]) else (b[1] - a[1]) })
//        Collections.sort(ranges, Comparator { a, b -> if (a[0] !== b[0]) (a[0] - b[0]) else (b[1] - a[1]) })
        val consolidatedRanges: MutableList<List<Int>> = ArrayList()
        //        List<Integer> lastRange = listOf(new Integer[]{0, 0});
        var lastRange: MutableList<Int> = ArrayList(listOf(0, 0))
        for (i in ranges.indices) {
            val range = ranges[i]
            if (lastRange[1] != null && range[0] <= lastRange[1] + 1) {
                lastRange[1] = (lastRange[1]).coerceAtLeast(range[1])
            } else {
                // reference of lastRange is added
                consolidatedRanges.add(range.also { lastRange = it })
            }
        }
        for (i in consolidatedRanges.indices) {
            val range = consolidatedRanges[i]
            out.add(encodeEscape(range[0]))
            if (range[1] > range[0]) {
                if (range[1] + 1 > range[0]) {
                    out.add("-")
                }
                out.add(encodeEscape(range[1]))
            }
        }
        out.add("]")
        return join(out)
    }

    internal fun allowAnywhereFoldCaseAndRenumberGroups(regex: Regex): String {
        // Split into character sets, escape sequences, punctuation strings
        // like ('(', '(?:', ')', '^'), and runs of characters that do not
        // include any of the above.
        val parts = Util.match(
            Regex(
                ("(?:"
                        + "\\[(?:[^\\x5C\\x5D]|\\\\[\\s\\S])*\\]" // a character set
                        + "|\\\\u[A-Fa-f0-9]{4}" // a unicode escape
                        + "|\\\\x[A-Fa-f0-9]{2}" // a hex escape
                        + "|\\\\[0-9]+" // a back-reference or octal escape
                        + "|\\\\[^ux0-9]" // other escape sequence
                        + "|\\(\\?[:!=]" // start of a non-capturing group
                        + "|[\\(\\)\\^]" // start/end of a group, or line start
                        + "|[^\\x5B\\x5C\\(\\)\\^]+" // run of other characters
                        + ")")
            ), regex.pattern, true
        )
        val n = parts.size

        // Maps captured group numbers to the number they will occupy in
        // the output or to -1 if that has not been determined, or to
        // undefined if they need not be capturing in the output.
        val capturedGroups: MutableMap<Int, Int?> = HashMap()

        // Walk over and identify back references to build the capturedGroups
        // mapping.
        run {
            var i: Int = 0
            var groupIndex: Int = 0
            while (i < n) {
                val p: String? = parts.get(i)
                if ((p == "(")) {
                    // groups are 1-indexed, so max group index is count of '('
                    ++groupIndex
                } else if ('\\' == p!![0]) {
                    try {
                        val decimalValue: Int = abs(p.substring(1).toInt())
                        if (decimalValue <= groupIndex) {
                            capturedGroups[decimalValue] = -1
                        } else {
                            // Replace with an unambiguous escape sequence so that
                            // an octal escape sequence does not turn into a backreference
                            // to a capturing group from an earlier regex.
                            parts[i] = encodeEscape(decimalValue)
                        }
                    } catch (ex: NumberFormatException) {
                    }
                }
                ++i
            }
        }

        // Renumber groups and reduce capturing groups to non-capturing groups
        // where possible.
        for (i: Int in capturedGroups.keys) {
            if (-1 == capturedGroups[i]) {
                capturedGroups[i] = ++capturedGroupIndex
            }
        }
        run {
            var i: Int = 0
            var groupIndex: Int = 0
            while (i < n) {
                val p: String? = parts.get(i)
                if ((p == "(")) {
                    ++groupIndex
                    if (capturedGroups.get(groupIndex) == null) {
                        parts[i] = "(?:"
                    }
                } else if ('\\' == p!!.get(0)) {
                    try {
                        val decimalValue: Int = abs(p.substring(1).toInt())
                        if (decimalValue <= groupIndex) {
                            parts[i] = "\\" + capturedGroups.get(decimalValue)
                        }
                    } catch (ex: NumberFormatException) {
                    }
                }
                ++i
            }
        }

        // Remove any prefix anchors so that the output will match anywhere.
        // ^^ really does mean an anchored match though.
        for (i in 0 until n) {
            if (("^" == parts[i]) && "^" != parts.get(i + 1)) {
                parts[i] = ""
            }
        }

        // Expand letters to groups to handle mixing of case-sensitive and
        // case-insensitive patterns if necessary.
        if (regex.options.contains(RegexOption.IGNORE_CASE) && needToFoldCase) {
            for (i in 0 until n) {
                val p = parts[i]
                val ch0: Char = if (p.length > 0) p[0] else '0'
                if (p.length >= 2 && ch0 == '[') {
                    parts[i] = caseFoldCharset(p)
                } else if (ch0 != '\\') {
                    // TODO: handle letters in numeric escapes.


//                    val sb = StringBuilder()
//                    val _matcher = Pattern.compile("[a-zA-Z]").matcher(p)
//                    while (_matcher.find()) {
//                        val cc = _matcher.group(0).codePointAt(0)
//                        _matcher.appendReplacement(sb, "")
//                        sb.append("[").append((cc and 32.inv()).toChar().toString()).append(
//                            (cc or 32).toChar().toString()
//                        ).append("]")
//                    }
//                    _matcher.appendTail(sb)

                    //This code replaces the above commented code
                    val mySb = StringBuilder()
                    val regEx = Regex("[a-zA-Z]")
                    var startIndex = 0
                    var matchResult = regEx.find(p, startIndex)
                    if (matchResult == null) {
                        mySb.append(p.substring(startIndex, p.length))
                    }
                    var appendIndex = 0
                    while (matchResult != null) {
                        val cc = matchResult.groups.get(0)?.value?.get(0)?.code
                        mySb.append(p.substring(appendIndex,matchResult.range.first))
                        appendIndex = matchResult.range.last
                        if (cc != null) {
                            mySb.append("[").append((cc and 32.inv()).toChar().toString()).append(
                                (cc or 32).toChar().toString()
                            ).append("]")
                        }
                        startIndex = matchResult.range.last + 1
                        matchResult = regEx.find(p, startIndex)
                        if (matchResult == null) {
                            if (startIndex < p.length) {
                                mySb.append(p.substring(startIndex, p.length))
                            }
                        }
                    }

//                    println("-----------------")
//                    println("finding in : $p")
//                    println("His String : $sb")
//                    println("Myy String : $mySb")
                    parts[i] = mySb.toString()
                }
            }
        }
        return join(parts)
    }
}