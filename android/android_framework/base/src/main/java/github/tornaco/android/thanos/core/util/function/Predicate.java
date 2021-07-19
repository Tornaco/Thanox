/*
 * Copyright (C) 2007 The Guava Authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package github.tornaco.android.thanos.core.util.function;

import com.google.common.base.Predicates;
import github.tornaco.android.thanos.core.annotation.Nullable;

/**
 * Legacy version of {@link java.util.function.Predicate java.util.function.Predicate}. Determines a
 * true or false value for a given input.
 *
 * <p>As this interface extends {@code java.util.function.Predicate}, an instance of this type may
 * be used as a {@code Predicate} directly. To use a {@code java.util.function.Predicate} where a
 * {@code com.google.common.base.Predicate} is expected, use the method reference {@code
 * predicate::test}.
 *
 * <p>This interface is now a legacy type. Use {@code java.util.function.Predicate} (or the
 * appropriate primitive specialization such as {@code IntPredicate}) instead whenever possible.
 * Otherwise, at least reduce <i>explicit</i> dependencies on this type by using lambda expressions
 * or method references instead of classes, leaving your code easier to migrate in the future.
 *
 * <p>The {@link Predicates} class provides common predicates and related utilities.
 *
 * <p>See the Guava User Guide article on <a
 * href="https://github.com/google/guava/wiki/FunctionalExplained">the use of {@code Predicate}</a>.
 *
 * @author Kevin Bourrillion
 * @since 2.0
 */
public interface Predicate<T> {
    boolean test(@Nullable T input);
}
