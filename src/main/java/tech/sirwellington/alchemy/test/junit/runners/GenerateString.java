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

package tech.sirwellington.alchemy.test.junit.runners;

import java.util.UUID;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static tech.sirwellington.alchemy.generator.StringGenerators.alphabeticString;
import static tech.sirwellington.alchemy.generator.StringGenerators.alphanumericString;
import static tech.sirwellington.alchemy.generator.StringGenerators.hexadecimalString;
import static tech.sirwellington.alchemy.generator.StringGenerators.uuids;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;

/**
 *
 * @author SirWellington
 */
public @interface GenerateString
{

    /*
     * Named value because it allows for @StringGenerator(ALPHABETIC) instead of @StringGenerator(type = ALPHABETIC)
     */
    Type value() default Type.ALPHABETIC;

    int length() default 10;

    public static enum Type
    {
        ALPHABETIC,
        ALPHANUMERIC,
        HEXADECIMAL,
        /**
         * For UUIDS, the {@link #length() } property will be ignored, and instead the standard UUID size from {@link UUID#randomUUID()
         * } is used.
         */
        UUID;

    }

    @Internal
    @NonInstantiable
    static class Values
    {

        Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<String> createGeneratorFor(GenerateString annotation)
        {
            checkNotNull(annotation, "annotation is missing");

            int length = annotation.length();
            checkThat(length > 0, "Invalid @GenerateString use, length must be positive");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateString Annotation missing type");

            switch (type)
            {
                case ALPHABETIC:
                    return alphabeticString(length);
                case ALPHANUMERIC:
                    return alphanumericString(length);
                case HEXADECIMAL:
                    return hexadecimalString(length);
                case UUID:
                    return uuids;
                default:
                    return alphabeticString(length);
            }

        }
    }

}
