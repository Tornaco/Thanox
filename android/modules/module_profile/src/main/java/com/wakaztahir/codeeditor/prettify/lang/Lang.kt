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
package com.wakaztahir.codeeditor.prettify.lang

import com.wakaztahir.codeeditor.prettify.lang.Lang
import com.wakaztahir.codeeditor.prettify.parser.StylePattern

/**
 * Lang class for Java Prettify.
 * Note that the method [.getFileExtensions] should be overridden.
 *
 * @author Chan Wai Shing <cws1989></cws1989>@gmail.com>
 */
abstract class Lang {
    /**
     * Similar to those in JavaScript prettify.js.
     */
    internal abstract val shortcutStylePatterns: List<StylePattern>

    /**
     * Similar to those in JavaScript prettify.js.
     */
    internal abstract val fallthroughStylePatterns: List<StylePattern>

    abstract fun getFileExtensions(): List<String>
}