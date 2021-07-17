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