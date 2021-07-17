/*
 * Copyright (C) 2007 The Android Open Source Project
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

package com.andrognito.patternlockview.utils;

import com.andrognito.patternlockview.PatternLockView;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class PatternLockUtils {

    private static final String UTF8 = "UTF-8";
    private static final String SHA1 = "SHA-1";
    private static final String MD5 = "MD5";

    private PatternLockUtils() {
        throw new AssertionError("You can not instantiate this class. Use its static utility " +
                "methods instead");
    }

    /**
     * Serializes a given pattern to its equivalent string representation. You can store this string
     * in any persistence storage or send it to the server for verification
     *
     * @param pattern The actual pattern
     * @return The pattern in its string form
     */
    public static String patternToString(PatternLockView patternLockView,
                                         List<PatternLockView.Dot> pattern) {
        if (pattern == null) {
            return "";
        }
        int patternSize = pattern.size();
        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < patternSize; i++) {
            PatternLockView.Dot dot = pattern.get(i);
            stringBuilder.append((dot.getRow() * patternLockView.getDotCount() + dot.getColumn()));
        }
        return stringBuilder.toString();
    }

    /**
     * De-serializes a given string to its equivalent pattern representation
     *
     * @param string The pattern serialized with {@link #patternToString}
     * @return The actual pattern
     */
    public static List<PatternLockView.Dot> stringToPattern(PatternLockView patternLockView,
                                                            String string) {
        List<PatternLockView.Dot> result = new ArrayList<>();

        for (int i = 0; i < string.length(); i++) {
            int number = Character.getNumericValue(string.charAt(i));
            result.add(PatternLockView.Dot.of(number / patternLockView.getDotCount(),
                    number % patternLockView.getDotCount()));
        }
        return result;
    }

    /**
     * Serializes a given pattern to its equivalent SHA-1 representation. You can store this string
     * in any persistence storage or send it to the server for verification
     *
     * @param pattern The actual pattern
     * @return The SHA-1 string of the pattern
     */
    public static String patternToSha1(PatternLockView patternLockView,
                                       List<PatternLockView.Dot> pattern) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(SHA1);
            messageDigest.update(patternToString(patternLockView, pattern).getBytes(UTF8));

            byte[] digest = messageDigest.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            return String.format((Locale) null,
                    "%0" + (digest.length * 2) + "x", bigInteger).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Serializes a given pattern to its equivalent MD5 representation. You can store this string
     * in any persistence storage or send it to the server for verification
     *
     * @param pattern The actual pattern
     * @return The MD5 string of the pattern
     */
    public static String patternToMD5(PatternLockView patternLockView,
                                      List<PatternLockView.Dot> pattern) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance(MD5);
            messageDigest.update(patternToString(patternLockView, pattern).getBytes(UTF8));

            byte[] digest = messageDigest.digest();
            BigInteger bigInteger = new BigInteger(1, digest);
            return String.format((Locale) null,
                    "%0" + (digest.length * 2) + "x", bigInteger).toLowerCase();
        } catch (NoSuchAlgorithmException e) {
            return null;
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * Generates a random "CAPTCHA" pattern. The generated pattern is easy for the user to re-draw.
     * <p>
     * NOTE: This method is <b>not</b> optimized and <b>not</b> benchmarked yet for large mSize
     * of the pattern's matrix. Currently it works fine with a matrix of {@code 3x3} cells.
     * Be careful when the mSize increases. </p>
     */
    public static ArrayList<PatternLockView.Dot> generateRandomPattern(PatternLockView patternLockView,
                                                                       int size)
            throws IndexOutOfBoundsException {
        if (patternLockView == null) {
            throw new IllegalArgumentException("PatternLockView can not be null.");
        }

        if (size <= 0 || size > patternLockView.getDotCount()) {
            throw new IndexOutOfBoundsException("Size must be in range [1, " +
                    patternLockView.getDotCount() + "]");
        }

        List<Integer> usedIds = new ArrayList<>();
        int lastId = RandomUtils.randInt(patternLockView.getDotCount());
        usedIds.add(lastId);

        while (usedIds.size() < size) {
            // We start from an empty matrix, so there's always a break point to
            // exit this loop
            final int lastRow = lastId / patternLockView.getDotCount();
            final int lastCol = lastId % patternLockView.getDotCount();

            // This is the max available rows/ columns that we can reach from
            // the cell of `lastId` to the border of the matrix.
            final int maxDistance = Math.max(
                    Math.max(lastRow, patternLockView.getDotCount() - lastRow),
                    Math.max(lastCol, patternLockView.getDotCount() - lastCol));

            lastId = -1;

            // Starting from `distance` = 1, find the closest-available
            // neighbour value of the cell [lastRow, lastCol].
            for (int distance = 1; distance <= maxDistance; distance++) {

                // Now we have a square surrounding the current cell. We call it
                // ABCD, in which A is top-left, and C is bottom-right.
                final int rowA = lastRow - distance;
                final int colA = lastCol - distance;
                final int rowC = lastRow + distance;
                final int colC = lastCol + distance;

                int[] randomValues;

                // Process randomly AB, BC, CD, and DA. Break the loop as soon
                // as we find one value.
                final int[] lines = RandomUtils.randIntArray(4);
                for (int line : lines) {
                    switch (line) {
                        case 0: {
                            if (rowA >= 0) {
                                randomValues = RandomUtils.randIntArray(Math.max(0, colA),
                                        Math.min(patternLockView.getDotCount(),
                                                colC + 1));
                                for (int c : randomValues) {
                                    lastId = rowA * patternLockView.getDotCount()
                                            + c;
                                    if (usedIds.contains(lastId))
                                        lastId = -1;
                                    else
                                        break;
                                }
                            }

                            break;
                        }

                        case 1: {
                            if (colC < patternLockView.getDotCount()) {
                                randomValues = RandomUtils.randIntArray(Math.max(0, rowA + 1),
                                        Math.min(patternLockView.getDotCount(),
                                                rowC + 1));
                                for (int r : randomValues) {
                                    lastId = r * patternLockView.getDotCount()
                                            + colC;
                                    if (usedIds.contains(lastId))
                                        lastId = -1;
                                    else
                                        break;
                                }
                            }

                            break;
                        }

                        case 2: {
                            if (rowC < patternLockView.getDotCount()) {
                                randomValues = RandomUtils.randIntArray(Math.max(0, colA),
                                        Math.min(patternLockView.getDotCount(),
                                                colC));
                                for (int c : randomValues) {
                                    lastId = rowC * patternLockView.getDotCount()
                                            + c;
                                    if (usedIds.contains(lastId))
                                        lastId = -1;
                                    else
                                        break;
                                }
                            }

                            break;
                        }

                        case 3: {
                            if (colA >= 0) {
                                randomValues = RandomUtils.randIntArray(Math.max(0, rowA + 1),
                                        Math.min(patternLockView.getDotCount(),
                                                rowC));
                                for (int r : randomValues) {
                                    lastId = r * patternLockView.getDotCount()
                                            + colA;
                                    if (usedIds.contains(lastId))
                                        lastId = -1;
                                    else
                                        break;
                                }
                            }

                            break;
                        }
                    }

                    if (lastId >= 0) break;
                }

                if (lastId >= 0) break;
            }

            usedIds.add(lastId);
        }

        ArrayList<PatternLockView.Dot> result = new ArrayList<>();
        for (int id : usedIds) {
            result.add(PatternLockView.Dot.of(id));
        }

        return result;
    }
}
