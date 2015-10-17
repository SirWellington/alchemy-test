/*
 * Copyright 2015 SirWellington.
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
package tech.sirwellington.alchemy.test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tech.sirwellington.alchemy.annotations.access.Internal;

/**
 * Package Private utility class for housing numerical operations
 *
 * @author SirWellington
 */
@Internal
class Numbers
{

    private final static Logger LOG = LoggerFactory.getLogger(Numbers.class);

    /**
     * Attempts to increment the value without potentially circling back to {@link Long#MIN_VALUE}
     *
     * @param value
     *
     * @return
     */
    static long safeIncrement(long value)
    {
        return value == Long.MAX_VALUE ? value : value + 1;
    }

    /**
     * Attempts to decrement the value without potentially circling back to {@link Long#MAX_VALUE}.
     *
     * @param value
     *
     * @return
     */
    static long safeDecrement(long value)
    {
        return value == Long.MIN_VALUE ? value : value - 1;
    }

    /**
     * Attempts to increment the value without potentially circling back to
     * {@link Integer#MIN_VALUE}
     *
     * @param value
     * @return
     */
    static int safeIncrement(int value)
    {
        return value == Integer.MAX_VALUE ? value : value + 1;
    }

    /**
     * Attempts to decrement the value without potentially circling back to
     * {@link Integer#MAX_VALUE}.
     *
     * @param value
     * @return
     */
    static int safeDecrement(int value)
    {
        return value == Integer.MIN_VALUE ? value : value - 1;
    }
}
