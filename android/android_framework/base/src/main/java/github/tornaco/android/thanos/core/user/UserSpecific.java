/*
 * (C) Copyright 2022 Thanox
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
 *
 */

package github.tornaco.android.thanos.core.user;

import java.util.Objects;

public class UserSpecific<T> {
    private final int userId;
    private final T data;

    public UserSpecific(int userId, T data) {
        this.userId = userId;
        this.data = data;
    }

    public int getUserId() {
        return userId;
    }

    public T getData() {
        return data;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserSpecific<?> that = (UserSpecific<?>) o;
        return userId == that.userId && Objects.equals(data, that.data);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, data);
    }

    @Override
    public String toString() {
        return "UserSpecific{" +
                "userId=" + userId +
                ", data=" + data +
                '}';
    }
}
