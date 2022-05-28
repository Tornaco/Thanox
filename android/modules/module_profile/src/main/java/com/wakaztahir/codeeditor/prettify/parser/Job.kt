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

/**
 * This is the job object that similar to those in JavaScript Prettify.
 *
 * @author Chan Wai Shing <cws1989></cws1989>@gmail.com>
 */
class Job constructor(basePos: Int = 0, sourceCode: String? = "") {
    /**
     * Set the starting point of the source code.
     *
     * @return the position
     */
    /**
     * Set the starting point of the source code.
     *
     * @param basePos the position
     */
    /**
     * The starting point of the source code.
     */
    var basePos: Int

    /**
     * The source code.
     */
    internal var sourceCode: String

    /**
     * The parsed results. n<sup>th</sup> items are starting position position,
     * n+1<sup>th</sup> items are the three-letter style keyword, where n start
     * from 0.
     */
    internal var decorations: List<Any>

    /**
     * Get the source code.
     *
     * @return the source code
     */
    fun getSourceCode(): String {
        return sourceCode
    }

    /**
     * Set the source code.
     *
     * @param sourceCode the source code
     */
    fun setSourceCode(sourceCode: String?) {
        if (sourceCode == null) {
            throw NullPointerException("argument 'sourceCode' cannot be null")
        }
        this.sourceCode = sourceCode
    }

    /**
     * Get the parsed results. see [.decorations].
     *
     * @return the parsed results
     */
    fun getDecorations(): List<Any> {
        return ArrayList(decorations)
    }

    /**
     * Set the parsed results. see [.decorations].
     *
     * @param decorations the parsed results
     */
    fun setDecorations(decorations: List<Any>?) {
        if (decorations == null) {
            this.decorations = ArrayList()
            return
        }
        this.decorations = ArrayList(decorations)
    }
    /**
     * Constructor.
     *
     * @param basePos the starting point of the source code
     * @param sourceCode the source code
     */
    /**
     * Constructor.
     */
    init {
        if (sourceCode == null) {
            throw NullPointerException("argument 'sourceCode' cannot be null")
        }
        this.basePos = basePos
        this.sourceCode = sourceCode
        decorations = ArrayList()
    }
}