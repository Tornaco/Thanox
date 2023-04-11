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

package com.andrognito.pinlockview;

import java.util.Random;

/**
 * Created by aritraroy on 10/03/17.
 */

public class ShuffleArrayUtils {

    /**
     * Shuffle an array
     *
     * @param array
     */
    static int[] shuffle(int[] array) {
        int length = array.length;
        Random random = new Random();
        random.nextInt();

        for (int i = 0; i < length; i++) {
            int change = i + random.nextInt(length - i);
            swap(array, i, change);
        }
        return array;
    }

    private static void swap(int[] array, int index, int change) {
        int temp = array[index];
        array[index] = array[change];
        array[change] = temp;
    }
}