/*
 * Copyright (C) 2014 Google, Inc.
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
package github.tornaco.permission.compiler.common;

import com.google.common.collect.Ordering;

import java.util.Set;

import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;

import static com.google.common.base.Preconditions.checkNotNull;
import static javax.lang.model.element.ElementKind.PACKAGE;

public enum Visibility {
    PRIVATE,
    DEFAULT,
    PROTECTED,
    PUBLIC;

    public static Visibility ofElement(Element element) {
        checkNotNull(element);
        // packages don't have modifiers, but they're obviously "public"
        if (element.getKind().equals(PACKAGE)) {
            return PUBLIC;
        }
        Set<Modifier> modifiers = element.getModifiers();
        if (modifiers.contains(Modifier.PRIVATE)) {
            return PRIVATE;
        } else if (modifiers.contains(Modifier.PROTECTED)) {
            return PROTECTED;
        } else if (modifiers.contains(Modifier.PUBLIC)) {
            return PUBLIC;
        } else {
            return DEFAULT;
        }
    }

    public static Visibility effectiveVisibilityOfElement(Element element) {
        checkNotNull(element);
        Visibility effectiveVisibility = PUBLIC;
        Element currentElement = element;
        while (currentElement != null) {
            effectiveVisibility =
                    Ordering.natural().min(effectiveVisibility, ofElement(currentElement));
            currentElement = currentElement.getEnclosingElement();
        }
        return effectiveVisibility;
    }
}