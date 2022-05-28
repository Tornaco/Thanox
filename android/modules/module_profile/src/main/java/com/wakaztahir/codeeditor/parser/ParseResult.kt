// Copyright (c) 2012 Chan Wai Shing
//
// Permission is hereby granted, free of charge, to any person obtaining
// a copy of this software and associated documentation files (the
// "Software"), to deal in the Software without restriction, including
// without limitation the rights to use, copy, modify, merge, publish,
// distribute, sublicense, and/or sell copies of the Software, and to
// permit persons to whom the Software is furnished to do so, subject to
// the following conditions:
//
// The above copyright notice and this permission notice shall be
// included in all copies or substantial portions of the Software.
//
// THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
// EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
// MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
// NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
// LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
// OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION
// WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
package com.wakaztahir.codeeditor.parser

/**
 * The parser parsed result.
 *
 * This class include the information needed to highlight the syntax.
 * Information includes where the content located in the document (offset and
 * length) and what style(s) should be applied on that segment of content.
 *
 * @author Chan Wai Shing <cws1989></cws1989>@gmail.com>
 */
class ParseResult(
    /**
     * The start position of the content.
     */
    var offset: Int,
    /**
     * The length of the content.
     */
    var length: Int, styleKeys: List<String>?
) {
    /**
     * The start position of the content.
     * @return the start position of the content
     */
    /**
     * The start position of the content.
     * @param offset the start position of the content
     */
    /**
     * The length of the content.
     * @return the length of the content
     */
    /**
     * The length of the content.
     * @param length the length of the content
     */

    /**
     * The style keys of the content. The style at higher index of the list will
     * override the style of the lower index.
     */
    protected var styleKeys: MutableList<String>

    /**
     * Get the style keys represented by one string key, see
     * [Theme.getStylesAttributeSet].
     * @return the style keys of the content
     */
    val styleKeysString: String
        get() {
            val sb = StringBuilder(10)
            var i = 0
            val iEnd = styleKeys.size
            while (i < iEnd) {
                if (i != 0) {
                    sb.append(" ")
                }
                sb.append(styleKeys[i])
                i++
            }
            return sb.toString()
        }

    /**
     * The style keys of the content.
     * @param styleKey the style key
     * @return see the return value of [List.add]
     */
    fun addStyleKey(styleKey: String): Boolean {
        return styleKeys.add(styleKey)
    }

    /**
     * The style keys of the content.
     * @param styleKey the style key
     * @return see the return value of [List.remove]
     */
    fun removeStyleKey(styleKey: String): Boolean {
        return styleKeys.remove(styleKey)
    }

    /**
     * The style keys of the content.
     */
    fun clearStyleKeys() {
        styleKeys.clear()
    }

    /**
     * {@inheritDoc}
     */
    override fun toString(): String {
        val sb = StringBuilder()
        sb.append("[")
        sb.append(offset)
        sb.append("; ")
        sb.append(length)
        sb.append("; ")
        var i = 0
        val iEnd = styleKeys.size
        while (i < iEnd) {
            if (i != 0) {
                sb.append(", ")
            }
            sb.append(styleKeys[i])
            i++
        }
        sb.append("]")
        return sb.toString()
    }

    /**
     * Constructor.
     *
     * @param offset the start position of the content
     * @param length the length of the content
     * @param styleKeys the style keys of the content
     */
    init {
        this.styleKeys = styleKeys?.toMutableList() ?: mutableListOf()
    }
}