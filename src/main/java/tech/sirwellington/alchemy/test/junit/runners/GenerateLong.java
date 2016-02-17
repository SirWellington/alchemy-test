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

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import tech.sirwellington.alchemy.annotations.access.Internal;
import tech.sirwellington.alchemy.annotations.access.NonInstantiable;
import tech.sirwellington.alchemy.generator.AlchemyGenerator;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static tech.sirwellington.alchemy.generator.NumberGenerators.longs;
import static tech.sirwellington.alchemy.generator.NumberGenerators.positiveLongs;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkNotNull;
import static tech.sirwellington.alchemy.test.Checks.Internal.checkThat;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateLong.Type.POSITIVE;
import static tech.sirwellington.alchemy.test.junit.runners.GenerateLong.Type.RANGE;

/**
 * Used in with the {@link AlchemyTestRunner}, this Annotations allows the 
 * Runtime Injection of Generated Longs from the {@link AlchemyGenerator} library.
 * 
 * Example:
 * <pre>
 * {@code
 * `@RunWith(AlchemyTestRunner.class)
 * public class ExampleTest
 * {
 *   `@GenerateLongs(POSITIVE)
 *    private long hits;
 * 
 *    ...
 * }
 * }
 * </pre>
 * Note, ticks (`) used to escape Javadocs.
 * 
 * @see GenerateInteger
 * @see GenerateString
 * 
 * @author SirWellington
 */
@Target(FIELD)
@Retention(RUNTIME)
public @interface GenerateLong
{

    Type value() default POSITIVE;

    long min() default 0;

    long max() default 0;

    public enum Type
    {
        POSITIVE,
        NEGATIVE,
        ANY,
        RANGE;
    }

    @Internal
    @NonInstantiable
    class Values
    {

        private Values() throws IllegalAccessException
        {
            throw new IllegalAccessException("cannot instantiate");
        }

        static AlchemyGenerator<Long> createGeneratorFor(GenerateLong annotation)
        {
            checkNotNull(annotation, "missing annotation");

            Type type = annotation.value();
            checkNotNull(type, "@GenerateLong missing value");

            if (type == RANGE)
            {
                long min = annotation.min();
                long max = annotation.max();
                checkThat(min < max, "@GenerateLong: min must be less than max");
                return longs(min, max);
            }

            //Cover remaining cases
            switch (type)
            {
                case POSITIVE:
                    return positiveLongs();
                case NEGATIVE:
                    return longs(Long.MIN_VALUE, 0);
                default:
                    return longs(Long.MIN_VALUE, Long.MAX_VALUE);
            }
        }

    }

}
