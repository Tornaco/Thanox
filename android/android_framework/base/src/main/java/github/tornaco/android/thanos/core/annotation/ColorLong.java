/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package github.tornaco.android.thanos.core.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.SOURCE;

/**
 * <p>Denotes that the annotated element represents a packed color
 * long. If applied to a long array, every element in the array
 * represents a color long. For more information on how colors
 * are packed in a long, please refer to the documentation of
 * the {@link android.graphics.Color} class.</p>
 *
 * <p>Example:</p>
 *
 * <pre>{@code
 *  public void setFillColor(@ColorLong long color);
 * }</pre>
 *
 * @see android.graphics.Color
 */
@Retention(SOURCE)
@Target({PARAMETER, METHOD, LOCAL_VARIABLE, FIELD})
public @interface ColorLong {
}
