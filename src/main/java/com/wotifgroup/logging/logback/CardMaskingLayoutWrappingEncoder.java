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
package com.wotifgroup.logging.logback;

import ch.qos.logback.core.encoder.LayoutWrappingEncoder;
import com.wotifgroup.logging.util.CardMaskingUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

public class CardMaskingLayoutWrappingEncoder<E> extends LayoutWrappingEncoder<E> {

    @Override
    public void doEncode(final E event) throws IOException {
        final String txt = layout.doLayout(event);
        final StringBuilder sb = new StringBuilder(txt);
        CardMaskingUtil.mask(sb);
        outputStream.write(convertToBytes(sb.toString()));
        if (isImmediateFlush())
            outputStream.flush();
    }

    private byte[] convertToBytes(String s) {
        final Charset charset = getCharset();
        if (charset == null) {
            return s.getBytes();
        } else {
            try {
                return s.getBytes(charset.name());
            } catch (UnsupportedEncodingException e) {
                throw new IllegalStateException(
                        "An existing charset cannot possibly be unsupported.");
            }
        }
    }

}
