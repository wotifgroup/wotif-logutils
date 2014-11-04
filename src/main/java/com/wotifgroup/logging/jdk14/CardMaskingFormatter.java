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
package com.wotifgroup.logging.jdk14;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.wotifgroup.logging.util.CardMaskingUtil;

/**
 * Delegates to the passed log formatter but masks the messages generated.
 */
public class CardMaskingFormatter extends Formatter {

    private final Formatter wrapped;

    public CardMaskingFormatter(final Formatter wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public String format(LogRecord record) {
        return mask(wrapped.format(record));
    }

    @Override
    public synchronized String formatMessage(LogRecord record) {
        return mask(wrapped.formatMessage(record));
    }

    private String mask(String text) {
        final StringBuilder sb = new StringBuilder(text);
        CardMaskingUtil.mask(sb);
        return sb.toString();
    }

}
