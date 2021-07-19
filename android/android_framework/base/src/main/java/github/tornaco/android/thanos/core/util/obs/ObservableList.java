/*
 * Copyright (C) 2018 The Android Open Source Project
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
package github.tornaco.android.thanos.core.util.obs;

import java.util.List;

/**
 * A {@link List} that notifies when changes are made. An ObservableList bound to the UI
 * will keep the it up-to-date when changes occur.
 * <p>
 * The ObservableList must notify its callbacks whenever a change to the list occurs, using
 * {@link OnListChangedCallback}.
 * <p>
 * ObservableArrayList implements ObservableList with an underlying ArrayList.
 * ListChangeRegistry can help in maintaining the callbacks of other implementations.
 */
public interface ObservableList<T> extends List<T> {

    /**
     * Adds a callback to be notified when changes to the list occur.
     *
     * @param callback The callback to be notified on list changes
     */
    void addOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> callback);

    /**
     * Removes a callback previously added.
     *
     * @param callback The callback to remove.
     */
    void removeOnListChangedCallback(OnListChangedCallback<? extends ObservableList<T>> callback);

    /**
     * The callback that is called by ObservableList when the list has changed.
     */
    abstract class OnListChangedCallback<T extends ObservableList> {

        /**
         * Called whenever a change of unknown type has occurred, such as the entire list being
         * set to new values.
         *
         * @param sender The changing list.
         */
        public abstract void onChanged(T sender);

        /**
         * Called whenever one or more items in the list have changed.
         *
         * @param sender        The changing list.
         * @param positionStart The starting index that has changed.
         * @param itemCount     The number of items that have changed.
         */
        public abstract void onItemRangeChanged(T sender, int positionStart, int itemCount);

        /**
         * Called whenever items have been inserted into the list.
         *
         * @param sender        The changing list.
         * @param positionStart The insertion index
         * @param itemCount     The number of items that have been inserted
         */
        public abstract void onItemRangeInserted(T sender, int positionStart, int itemCount);

        /**
         * Called whenever items in the list have been moved.
         *
         * @param sender       The changing list.
         * @param fromPosition The position from which the items were moved
         * @param toPosition   The destination position of the items
         * @param itemCount    The number of items moved
         */
        public abstract void onItemRangeMoved(T sender, int fromPosition, int toPosition,
                                              int itemCount);

        /**
         * Called whenever items in the list have been deleted.
         *
         * @param sender        The changing list.
         * @param positionStart The starting index of the deleted items.
         * @param itemCount     The number of items removed.
         */
        public abstract void onItemRangeRemoved(T sender, int positionStart, int itemCount);
    }
}
