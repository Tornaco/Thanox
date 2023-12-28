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

package github.tornaco.android.thanos.core.persist;

import android.annotation.NonNull;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

import github.tornaco.android.thanos.core.annotation.Keep;

@Keep
public class UserStringMapRepo {
    private final StringMapRepo repo;
    private final int userId;

    public UserStringMapRepo(StringMapRepo repo, int userId) {
        this.repo = repo;
        this.userId = userId;
    }

    public StringMapRepo getRepo() {
        return repo;
    }

    public int getUserId() {
        return userId;
    }

    public void reload() {
        repo.reload();
    }

    public void reloadAsync() {
        repo.reloadAsync();
    }

    public void flush() {
        repo.flush();
    }

    public void flushAsync() {
        repo.flushAsync();
    }

    public String name() {
        return repo.name();
    }

    public Map<String, String> snapshot() {
        return repo.snapshot();
    }

    public boolean hasNoneNullValue(String s) {
        return repo.hasNoneNullValue(s);
    }

    public int size() {
        return repo.size();
    }

    public boolean isEmpty() {
        return repo.isEmpty();
    }

    public boolean containsKey(Object key) {
        return repo.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return repo.containsValue(value);
    }

    public String get(Object key) {
        return repo.get(key);
    }

    public String put(String key, String value) {
        return repo.put(key, value);
    }

    public String remove(Object key) {
        return repo.remove(key);
    }

    public void putAll(Map<? extends String, ? extends String> m) {
        repo.putAll(m);
    }

    public void clear() {
        repo.clear();
    }

    @NonNull
    public Set<String> keySet() {
        return repo.keySet();
    }

    @NonNull
    public Collection<String> values() {
        return repo.values();
    }

    @NonNull
    public Set<Map.Entry<String, String>> entrySet() {
        return repo.entrySet();
    }
}
