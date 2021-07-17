package github.tornaco.permission.compiler;

/*
 * Copyright (C) 13/07/16 aitorvs
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

class AbortProcessingException extends RuntimeException {
    public AbortProcessingException() {
    }

    public AbortProcessingException(String s) {
        super(s);
    }

    public AbortProcessingException(String s, Throwable throwable) {
        super(s, throwable);
    }

    public AbortProcessingException(Throwable throwable) {
        super(throwable);
    }

    public AbortProcessingException(String s, Throwable throwable, boolean b, boolean b1) {
        super(s, throwable, b, b1);
    }
}
