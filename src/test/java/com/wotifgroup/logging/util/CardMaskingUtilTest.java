/*
 * #%L
 * Wotif Group Logging Utilities
 * %%
 * Copyright (C) 2014 The Wotif Group
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


import org.junit.Assert;
import org.junit.Test;

public class CardMaskingUtilTest {

    @Test
    public void maskCVV() {
        assertMasked("SeriesCode=\"###\"", "SeriesCode=\"922\"");
        assertMasked("\"cvv\":\"###\"", "\"cvv\":\"123\"");
        assertMasked("\"SeriesCode\":\"123\"", "\"SeriesCode\":\"123\"");
    }

    @Test
    public void maskCardNumbers() {
        // American Express Test Cards
        assertMasked("Value '378282#####0005' is not facet-valid", "Value '378282246310005' is not facet-valid");
        assertMasked("Value '371449#####8431' is not facet-valid", "Value '371449635398431' is not facet-valid");
        assertMasked("Value '378734#####1000' is not facet-valid", "Value '378734493671000' is not facet-valid");

        // Australian BankCard (deprecated)
        assertMasked("Value '561059######8250' is not facet-valid", "Value '5610591081018250' is not facet-valid");

        // Diners Club
        assertMasked("Value '305693####5904' is not facet-valid", "Value '30569309025904' is not facet-valid");
        assertMasked("Value '385200####3237' is not facet-valid", "Value '38520000023237' is not facet-valid");

        // Discover
        assertMasked("Value '601111######1117' is not facet-valid", "Value '6011111111111117' is not facet-valid");
        assertMasked("Value '601100######9424' is not facet-valid", "Value '6011000990139424' is not facet-valid");

        // JCB
        assertMasked("Value '353011######0000' is not facet-valid", "Value '3530111333300000' is not facet-valid");
        assertMasked("Value '356600######0505' is not facet-valid", "Value '3566002020360505' is not facet-valid");

        // MasterCard
        assertMasked("Value '555555######4444' is not facet-valid", "Value '5555555555554444' is not facet-valid");
        assertMasked("Value '510510######5100' is not facet-valid", "Value '5105105105105100' is not facet-valid");

        // Visa
        assertMasked("Value '411111######1111' is not facet-valid", "Value '4111111111111111' is not facet-valid");
        assertMasked("Value '401288######1881' is not facet-valid", "Value '4012888888881881' is not facet-valid");
        assertMasked("Value '422222###2222' is not facet-valid", "Value '4222222222222' is not facet-valid");

        assertMasked("Value '411111######1111' is not facet-valid", "Value '4111111111111111' is not facet-valid");
        assertMasked("Value '4111 11## #### 1111' is not facet-valid", "Value '4111 1111 1111 1111' is not facet-valid");
        assertMasked("Value ' 4111 11## #### 1111' is not facet-valid", "Value ' 4111 1111 1111 1111' is not facet-valid");
        assertMasked("Value '4111 11## #### 1111 ' is not facet-valid", "Value '4111 1111 1111 1111 ' is not facet-valid");
        assertMasked("Value ' 4111 11## #### 1111 ' is not facet-valid", "Value ' 4111 1111 1111 1111 ' is not facet-valid");
        assertMasked("Value '4111-11##-####-1111' is not facet-valid", "Value '4111-1111-1111-1111' is not facet-valid");

        // Shorter but valid numbers
        assertMasked("Value '385200####3237' is not facet-valid", "Value '38520000023237' is not facet-valid");
        assertMasked("Value '422222###2222' is not facet-valid", "Value '4222222222222' is not facet-valid");

        // Two numbers, one message
        assertMasked("Value '411111######1111 411111######1111' is not facet-valid",
                "Value '4111111111111111 4111111111111111' is not facet-valid");
        assertMasked("Value '411111######1111 422222###2222' is not facet-valid",
                "Value '4111111111111111 4222222222222' is not facet-valid");
        assertMasked("Value '422222###2222 411111######1111' is not facet-valid",
                "Value '4222222222222 4111111111111111' is not facet-valid");

        // Boundaries
        assertMasked("411111######1111' is not facet-valid", "4111111111111111' is not facet-valid");
        assertMasked("Value '411111######1111", "Value '4111111111111111");
        assertMasked("411111######1111", "4111111111111111");
        assertMasked("411111######1111 411111######1111", "4111111111111111 4111111111111111");

        // Not a card number
        assertMasked("Value '9123012 10991' is not facet-valid", "Value '9123012 10991' is not facet-valid");

        // URL encoded
        assertMasked("4111%2011##%20####%201111", "4111%201111%201111%201111");

        // CGI encoded
        assertMasked("4111+11##+####+1111", "4111+1111+1111+1111");
    }

    private void assertMasked(String expected, String input) {
        try {
            StringBuilder sb = new StringBuilder(input);
            CardMaskingUtil.mask(sb);
            Assert.assertEquals(expected, sb.toString());
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Should not throw exception: " + ex.getMessage());
        }
    }

}
