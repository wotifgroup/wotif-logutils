/*
 * #%L
 * Wotif Group Logging Utilities
 * %%
 * Copyright (C) 2014 Wotifgroup
 * %%
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
 * #L%
 */
package com.wotifgroup.logging.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Attempts to detect and mask credit card information in log records.
 * 
 * Sure you should never log card numbers, but it's inevitable if you use something like Spring and turn on request or response
 * logging.
 */
public final class CardMaskingUtil {

    private static final char MASK_CHAR = '#';

    private static final Pattern CARD_NUMBER = Pattern
            .compile("(?<!\\d)([23456]\\d{3})(?:[ \\+\\-]|%20)?(\\d{4})(?:[ \\-\\+]|%20)?" +
                    "(\\d{2})(?:[ \\-\\+]|%20)?(\\d{2})(?:[ \\-\\+]|%20)?(\\d{1,4})(?!\\d)");

    private static final Pattern CVV = Pattern
            .compile("SeriesCode=\"([0-9]{3})\"|\"cvv\":\"([0-9]{3})\"");

    private static final int[][] LUHN_TABLE = { { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 }, { 0, 2, 4, 6, 8, 1, 3, 5, 7, 9 } };

    /**
     * Masks all card numbers in the given StringBuffer. Takes care to only replace the actual digits and leave any separators
     * alone.
     * 
     * @param sb
     *            with possible card numbers.
     */
    public static void mask(final StringBuilder sb) {
        maskCC(sb);
        maskCVV(sb);
    }

    private static void maskCVV(StringBuilder sb) {
        final Matcher m = CVV.matcher(sb);
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                if (m.start(i) != -1) {
                    sb.setCharAt(m.start(i), MASK_CHAR);
                    sb.setCharAt(m.start(i) + 1, MASK_CHAR);
                    sb.setCharAt(m.start(i) + 2, MASK_CHAR);
                }
            }
        }
    }

    private static void maskCC(StringBuilder sb) {
        final Matcher m = CARD_NUMBER.matcher(sb);
        while (m.find()) {
            // System.err.println("Card candidate: " + sb.substring(m.start(), m.end()));
            if (isValidCC(m)) {
                sb.setCharAt(m.start(2) + 2, MASK_CHAR);
                sb.setCharAt(m.start(2) + 3, MASK_CHAR);

                final int suffixLength = m.end(5) - m.start(5);
                final int groupFourOffset = m.start(4);
                final int groupThreeOffset = m.start(3);

                switch (suffixLength) {
                case 4:
                    sb.setCharAt(groupFourOffset + 1, MASK_CHAR);
                case 3:
                    sb.setCharAt(groupFourOffset + 0, MASK_CHAR);
                case 2:
                    sb.setCharAt(groupThreeOffset + 1, MASK_CHAR);
                case 1:
                    sb.setCharAt(groupThreeOffset + 0, MASK_CHAR);
                }
            }
        }
    }

    /**
     * Attempts to do a luhn check on a possible card number. Assumes the groups in the matcher point at the card digits.
     * 
     * @param m
     *            matcher containing the card number groups
     * @return true if luhn passes
     */
    private static boolean isValidCC(final Matcher m) {
        int sum = 0;
        int flip = 0;

        for (int i = m.groupCount(); i > 0; i--) {
            final String match = m.group(i);

            for (int j = match.length() - 1; j >= 0; j--) {
                final int n = match.charAt(j) - '0';

                sum += LUHN_TABLE[flip++ & 0x1][n];
            }
        }

        return sum % 10 == 0;
    }

    private CardMaskingUtil() {
        // Utility class
    }

}
