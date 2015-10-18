/*
 * Copyright 2015 SirWellington Tech.
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
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;

/**
 * Used internally to perform argument checks.
 *
 * @author SirWellington
 */
@tech.sirwellington.alchemy.annotations.access.Internal
@NonInstantiable
public final class Checks
{

    private final static Logger LOG = LoggerFactory.getLogger(Checks.class);

    Checks() throws IllegalAccessException
    {
        throw new IllegalAccessException("cannot instantiate");
    }

    @tech.sirwellington.alchemy.annotations.access.Internal
    @NonInstantiable
    public static class Internal
    {

        Internal() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        public static void checkNotNull(Object ref) throws IllegalArgumentException
        {
            checkNotNull(ref, "");
        }

        public static void checkNotNull(Object ref, String message) throws IllegalArgumentException
        {
            if (ref == null)
            {
                throw new IllegalArgumentException(message);
            }
        }

        public static void checkThat(boolean predicate) throws IllegalArgumentException
        {
            checkThat(predicate, "");
        }

        public static void checkThat(boolean predicate, String message) throws IllegalArgumentException
        {
            if (!predicate)
            {
                throw new IllegalArgumentException(message);
            }
        }
    }

}
