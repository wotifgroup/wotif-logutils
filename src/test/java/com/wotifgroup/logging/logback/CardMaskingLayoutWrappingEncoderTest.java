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

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.PatternLayout;
import ch.qos.logback.classic.spi.LoggingEvent;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

import static org.junit.Assert.assertEquals;

public class CardMaskingLayoutWrappingEncoderTest {

    @Test
    public void maskCardNumbers() throws Exception {
        Logger rootLogger = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        LoggerContext loggerContext = rootLogger.getLoggerContext();
        // we are not interested in auto-configuration
        loggerContext.reset();

        final PatternLayout layout = new PatternLayout();
        layout.setContext(loggerContext);
        layout.setPattern("%m");
        layout.start();

        final CardMaskingLayoutWrappingEncoder encoder = new CardMaskingLayoutWrappingEncoder();

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        encoder.setContext(loggerContext);
        encoder.setLayout(layout);
        encoder.init(baos);
        encoder.start();

        final LoggingEvent event = new LoggingEvent();

        event.setMessage("Value '378282246310005' is not facet-valid");

        encoder.doEncode(event);

        encoder.stop();
        encoder.close();

        layout.stop();

        assertEquals("Value '378282#####0005' is not facet-valid", baos.toString());
    }

}
